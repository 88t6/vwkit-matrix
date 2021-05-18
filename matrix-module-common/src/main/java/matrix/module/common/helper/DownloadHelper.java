package matrix.module.common.helper;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.StreamUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


/**
 * @author wangcheng
 * detail 构造函数 filePath为文件下载路径例如:d:\temp
 * download param(fileName : 文件路径名, ofileName原文件名)
 */
public class DownloadHelper {

    private String filePath;

    public static DownloadHelper getInstance(String filePath) {
        return new DownloadHelper(filePath);
    }

    public DownloadHelper(String filePath) {
        this.filePath = filePath;
    }

    public void download(HttpServletResponse response, String sysFileName, String fileName) {
        OutputStream outputStream = null;
        try {
            try {
                outputStream = response.getOutputStream();
            } catch (IOException e) {
                throw new ServiceException(e);
            }
            File file = new File(filePath, sysFileName);
            try {
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), StandardCharsets.ISO_8859_1));
            } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e);
            }
            response.addHeader("Content-Length", "" + file.length());
            StreamUtil.fileWriteStream(file, outputStream);
        } finally {
            StreamUtil.closeStream(outputStream);
        }
    }
}
