package matrix.module.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 包扫描工具
 *
 * @author wangcheng
 */
public class PackageScanUtil {

    /**
     * 获取文件列表
     *
     * @param fileList 参数
     * @param filePath 参数
     * @return List
     */
    private static List<File> getFileList(List<File> fileList, String filePath) {
        File[] files = (new File(filePath.toString())).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileList.add(file);
                } else {
                    getFileList(fileList, file.getAbsolutePath());
                }
            }
        }
        return fileList;
    }

    /**
     * 获取class列表
     *
     * @param packageName 参数
     * @return List
     */
    private static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        String dirPath = PackageScanUtil.class.getResource("/").getPath() +
                packageName.replace(".", File.separator);
        List<File> fileList = getFileList(new ArrayList<>(), dirPath);
        for (File file : fileList) {
            String classPath = file.getAbsolutePath().replace(File.separator, ".");
            classPath = classPath.substring(classPath.indexOf(packageName), classPath.indexOf(".class"));
            try {
                classList.add(Class.forName(classPath));
            } catch (ClassNotFoundException ignored) {
            }
        }

        return classList;
    }

    /**
     * 扫描
     *
     * @param packageName 参数
     * @param callback 参数
     */
    public static void scan(String packageName, CallBack callback) {
        List<Class<?>> classList = getClasses(packageName);
        if (classList.size() > 0 && callback != null) {
            callback.invoke(classList);
        } else {
            System.err.println(packageName + ".未找到class");
        }
    }

    public interface CallBack {

        /**
         * 调用
         *
         * @param classList 参数
         */
        void invoke(List<Class<?>> classList);
    }
}
