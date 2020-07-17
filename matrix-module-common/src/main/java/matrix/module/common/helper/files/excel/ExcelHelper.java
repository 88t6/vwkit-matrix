package matrix.module.common.helper.files.excel;

import matrix.module.common.enums.ExcelEnum;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangcheng
 */
public class ExcelHelper {

    private ExcelMapHelper mapExcelHelper;

    public ExcelHelper(String filePath) {
        mapExcelHelper = new ExcelMapHelper(filePath);
    }

    public static ExcelHelper getInstance(String filePath) {
        return new ExcelHelper(filePath);
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
