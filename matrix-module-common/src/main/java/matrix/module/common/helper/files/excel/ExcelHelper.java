package matrix.module.common.helper.files.excel;

import matrix.module.common.enums.ExcelEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangcheng
 */
public class ExcelHelper {

    private ExcelBeanHelper beanExcelHelper;

    private ExcelMapHelper mapExcelHelper;

    public ExcelHelper(String filePath) {
        beanExcelHelper = new ExcelBeanHelper(filePath);
        mapExcelHelper = new ExcelMapHelper(filePath);
    }

    public static ExcelHelper getInstance(String filePath) {
        return new ExcelHelper(filePath);
    }

    /**
     * 导出Excel With Bean
     *
     * @param exportData 参数
     * @param excelEnum 参数
     * @return String
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String exportExcelForBean(Map exportData, ExcelEnum excelEnum) {
        return beanExcelHelper.exportExcel(exportData, excelEnum);
    }

    /**
     * 导出Excel With Bean
     *
     * @param data 参数
     * @param excelEnum 参数
     * @return String
     */
    public String exportExcelForBean(List<? extends Serializable> data, ExcelEnum excelEnum) {
        Map<String, List<? extends Serializable>> exportData = new HashMap<>();
        exportData.put(String.valueOf(System.currentTimeMillis()), data);
        return exportExcelForBean(exportData, excelEnum);
    }

    /**
     * 导出Excel With Map
     *
     * @param exportData 参数
     * @param excelEnum 参数
     * @return String
     */
    public String exportExcelForMap(Map<String, List<LinkedHashMap<String, Object>>> exportData, ExcelEnum excelEnum) {
        return mapExcelHelper.exportExcel(exportData, excelEnum);
    }

    /**
     * 导出Excel With Map
     *
     * @param data 参数
     * @param excelEnum 参数
     * @return String
     */
    public String exportExcelForMap(List<LinkedHashMap<String, Object>> data, ExcelEnum excelEnum) {
        Map<String, List<LinkedHashMap<String, Object>>> exportData = new HashMap<>();
        exportData.put(String.valueOf(System.currentTimeMillis()), data);
        return exportExcelForMap(exportData, excelEnum);
    }

    /**
     * 导入Excel
     *
     * @param fileName 参数
     * @param excelEnum 参数
     * @return Map
     */
    public Map<String, List<LinkedHashMap<String, Object>>> importExcel(String fileName, ExcelEnum excelEnum) {
        return mapExcelHelper.importExcel(fileName, excelEnum);
    }
}
