package matrix.module.based.utils;

/**
 * @author wangcheng
 * 分页工具
 */
public class PageUtil {

    /**
     * 获取一共多少页
     *
     * @param pageCount 参数
     * @param totalCount 参数
     */
    public static Integer getTotalPage(Integer pageCount, Integer totalCount) {
        return (totalCount % pageCount) == 0 ? (totalCount / pageCount) : (totalCount / pageCount) + 1;
    }

    /**
     * 获取从第几条数据开始
     *
     * @param pageCount 参数
     * @param pageNum 参数
     */
    public static Integer getStartIndex(Integer pageCount, Integer pageNum) {
        return pageCount * (pageNum - 1);
    }
}
