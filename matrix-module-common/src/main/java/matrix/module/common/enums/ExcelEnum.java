package matrix.module.common.enums;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author wangcheng
 */
public enum ExcelEnum {

    EXCEL2003(HSSFWorkbook.class, ".xls"),
    EXCEL2007(SXSSFWorkbook.class, ".xlsx");

    private Class<?> clazz;

    private String suffix;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    ExcelEnum(Class<?> clazz, String suffix) {
        this.clazz = clazz;
        this.suffix = suffix;
    }}
