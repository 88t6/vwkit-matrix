package matrix.module.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author wangcheng
 * @getPrefix() 获取文件名的前缀.
 * @getSuffix() 获取文件名的后缀.
 * @getPosPath(realPath) 参数：例如/static/upload/... 获取相对路径
 * @getAbsPath(realPath) 参数：例如/static/upload/... 获取绝对路径
 */
public class FileUtil {
    public static String getPrefix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index, fileName.length());
        }
        return "";
    }

    public static String getPosPath(String realPath) {
        String result = null;
        URL fileUrl = null;
        fileUrl = FileUtil.class.getClass().getResource(realPath);
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getClass().getResource("/"));
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getClassLoader().getResource(realPath));
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getClassLoader().getResource("/"));
        if (fileUrl != null) {
            result = fileUrl.getPath();
            if (result.indexOf(realPath) < 0) {
                result = result.substring(0, result.length() - 1) + realPath;
            }
            if (result != null) {
                File fileFolder = new File(result);
                if (!fileFolder.exists()) {
                    fileFolder.mkdirs();
                }
            }
        } else {
            result = getAbsPath(realPath);
        }
        return result;
    }

    public static String getAbsPath(String realPath) {
        String result = null;
        try {
            Process process = Runtime.getRuntime().exec("pwd");
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String sysPath = br.readLine();
            br.close();
            isr.close();
            process.destroy();
            result = sysPath + realPath;
        } catch (IOException e) {
            result = "C:" + realPath.replace("/", "\\");
        }
        if (result != null) {
            File fileFolder = new File(result);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
        }
        return result;
    }

    /**
     * 获取缓存文件夹
     */
    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }
}