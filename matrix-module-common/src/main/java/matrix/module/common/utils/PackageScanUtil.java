package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描工具
 *
 * @author wangcheng
 */
public class PackageScanUtil {

    /**
     * jar类型名称
     */
    private static final String JAR_TYPE_NAME = "jar";

    /**
     * war类型名称
     */
    private static final String WAR_TYPE_NAME = "war";

    /**
     * 文件类型名称
     */
    private static final String FILE_TYPE_NAME = "file";

    /**
     * 类类型名称
     */
    private static final String CLASS_TYPE_NAME = "class";

    /**
     * 非class正则
     */
    private static final String NOT_CLASS_REGEX = "^[A-Za-z0-9.]+\\$[0-9]+$";

    /**
     * 获取class列表
     * @param packageName 包名
     * @return class列表
     */
    public static List<Class<?>> getClassList(String packageName) {
        if (StringUtil.isEmpty(packageName)) {
            return new ArrayList<>();
        }
        List<Class<?>> classes = new ArrayList<>();
        try {
            URL url = PackageScanUtil.class.getResource("");
            //协议获取
            String protocol = url.getProtocol();
            //定义文件列表
            List<String> classPackageNameList = new ArrayList<>();
            if (JAR_TYPE_NAME.equals(protocol)) {
                // jar包 开始扫描
                String jarFilePath = new File(url.getPath()).getAbsolutePath().split("file:")[1]
                        .replace(PackageScanUtil.class.getPackage().getName().replace(".", File.separator), "")
                        .replace("!" + File.separator, "");
                scan(protocol, jarFilePath, classPackageNameList);
            } else if (FILE_TYPE_NAME.equals(protocol)) {
                //根目录路径
                String rootFilePath = new File(url.getPath()).getAbsolutePath().replace(PackageScanUtil.class.getPackage().getName().replace(".", File.separator), "");
                scan(protocol, rootFilePath, classPackageNameList);
            }
            classPackageNameList.forEach(className -> {
                try {
                    if (className.startsWith(packageName)) {
                        classes.add(Class.forName(className));
                    }
                } catch (Throwable e) {
                    //ignore
                }
            });
        } catch (Exception e) {
            throw new ServiceException("find class error", e);
        }
        return classes;
    }

    /**
     * 扫描包
     * @param protocol 协议类型 jar file
     * @param rootFilePath 根路径
     * @param classPackageNameList 包名称，例如（matrix.module.common.annotation.Excel）
     */
    private static void scan(String protocol, String rootFilePath, List<String> classPackageNameList) throws Exception {
        //根文件
        File rootFile = new File(rootFilePath);
        if (JAR_TYPE_NAME.equals(protocol)) {
            try (JarFile jar = new JarFile(new File(rootFilePath))) {
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    if (name.endsWith(CLASS_TYPE_NAME)) {
                        String classPackagePath = name
                                .replace("." + CLASS_TYPE_NAME, "")
                                .replace("/", ".");
                        if (!classPackagePath.matches(NOT_CLASS_REGEX)) {
                            classPackageNameList.add(classPackagePath);
                        }
                    }
                }
            }
        } else if (FILE_TYPE_NAME.equals(protocol)) {
            //定义文件列表
            List<File> files = new ArrayList<>();
            //获取目录下所有文件
            FolderUtil.getFileList(rootFile, files);
            //遍历所有文件
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                String suffix = FileUtil.getSuffix(file.getName());
                if (suffix.contains(CLASS_TYPE_NAME)) {
                    //class文件
                    String classPackagePath = file.getAbsolutePath()
                            .replace(rootFile.getAbsolutePath() + File.separator, "")
                            .replace("." + CLASS_TYPE_NAME, "")
                            .replace(File.separator, ".");
                    if (!classPackagePath.matches(NOT_CLASS_REGEX)) {
                        classPackageNameList.add(classPackagePath);
                    }
                }else if (suffix.contains(JAR_TYPE_NAME) || suffix.contains(WAR_TYPE_NAME)) {
                    // jar包
                    scan(JAR_TYPE_NAME, file.getAbsolutePath(), classPackageNameList);
                }
            }
        }
    }
}
