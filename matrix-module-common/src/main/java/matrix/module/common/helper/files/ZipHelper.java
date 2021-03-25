package matrix.module.common.helper.files;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * zip工具
 *
 * @author wangcheng
 */
public class ZipHelper {

    /**
     * 文件路径
     */
    private final String filePath;

    public static ZipHelper getInstance(String filePath) {
        return new ZipHelper(filePath);
    }

    public ZipHelper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * zip压缩
     * @param sourceFilePath 原文件路径
     * @return 文件名称
     */
    public String doCompress(String[] sourceFilePath) {
        Assert.state(sourceFilePath != null && sourceFilePath.length > 0, "sourceFilePath must not null");
        String folderPath = (filePath.endsWith(File.separator) ? filePath : (filePath + File.separator)) +RandomUtil.getUUID();
        //创建文件夹
        FolderUtil.mkdirs(folderPath);
        //复制原文件至此文件夹
        assert sourceFilePath != null;
        for (String filePath: sourceFilePath) {
            StreamUtil.fileWriteFile(filePath, folderPath + File.separator + new File(filePath).getName());
        }
        try {
            return this.doCompress(folderPath);
        } finally {
            FolderUtil.rmdir(folderPath);
        }
    }

    /**
     * zip压缩
     *
     * @param sourceFolderPath  压缩文件目录或文件
     * @return 文件名称
     */
    public String doCompress(String sourceFolderPath) {
        Assert.state(StringUtil.isNotEmpty(sourceFolderPath), "doCompress参数异常");
        File fileDir = new File(sourceFolderPath);
        List<File> files = new ArrayList<>();
        FolderUtil.getFileList(fileDir, files);
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            String fileName = RandomUtil.getUUID() + ".zip";
            fos = new FileOutputStream(new File(filePath, fileName));
            zos = new ZipOutputStream(fos);
            for (File file : files) {
                if (!file.isDirectory()) {
                    ZipEntry entry = new ZipEntry(file.getAbsolutePath().replace(fileDir.getAbsolutePath() + File.separator, ""));
                    zos.putNextEntry(entry);
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        StreamUtil.streamWriteStream(fis, zos);
                    } finally {
                        StreamUtil.closeStream(fis);
                    }
                } else {
                    //创建空文件夹
                    if (Objects.requireNonNull(file.list()).length <= 0) {
                        zos.putNextEntry(new ZipEntry(file.getAbsolutePath().replace(fileDir.getAbsolutePath() + File.separator, "") + File.separator));
                    }
                }
            }
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(zos);
            StreamUtil.closeStream(fos);
        }
    }

    /**
     * 解压缩
     *
     * @param sourceZipFilePath 压缩文件路径
     * @param destFilePath      解压缩文件路径
     */
    public void doUnCompress(String sourceZipFilePath, String destFilePath) {
        if (sourceZipFilePath == null || "".equals(sourceZipFilePath)
                || destFilePath == null || "".equals(destFilePath)) {
            throw new ServiceException("doUnCompress参数异常");
        }
        File zipFile = new File(sourceZipFilePath);
        if (zipFile.exists()) {
            String fileName = zipFile.getName();
            fileName = fileName.substring(0, fileName.indexOf("."));
            destFilePath = destFilePath + File.separator + fileName;
        }
        File unzipFileDir = new File(destFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            FolderUtil.mkdirs(unzipFileDir.getAbsolutePath());
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryFilePath = destFilePath + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    int index = entryFilePath.lastIndexOf(File.separator);
                    String entryDirPath = "";
                    if (index != -1) {
                        entryDirPath = entryFilePath.substring(0, index);
                    }
                    File entryDir = new File(entryDirPath);
                    if (!entryDir.exists() || !entryDir.isDirectory()) {
                        FolderUtil.mkdirs(entryDir.getAbsolutePath());
                    }
                    File entryFile = new File(entryFilePath);
                    FileUtil.rm(entryFile);
                    FileOutputStream fos = null;
                    InputStream is = null;
                    try {
                        fos = new FileOutputStream(entryFile);
                        is = zip.getInputStream(entry);
                        StreamUtil.streamWriteStream(is, fos);
                    } finally {
                        StreamUtil.closeStream(is);
                        StreamUtil.closeStream(fos);
                    }
                } else {
                    File entryFile = new File(entryFilePath);
                    FileUtil.rm(entryFile);
                    FolderUtil.mkdirs(entryFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(zip);
        }
    }
}
