package matrix.module.common.utils;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;

import java.io.File;

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
        Assert.isNotNull(path, "path");
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
}
