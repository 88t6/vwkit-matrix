package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangcheng
 * 2020/7/3
 **/
public class FolderUtil {

    /**
     * 创建文件夹
     * @param paths 多个文件夹地址
     */
    public static void mkdirs(String... paths) {
        for (String path: paths) {
            FolderUtil.mkdirs(path);
        }
    }

    /**
     * 创建文件夹
     * @param path 单个文件夹地址
     */
    private static void mkdirs(String path) {
        Assert.notNullTip(path, "path");
        path = path.replace(File.separator, "&");
        String[] folders = path.split("&");
        StringBuilder folderPath = new StringBuilder();
        File file;
        for (String folder : folders) {
            folderPath.append(folder).append(File.separator);
            file = new File(folderPath.toString());
            if (!file.exists() && !file.mkdirs()) {
                throw new ServiceException("文件夹创建失败");
            }
        }
    }

    /**
     * 递归文件夹中得文件
     * @param folderPath 文件夹目录
     * @param files 文件列表
     */
    public static void getFileList(File folderPath, List<File> files) {
        if (folderPath != null && Objects.requireNonNull(folderPath.list()).length > 0) {
            File[] listFiles = folderPath.listFiles();
            assert listFiles != null;
            for (File file : listFiles) {
                files.add(file);
                if (file.isDirectory()) {
                    FolderUtil.getFileList(file, files);
                }
            }
        }
    }

    /**
     * 删除文件夹
     * @param folderPath 文件夹地址
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public static void rmdir(String folderPath) {
        List<File> files = new ArrayList<>();
        File folderFile = new File(folderPath);
        FolderUtil.getFileList(folderFile, files);
        if (!CollectionUtils.isEmpty(files)) {
            files.forEach(File::delete);
        }
        folderFile.delete();
    }
}
