package matrix.module.common.listener;

import matrix.module.common.bean.ExcelColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

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

    /**
     * 处理数据前操作
     * @param workbook 工作簿
     */
    public void beforeProcessData(Workbook workbook) {

    }

    /**
     * 处理中的数据
     * @param column 单元格信息
     * @param cell 实际单元格
     */
    public void processingData(ExcelColumn column, Cell cell) {

    }

    /**
     * 处理数据后操作
     * @param workbook 工作簿
     */
    public void afterProcessData(Workbook workbook) {

    }

}
