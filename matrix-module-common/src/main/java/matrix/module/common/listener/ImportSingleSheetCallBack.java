package matrix.module.common.listener;

import java.util.List;

/**
 * 导入回调函数
 *
 * @author WangCheng
 * date 2020/7/26
 */
public abstract class ImportSingleSheetCallBack<T, S> {

    /**
     * 处理数据
     *
     * @param sheetName sheet名
     * @param rows      总行数
     * @return 处理需要返回的值
     */
    public abstract List<T> processData(String sheetName, List<S> rows);

}
