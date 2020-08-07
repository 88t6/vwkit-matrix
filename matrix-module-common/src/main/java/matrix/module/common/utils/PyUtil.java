package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;

import java.io.File;
import java.io.InputStream;

/**
 * @author wangcheng
 */
public class PyUtil {

    private static final Runtime runtime = Runtime.getRuntime();

    /**
     * 执行resource下的py文件
     * @param resourceFilePath 参数
     * @param args 参数
     * @return String
     */
    public static String execFile(String resourceFilePath, String ... args) {
        InputStream is;
        try {
            is = PyUtil.class.getClassLoader().getResourceAsStream(resourceFilePath);
            if (is == null) {
                throw new ServiceException("py文件未找到");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        try {
            return exec(StreamUtil.streamToString(is), args);
        } finally {
            StreamUtil.closeStream(is);
        }
    }

    /**
     * 执行python命令
     * @param command 参数
     * @return String
     */
    public static String execCommand(String command) {
        return exec(command, "");
    }

    private static String exec(String command, String ... args) {
        String filePath = FileUtil.getTmpDir();
        String fileName = RandomUtil.getUUID() + ".py";
        File file = StreamUtil.stringWriteFile(command, filePath, fileName);
        try {
            Process process = runtime.exec(String.format("python %s %s",
                    file.getPath(), (args != null ? "\"" + String.join("\" \"", args) + "\"" : "")));
            if (process.waitFor() == 0) {
                return StreamUtil.streamToString(process.getInputStream());
            }
            return StreamUtil.streamToString(process.getErrorStream());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            if (file.exists() && !file.delete()) {
                System.err.println("delete error");
            }
        }
    }
}
