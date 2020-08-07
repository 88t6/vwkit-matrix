package matrix.module.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author wangcheng
 * getPrefix() 获取文件名的前缀.
 * getSuffix() 获取文件名的后缀.
 * getPosPath(realPath) 参数：例如/static/upload/... 获取相对路径
 * getAbsPath(realPath) 参数：例如/static/upload/... 获取绝对路径
 */
public class FileUtil {
    public static String getPrefix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index);
        }
        return "";
    }

    public static String getPosPath(String realPath) {
        String result;
        URL fileUrl;
        fileUrl = FileUtil.class.getResource(realPath);
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getResource("/"));
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getClassLoader().getResource(realPath));
        fileUrl = fileUrl != null ? fileUrl : (FileUtil.class.getClassLoader().getResource("/"));
        if (fileUrl != null) {
            result = fileUrl.getPath();
            if (!result.contains(realPath)) {
                result = result.substring(0, result.length() - 1) + realPath;
            }
            File fileFolder = new File(result);
            if (!fileFolder.exists()) {
                FolderUtil.mkdirs(fileFolder.getAbsolutePath());
            }
        } else {
            result = getAbsPath(realPath);
        }
        return result;
    }

    public static String getAbsPath(String realPath) {
        String result;
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
        File fileFolder = new File(result);
        if (!fileFolder.exists()) {
            FolderUtil.mkdirs(fileFolder.getAbsolutePath());
        }
        return result;
    }

    /**
     * 获取缓存文件夹
     * @return String
     */
    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
