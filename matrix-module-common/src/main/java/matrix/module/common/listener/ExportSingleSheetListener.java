package matrix.module.common.listener;

import java.util.List;

/**
 * 导出单sheet回调函数
 *
 * @author WangCheng
 * date 2020/7/26
 */
public abstract class ExportSingleSheetListener<T> {

    /**
     * 获取数据
     *
     * @param count 处理次数
     * @return 需要写入excel中的数据
     */
    public abstract List<T> getData(Integer count);

}
