package matrix.module.common.helper.files.excel;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.annotation.Excel;
import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author wangcheng
 */
class ExcelBeanHelper {

    private String filePath;

    protected ExcelBeanHelper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 导出Excel
     *
     * @param exportData 参数
     * @param excelEnum 参数
     * @return String
     */
    protected String exportExcel(Map<String, List<? extends Serializable>> exportData, ExcelEnum excelEnum) {
        Assert.isNotNull(exportData, "exportData");
        Assert.isNotNull(excelEnum, "excelEnum");
        String fileName = RandomUtil.getUUID() + excelEnum.getSuffix();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(this.filePath, fileName));
            this.outputWorkbook(excelEnum, exportData, fos);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
        }
        return fileName;
    }

    /**
     * 获取Excel并写入流中
     */
    private void outputWorkbook(ExcelEnum excelEnum, Map<String, List<? extends Serializable>> exportData, OutputStream os) {
        Workbook book = null;
        try {
            if (excelEnum.getClazz().equals(SXSSFWorkbook.class)) {
                book = new SXSSFWorkbook(5000);
            } else {
                book = (Workbook) excelEnum.getClazz().newInstance();
            }
            this.generateExcelFile(book, exportData);
            book.write(os);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(book);
        }
    }

    /**
     * Excel生成
     */
    private void generateExcelFile(Workbook book, Map<String, List<? extends Serializable>> exportData) {
        Iterator<String> iterator = exportData.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<? extends Serializable> list = exportData.get(key);
            Sheet sheet = book.createSheet("AutoCreate_" + key);
            if (list == null || list.size() <= 0) {
                continue;
            }
            List<ExcelColumnData> excelColumnData = parseExcelColumnData(list.get(0));
            //标题
            Row title = sheet.createRow(0);
            for (int i = 0; i < excelColumnData.size(); i++) {
                ExcelColumnData columnData = excelColumnData.get(i);
                sheet.setColumnWidth(i, columnData.getWidth() * 20);
                exportDataConvertor(title.createCell(i), columnData.getColumnName(), columnData, true);
            }
            //数据
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(list.get(i)));
                for (int j = 0; j < excelColumnData.size(); j++) {
                    ExcelColumnData columnData = excelColumnData.get(j);
                    exportDataConvertor(row.createCell(j), data.get(columnData.getName()), columnData, false);
                }
            }

        }
    }

    /**
     * 导出数据转换
     */
    private void exportDataConvertor(Cell cell, Object value, ExcelColumnData columnData, boolean isTitle) {
        Workbook book = cell.getSheet().getWorkbook();
        CellStyle cellStyle = book.createCellStyle();
        if (isTitle) {
            cell.setCellValue(String.valueOf(value));
        } else {
            if (value == null) {
                cell.setCellValue("");
                return;
            }
            if (columnData.getType().equals(Date.class)) {
                cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                Date date = new Date();
                date.setTime(Long.valueOf(String.valueOf(value)));
                cell.setCellValue(date);
            } else if (columnData.getType().getName().equals(Double.class.getName())) {
                cell.setCellValue(Double.valueOf(String.valueOf(value)));
            } else if (columnData.getType().getName().equals(Boolean.class.getName())) {
                cell.setCellValue(Boolean.valueOf(String.valueOf(value)));
            } else {
                cell.setCellValue(String.valueOf(value));
            }
        }
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 获取实体对应的excel注解类型
     *
     * @return List
     */
    private <T extends Serializable> List<ExcelColumnData> parseExcelColumnData(T bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields.length <= 0) {
            throw new ServiceException("实体字段为空");
        }
        List<ExcelColumnData> result = new ArrayList<>();
        ExcelColumnData excelColumnData;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Excel annotation = field.getDeclaredAnnotation(Excel.class);
            if (annotation == null) {
                continue;
            }
            excelColumnData = new ExcelColumnData();
            excelColumnData.setName(field.getName());
            //value字段解析
            if (annotation.value() != null && !"".equals(annotation.value())) {
                excelColumnData.setColumnName(annotation.value());
            } else {
                excelColumnData.setColumnName(field.getName());
            }
            //单元格宽度解析
            excelColumnData.setWidth(annotation.width());
            //type字段解析
            excelColumnData.setType(field.getType());
            result.add(excelColumnData);
        }
        if (result.isEmpty()) {
            throw new ServiceException("please add annotation @Excel to bean field!");
        }
        return result;
    }

    class ExcelColumnData {
        private String name;

        private String columnName;

        private int width;

        private Class<?> type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }
    }
}
