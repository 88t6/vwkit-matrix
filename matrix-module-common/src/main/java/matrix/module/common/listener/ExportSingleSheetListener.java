package matrix.module.common.listener;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 导出单sheet回调函数
 *
 * @author WangCheng
 * date 2020/7/26
 */
public abstract class ExportSingleSheetListener<T> {

    /**
     * 处理数据前操作
     * @param workbook 工作簿
     */
    public void beforeProcessData(Workbook workbook) {

    }

    /**
     * 获取数据
     *
     * @param count 处理次数
     * @return 需要写入excel中的数据
     */
    public abstract List<T> getData(Integer count);

    /**
     * 处理数据后操作
     * @param workbook 工作簿
     */
    public void afterProcessData(Workbook workbook) {

    }

}
