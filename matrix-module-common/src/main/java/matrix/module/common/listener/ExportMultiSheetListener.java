package matrix.module.common.listener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出多sheet回调函数
 *
 * @author WangCheng
 * date 2020/7/26
 */
public abstract class ExportMultiSheetListener<T> {

    /**
     * 获取数据
     *
     * @param count 处理次数
     * @return 需要写入excel中的数据
     */
    public abstract LinkedHashMap<String, List<T>> getData(Integer count);

}
