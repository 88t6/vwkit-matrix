package matrix.module.common.helper.files.excel;

import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * @author wangcheng
 */
class ExcelMapHelper {

    private String filePath;

    protected ExcelMapHelper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 导入excel
     *
     * @return Map
     */
    protected Map<String, List<LinkedHashMap<String, Object>>> importExcel(String fileName, ExcelEnum excelEnum) {
        Assert.isNotNull(fileName, "fileName");
        Assert.isNotNull(excelEnum, "excelEnum");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath, fileName));
            return parseWorkbook(fis, excelEnum);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fis);
        }
    }

    /**
     * 解析workbook
     *
     * @param is 参数
     * @return Map
     */
    private Map<String, List<LinkedHashMap<String, Object>>> parseWorkbook(InputStream is, ExcelEnum excelEnum) {
        Map<String, List<LinkedHashMap<String, Object>>> result = new HashMap<>();
        Workbook book = null;
        try {
            if (excelEnum.getClazz().equals(SXSSFWorkbook.class)
                    || excelEnum.getClazz().equals(XSSFWorkbook.class)) {
                book = new XSSFWorkbook(is);
            } else {
                book = new HSSFWorkbook(is);
            }
            if (book.getNumberOfSheets() > 0) {
                for (int i = 0; i < book.getNumberOfSheets(); i++) {
                    Sheet sheet = book.getSheetAt(i);
                    List<LinkedHashMap<String, Object>> rows = new ArrayList<>();
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) {
                            continue;
                        }
                        LinkedHashMap<String, Object> cells = new LinkedHashMap<>();
                        for (Cell cell : row) {
                            String key = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                            this.importDataConvertor(key, cell, cells);
                        }
                        rows.add(cells);
                    }
                    result.put(sheet.getSheetName(), rows);
                }
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(book);
        }
    }

    /**
     * 导入数据公式字段匹配获取
     */
    private void importDataConvertor(String key, Cell cell, LinkedHashMap<String, Object> cells) {
        String cellType = cell.getCellTypeEnum().name();
        if (CellType._NONE.name().equals(cellType)) {
        	cells.put(key, null);
        	return ;
        }
        if (CellType.NUMERIC.name().equals(cellType)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cells.put(key, cell.getDateCellValue());
            } else {
                cells.put(key, cell.getNumericCellValue());
            }
            return ;
        }
        if (CellType.STRING.name().equals(cellType)) {
        	cells.put(key, cell.getStringCellValue());
        	return ;
        }
        if (CellType.FORMULA.name().equals(cellType)) {
        	throw new ServiceException(
        			String.format("excel parse error not support formula %s by row:%d cell:%d",
        					cell.getCellFormula(), cell.getRowIndex() + 1, cell.getColumnIndex() + 1));
        }
        if (CellType.BLANK.name().equals(cellType)) {
        	cells.put(key, "");
        	return ;
        }
        if (CellType.BOOLEAN.name().equals(cellType)) {
            cells.put(key, cell.getBooleanCellValue());
            return ;
        }
        if (CellType.ERROR.name().equals(cellType)) {
        	throw new ServiceException("excel import error code: " + cell.getErrorCellValue());
        }
        cells.put(key, cell.getStringCellValue());
    }

    /**
     * 导出excel
     */
    protected String exportExcel(Map<String, List<LinkedHashMap<String, Object>>> exportData, ExcelEnum excelEnum) {
        Assert.isNotNull(exportData, "exportData");
        Assert.isNotNull(excelEnum, "excelEnum");
        String fileName = RandomUtil.getUUID() + excelEnum.getSuffix();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(this.filePath, fileName));
            outputWorkbook(excelEnum, exportData, fos);
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
    private void outputWorkbook(ExcelEnum excelEnum, Map<String, List<LinkedHashMap<String, Object>>> exportData, OutputStream os) {
        Workbook book = null;
        try {
            if (excelEnum.getClazz().equals(SXSSFWorkbook.class)) {
                book = new SXSSFWorkbook(5000);
            } else {
                book = (Workbook) excelEnum.getClazz().newInstance();
            }
            generateExcelFile(book, exportData);
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
    private void generateExcelFile(Workbook book, Map<String, List<LinkedHashMap<String, Object>>> exportData) {
        for (String key : exportData.keySet()) {
            List<LinkedHashMap<String, Object>> list = exportData.get(key);
            Sheet sheet = book.createSheet("AutoCreate_" + key);
            if (list == null || list.isEmpty()) {
                continue;
            }
            //标题
            Row title = sheet.createRow(0);
            Iterator<String> titleData = list.get(0).keySet().iterator();
            int i = 0;
            while (titleData.hasNext()) {
                exportDataConvertor(title.createCell(i++), titleData.next(), true);
            }
            //数据
            for (i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Iterator<String> data = list.get(i).keySet().iterator();
                int j = 0;
                while (data.hasNext()) {
                    Object value = list.get(i).get(data.next());
                    exportDataConvertor(row.createCell(j++), value, false);
                }
            }
        }
    }

    /**
     * 导出数据转换
     */
    private void exportDataConvertor(Cell cell, Object value, boolean isTitle) {
        Workbook book = cell.getSheet().getWorkbook();
        CellStyle cellStyle = book.createCellStyle();
        if (isTitle) {
            cell.setCellValue(String.valueOf(value));
        } else {
            if (value instanceof Date) {
                cell.setCellValue((Date) value);
                DataFormat df = book.createDataFormat();
                cellStyle.setDataFormat(df.getFormat("yyyy-MM-dd HH:mm:ss"));
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue(String.valueOf(value));
            }
        }
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
}
