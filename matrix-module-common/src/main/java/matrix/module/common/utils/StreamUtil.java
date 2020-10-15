package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author wangcheng
 */
public class StreamUtil {

    public static String streamToString(InputStream is) {
        StringBuilder result = new StringBuilder("");
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n\r");
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            closeStream(br);
            closeStream(isr);
        }
        return result.toString();
    }

    public static File streamWriteFile(InputStream is, String filePath) {
        return StreamUtil.streamWriteFile(is, filePath, null);
    }

    @SuppressWarnings("all")
    public static File streamWriteFile(InputStream is, String filePath, String fileName) {
        FileOutputStream fos = null;
        File file = StringUtil.isEmpty(fileName) ? new File(filePath) : new File(filePath, fileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            streamWriteStream(is, fos);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            closeStream(fos);
        }
        return file;
    }

    public static void fileWriteStream(File file, OutputStream os) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            streamWriteStream(fis, os);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            closeStream(fis);
        }
    }

    public static void streamWriteStream(InputStream is, OutputStream os) {
        try {
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) > 0) {
                os.write(b, 0, len);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public static File fileWriteFile(String sourceFilePath, String distFilePath) {
        File file = new File(sourceFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return StreamUtil.streamWriteFile(fis, distFilePath);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fis);
        }
    }

    public static File stringWriteFile(String command, String filePath) {
        return StreamUtil.stringWriteFile(command, filePath, null);
    }

    @SuppressWarnings("all")
    public static File stringWriteFile(String command, String filePath, String fileName) {
        File file = StringUtil.isEmpty(fileName) ? new File(filePath) : new File(filePath, fileName);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write(command);
            return file;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(osw);
            StreamUtil.closeStream(fos);
        }
    }

    public static void closeStream(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ignored) {
        }
    }
}
