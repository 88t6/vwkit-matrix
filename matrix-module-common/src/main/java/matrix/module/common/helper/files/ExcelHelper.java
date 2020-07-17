package matrix.module.common.helper.files;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.bean.ExcelColumn;
import matrix.module.common.convert.ExcelColumnConvert;
import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.ClassUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.common.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * excel导出工具
 *
 * @author wangcheng
 * 2020/7/17
 **/
public class ExcelHelper {

    private static final int ROW_ACCESS_WINDOW_SIZE = 5000;

    //文件路径
    private String filePath;

    public ExcelHelper(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取实例
     *
     * @param filePath 文件地址
     * @return 文件名
     */
    public static ExcelHelper getInstance(String filePath) {
        return new ExcelHelper(filePath);
    }

    /**
     * 导出Bean单个sheet
     *
     * @param data      单个工作表数据
     * @param excelEnum excel导入类型
     * @param fileName  文件名称（空代表新创建）
     * @return 文件名
     */
    public <T> String exportSingleForBean(List<T> data, ExcelEnum excelEnum, String fileName) {
        Map<String, List<T>> sheetData = new HashMap<>();
        sheetData.put("AutoCreate_0", data);
        return exportForBean(sheetData, excelEnum, fileName);
    }

    /**
     * 导出Map单个sheet
     *
     * @param data      单个工作表数据
     * @param excelEnum excel导入类型
     * @param fileName  文件名称（空代表新创建）
     * @return 文件名
     */
    public String exportSingleForMap(List<LinkedHashMap<String, Object>> data, ExcelEnum excelEnum, String fileName) {
        Map<String, List<LinkedHashMap<String, Object>>> sheetData = new HashMap<>();
        sheetData.put("AutoCreate_0", data);
        return exportForMap(sheetData, excelEnum, fileName);
    }

    /**
     * 导出Bean多个sheet（需要顺序请用LinkedHashMap）
     *
     * @param sheetData 多个工作表数据
     * @param excelEnum excel导入类型
     * @param fileName  文件名称（空代表新创建）
     * @return 文件名
     */
    public <T> String exportForBean(Map<String, List<T>> sheetData, ExcelEnum excelEnum, String fileName) {
        Map<String, List<List<ExcelColumn>>> excelData = new HashMap<>();
        sheetData.forEach((sheetName, list) -> {
            if (CollectionUtils.isNotEmpty(list)) {
                excelData.put(sheetName, ExcelColumnConvert.convertForBean(list));
            }
        });
        return this.exportExcel(excelData, excelEnum, fileName);
    }

    /**
     * 导出Map多个sheet（需要顺序请用LinkedHashMap）
     *
     * @param sheetData 多个工作表数据
     * @param excelEnum excel导入类型
     * @param fileName  文件名称（空代表新创建）
     * @return 文件名
     */
    public String exportForMap(Map<String, List<LinkedHashMap<String, Object>>> sheetData, ExcelEnum excelEnum, String fileName) {
        Map<String, List<List<ExcelColumn>>> excelData = new HashMap<>();
        sheetData.forEach((sheetName, list) -> {
            if (CollectionUtils.isNotEmpty(list)) {
                excelData.put(sheetName, ExcelColumnConvert.convertForMap(list));
            }
        });
        return this.exportExcel(excelData, excelEnum, fileName);
    }


    /**
     * 导出excel(核心方法)
     *
     * @param sheetData 工作表数据
     * @param excelEnum excel导入类型
     * @param fileName  文件名称（空代表新创建）
     * @return 文件名
     */
    private String exportExcel(Map<String, List<List<ExcelColumn>>> sheetData, ExcelEnum excelEnum, String fileName) {
        Assert.state(sheetData != null && sheetData.size() > 0, "data not found");
        assert sheetData != null;
        Workbook book = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (StringUtil.isEmpty(fileName)) {
                fileName = RandomUtil.getUUID() + excelEnum.getSuffix();
                if (excelEnum.getClazz().equals(SXSSFWorkbook.class)) {
                    book = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
                } else {
                    book = (Workbook) excelEnum.getClazz().newInstance();
                }
            } else {
                File file = new File(filePath, fileName);
                Assert.state(file.exists(), "file not found!");
                fis = new FileInputStream(file);
                if (excelEnum.getClazz().equals(SXSSFWorkbook.class) || excelEnum.getClazz().equals(XSSFWorkbook.class)) {
                    book = new XSSFWorkbook(fis);
                } else if (excelEnum.getClazz().equals(HSSFWorkbook.class)) {
                    book = new HSSFWorkbook(fis);
                }
            }
            Assert.state(book != null, "book创建失败");
            assert book != null;
            //创建列类型字典
            Map<Class<?>, CellStyle> cellStyleMap = new HashMap<>();
            //创建默认的CellStyle
            CellStyle commonCellStyle = book.createCellStyle();
            commonCellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyleMap.put(String.class, commonCellStyle);
            //循环写入excel
            for (String sheetName : sheetData.keySet()) {
                Sheet sheet = book.getSheet(sheetName);
                if (sheet == null) {
                    sheet = book.createSheet(sheetName);
                }
                //获取所有行数据
                List<List<ExcelColumn>> rows = sheetData.get(sheetName);
                int rowIndex = sheet.getLastRowNum() == 0 ? 0 : (sheet.getLastRowNum() + 1);
                for (List<ExcelColumn> columns : rows) {
                    //新增标题
                    if (rowIndex == 0) {
                        Row excelRow = sheet.createRow(rowIndex++);
                        //起始列标识
                        int cellIndex = (excelRow.getLastCellNum() < 0 ? 0 : excelRow.getLastCellNum());
                        for (ExcelColumn column : columns) {
                            //设置列宽
                            sheet.setColumnWidth(cellIndex, column.getWidth() * 20);
                            Cell cell = excelRow.createCell(cellIndex);
                            //设置样式
                            cell.setCellStyle(cellStyleMap.get(String.class));
                            //设置值
                            cell.setCellValue(column.getName());
                            cellIndex++;
                        }
                    }
                    //新增数据
                    Row excelRow = sheet.createRow(rowIndex++);
                    //起始列标识
                    int cellIndex = (excelRow.getLastCellNum() < 0 ? 0 : excelRow.getLastCellNum());
                    for (ExcelColumn column : columns) {
                        Cell cell = excelRow.createCell(cellIndex++);
                        if (column.getValue() == null) {
                            cell.setCellValue("");
                            continue;
                        }
                        CellStyle cellStyle = cellStyleMap.get(column.getType());
                        if (cellStyle == null) {
                            //创建cellStyle
                            cellStyle = book.createCellStyle();
                            if (Date.class.equals(column.getType())) {
                                cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                            }
                            cellStyleMap.put(column.getType(), cellStyle);
                        }
                        assert cellStyle != null;
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        //设置样式
                        cell.setCellStyle(cellStyle);
                        //设置值
                        if (Date.class.equals(column.getType())) {
                            cell.setCellValue((Date) column.getValue());
                        } else if (Double.class.equals(column.getType())) {
                            cell.setCellValue((Double) column.getValue());
                        } else if (Boolean.class.equals(column.getType())) {
                            cell.setCellValue((Boolean) column.getValue());
                        } else if (Integer.class.equals(column.getType())) {
                            cell.setCellValue((Integer) column.getValue());
                        } else {
                            cell.setCellValue(String.valueOf(column.getValue()));
                        }
                    }
                }
            }
            fos = new FileOutputStream(new File(filePath, fileName));
            book.write(fos);
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
            StreamUtil.closeStream(fis);
            StreamUtil.closeStream(book);
        }
    }

    /**
     * 导入excel
     * @param fileName 文件名
     * @param excelEnum excel导入类型
     * @param sheetName sheet名称
     * @param batchSize 每批次数量
     * @param callBack 回调函数
     * @return 处理返回的数据
     */
    @SuppressWarnings("unchecked")
    public <T, S> List<T> importExcel(String fileName, ExcelEnum excelEnum, String sheetName, Integer batchSize, CallBack<T, S> callBack) {
        Assert.isNotNull(fileName, "fileName");
        Assert.isNotNull(excelEnum, "excelEnum");
        Assert.isNotNull(callBack, "callBack");
        Workbook book = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath, fileName);
            Assert.state(file.exists(), "file not found!");
            fis = new FileInputStream(file);
            if (excelEnum.getClazz().equals(SXSSFWorkbook.class) || excelEnum.getClazz().equals(XSSFWorkbook.class)) {
                book = new XSSFWorkbook(fis);
            } else if (excelEnum.getClazz().equals(HSSFWorkbook.class)) {
                book = new HSSFWorkbook(fis);
            }
            Assert.state(book != null, "book获取失败");
            assert book != null;
            //创建sheet页集合
            List<Sheet> sheets = new ArrayList<>();
            if (StringUtil.isEmpty(sheetName)) {
                for (Sheet sheet : book) {
                    sheets.add(sheet);
                }
            } else {
                sheets.add(book.getSheet(sheetName));
            }
            //获取callback上的第一个泛型
            Class<S> clazz = (Class<S>) ClassUtil.getGenericTypes(callBack.getClass(), 1);
            List<S> params = new ArrayList<>();
            List<T> result = new ArrayList<>();
            for (Sheet sheet : sheets) {
                if (sheet == null || sheet.getLastRowNum() <= 0) {
                    continue;
                }
                List<String> titles = new ArrayList<>();
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        for (Cell cell : row) {
                            titles.add(cell.getStringCellValue());
                        }
                        continue;
                    }
                    JSONObject jsonObject = new JSONObject();
                    for (int i = 0; i < titles.size(); i++) {
                        jsonObject.put(titles.get(i), ExcelColumnConvert.convertCellValue(row.getCell(i)));
                    }
                    //add数据
                    params.add(ExcelColumnConvert.convertJsonToGeneric(jsonObject, clazz));
                    //批处理数据
                    if (params.size() >= batchSize) {
                        List<T> processResult = callBack.execute(sheet.getSheetName(), params);
                        if (!CollectionUtils.isEmpty(processResult)) {
                            result.addAll(processResult);
                        }
                        params.clear();
                    }
                }
                //批处理数据
                if (!CollectionUtils.isEmpty(params)) {
                    List<T> processResult = callBack.execute(sheet.getSheetName(), params);
                    if (!CollectionUtils.isEmpty(processResult)) {
                        result.addAll(processResult);
                    }
                    params.clear();
                }
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fis);
            StreamUtil.closeStream(book);
        }
    }

    /**
     * 回调函数
     */
    public interface CallBack<T, S> {
        List<T> execute(String sheetName, List<S> rows);
    }
}
