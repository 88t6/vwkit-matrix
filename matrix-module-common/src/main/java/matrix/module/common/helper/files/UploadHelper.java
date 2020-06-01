package matrix.module.common.helper.files;

import matrix.module.common.bean.UploadProgress;
import matrix.module.common.bean.UploadResult;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.FileUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.common.utils.ThreadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangcheng
 */
public class UploadHelper {

    private static final String MULTIPART_FLAG = "multipart";

    private String filePath, suffixFilter;
    private Long fileMaxSize;

    private DiskFileItemFactory diskFileItemFactory;
    private ServletFileUpload servletFileUpload;

    public static UploadHelper getInstance(String filePath, Long fileMaxSize, String suffixFilter) {
        return new UploadHelper(filePath, fileMaxSize, suffixFilter);
    }

    public UploadHelper(String filePath, Long fileMaxSize, String suffixFilter) {
        Assert.isNotNull(filePath, "filePath");
        Assert.isNotNull(suffixFilter, "suffixFilter");
        this.filePath = filePath;
        this.fileMaxSize = fileMaxSize;
        this.suffixFilter = suffixFilter;
        diskFileItemFactory = new DiskFileItemFactory();
        servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setSizeMax(fileMaxSize);
        File cacheDir = new File(filePath + "/cache");
        cacheDir.mkdirs();
        diskFileItemFactory.setRepository(cacheDir);
        diskFileItemFactory.setSizeThreshold(1024 * 2048);
    }

    /**
     * 文件上传
     *
     * @return Map
     */
    public Map<String, UploadResult> upload(HttpServletRequest request) {
        return upload(request, null);
    }

    /**
     * 文件上传
     *
     * @return Map
     */
    public Map<String, UploadResult> upload(HttpServletRequest request, UploadProgress<?> progress) {
        if (!request.getClass().getName().toLowerCase().contains(MULTIPART_FLAG)) {
            return uploadNoMultipart(request, progress);
        } else {
            return uploadMultipart(request);
        }
    }

    /**
     * spring.servlet.multipart.enabled=true
     *
     * @param request
     * @return Map
     */
    public Map<String, UploadResult> uploadMultipart(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            if (parts != null) {
                CountDownLatch countDownLatch = new CountDownLatch(parts.size());
                Map<String, UploadResult> result = new HashMap<>(10);
                for (Part part : parts) {
                    if (part.getContentType() == null) {
                        countDownLatch.countDown();
                        continue;
                    }
                    String fieldName = part.getName();
                    String fileName = part.getHeader("content-disposition").split("filename=\"")[1].replaceAll("\"", "");
                    String suffixName = FileUtil.getSuffix(fileName);
                    String sysFileName = RandomUtil.getUUID() + suffixName;
                    UploadResult resultBean = new UploadResult(fieldName, fileName, sysFileName);
                    if (part.getSize() <= 0) {
                        resultBean.setMessage("文件大小为0B");
                        result.put(fieldName, resultBean);
                        countDownLatch.countDown();
                        continue;
                    }
                    if (part.getSize() > this.fileMaxSize) {
                        resultBean.setMessage("文件大于" + this.fileMaxSize + "B");
                        result.put(fieldName, resultBean);
                        countDownLatch.countDown();
                        continue;
                    }
                    if (!"*".equals(this.suffixFilter) && this.suffixFilter.indexOf(suffixName) < 0) {
                        resultBean.setMessage("文件类型不在" + this.suffixFilter + "范围内");
                        result.put(fieldName, resultBean);
                        countDownLatch.countDown();
                        continue;
                    }
                    ThreadUtil.startThread(new Runnable() {
                        @Override
                        public void run() {
                            InputStream is = null;
                            try {
                                is = part.getInputStream();
                                StreamUtil.streamWriteFile(is, filePath, sysFileName);
                                resultBean.setSuccess(true);
                                result.put(fieldName, resultBean);
                            } catch (IOException e) {
                                System.out.println("Multipart:" + e.getMessage());
                            } finally {
                                countDownLatch.countDown();
                                StreamUtil.closeStream(is);
                            }
                        }
                    });
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new ServiceException(e);
                }
                return result;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return null;
    }

    /**
     * spring.servlet.multipart.enabled=false
     *
     * @param request
     * @param progress
     * @return Map
     */
    public Map<String, UploadResult> uploadNoMultipart(HttpServletRequest request, UploadProgress<?> progress) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            System.err.println("ContentType不存在(multipart/form-data)上传失败");
            return null;
        }
        if (progress != null) {
            servletFileUpload.setProgressListener(progress);
        }
        List<FileItem> list;
        try {
            list = servletFileUpload.parseRequest(request);
        } catch (FileUploadException e) {
            throw new ServiceException(e);
        }
        if (list != null) {
            CountDownLatch countDownLatch = new CountDownLatch(list.size());
            Map<String, UploadResult> result = new HashMap<>(10);
            for (FileItem item : list) {
                if (item.isFormField()) {
                    countDownLatch.countDown();
                    continue;
                }
                String fieldName = item.getFieldName();
                String fileName = item.getName();
                String suffixName = FileUtil.getSuffix(fileName);
                String sysFileName = RandomUtil.getUUID() + suffixName;
                UploadResult resultBean = new UploadResult(fieldName, fileName, sysFileName);
                if (item.getSize() <= 0) {
                    resultBean.setMessage("文件大小为0B");
                    result.put(fieldName, resultBean);
                    countDownLatch.countDown();
                    continue;
                }
                if (item.getSize() > this.fileMaxSize) {
                    resultBean.setMessage("文件大于" + this.fileMaxSize + "B");
                    result.put(fieldName, resultBean);
                    countDownLatch.countDown();
                    continue;
                }
                if (!"*".equals(this.suffixFilter) && !this.suffixFilter.contains(suffixName)) {
                    resultBean.setMessage("文件类型不在" + this.suffixFilter + "范围内");
                    result.put(fieldName, resultBean);
                    countDownLatch.countDown();
                    continue;
                }
                ThreadUtil.startThread(() -> {
                    InputStream is = null;
                    try {
                        is = item.getInputStream();
                        StreamUtil.streamWriteFile(is, filePath, sysFileName);
                        resultBean.setSuccess(true);
                        result.put(fieldName, resultBean);
                    } catch (IOException e) {
                        System.out.println("No-Multipart:" + e.getMessage());
                    } finally {
                        countDownLatch.countDown();
                        StreamUtil.closeStream(is);
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new ServiceException(e);
            }
            return result;
        }
        return null;
    }
}
