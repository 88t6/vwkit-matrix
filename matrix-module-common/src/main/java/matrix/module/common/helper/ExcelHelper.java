package matrix.module.common.helper;

import com.alibaba.fastjson.JSONObject;
import com.monitorjbl.xlsx.StreamingReader;
import matrix.module.common.bean.ExcelColumn;
import matrix.module.common.convert.ExcelColumnConvert;
import matrix.module.common.enums.ExcelEnum;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.listener.ExportMultiSheetListener;
import matrix.module.common.listener.ExportSingleSheetListener;
import matrix.module.common.listener.ImportSingleSheetCallBack;
import matrix.module.common.utils.ClassUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.common.utils.StreamUtil;
import matrix.module.common.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

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

    private static final int ROW_ACCESS_WINDOW_SIZE = 200;

    /**
     * 文件路径
     */
    private final String filePath;

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
     * @param data      数据
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public <T> String exportSingleForBean(List<T> data, ExcelEnum excelEnum) {
        ExportMultiSheetListener<T> multiSheetListener = new ExportMultiSheetListener<T>() {
            @Override
            public LinkedHashMap<String, List<T>> getData(Integer count) {
                if (count == 0) {
                    LinkedHashMap<String, List<T>> result = new LinkedHashMap<>();
                    result.put("AutoCreate_0", data);
                    return result;
                }
                return null;
            }
        };
        return exportMultiForBean(multiSheetListener, excelEnum);
    }

    /**
     * 导出Bean单个sheet
     *
     * @param listener  数据监听器
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public <T> String exportSingleForBean(ExportSingleSheetListener<T> listener, ExcelEnum excelEnum) {
        ExportMultiSheetListener<T> multiSheetListener = new ExportMultiSheetListener<T>() {

            @Override
            public void beforeProcessData(Workbook workbook) {
                super.beforeProcessData(workbook);
                listener.beforeProcessData(workbook);
            }

            @Override
            public void processingData(ExcelColumn column, Cell cell) {
                super.processingData(column, cell);
                listener.processingData(column, cell);
            }

            @Override
            public void afterProcessData(Workbook workbook) {
                super.afterProcessData(workbook);
                listener.afterProcessData(workbook);
            }

            @Override
            public LinkedHashMap<String, List<T>> getData(Integer count) {
                LinkedHashMap<String, List<T>> result = new LinkedHashMap<>();
                result.put("AutoCreate_0", listener.getData(count));
                return result;
            }
        };
        return exportMultiForBean(multiSheetListener, excelEnum);
    }

    /**
     * 导出Map单个sheet
     *
     * @param data      数据
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public String exportSingleForMap(List<LinkedHashMap<String, Object>> data, ExcelEnum excelEnum) {
        ExportMultiSheetListener<LinkedHashMap<String, Object>> multiSheetListener = new ExportMultiSheetListener<LinkedHashMap<String, Object>>() {
            @Override
            public LinkedHashMap<String, List<LinkedHashMap<String, Object>>> getData(Integer count) {
                if (count == 0) {
                    LinkedHashMap<String, List<LinkedHashMap<String, Object>>> result = new LinkedHashMap<>();
                    result.put("AutoCreate_0", data);
                    return result;
                }
                return null;
            }
        };
        return exportMultiForMap(multiSheetListener, excelEnum);
    }

    /**
     * 导出Map单个sheet
     *
     * @param listener  数据监听器
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public String exportSingleForMap(ExportSingleSheetListener<LinkedHashMap<String, Object>> listener, ExcelEnum excelEnum) {
        ExportMultiSheetListener<LinkedHashMap<String, Object>> multiSheetListener = new ExportMultiSheetListener<LinkedHashMap<String, Object>>() {

            @Override
            public void beforeProcessData(Workbook workbook) {
                super.beforeProcessData(workbook);
                listener.beforeProcessData(workbook);
            }

            @Override
            public void processingData(ExcelColumn column, Cell cell) {
                super.processingData(column, cell);
                listener.processingData(column, cell);
            }

            @Override
            public void afterProcessData(Workbook workbook) {
                super.afterProcessData(workbook);
                listener.afterProcessData(workbook);
            }

            @Override
            public LinkedHashMap<String, List<LinkedHashMap<String, Object>>> getData(Integer count) {
                LinkedHashMap<String, List<LinkedHashMap<String, Object>>> result = new LinkedHashMap<>();
                result.put("AutoCreate_0", listener.getData(count));
                return result;
            }
        };
        return exportMultiForMap(multiSheetListener, excelEnum);
    }

    /**
     * 导出Bean多个sheet
     *
     * @param listener  数据监听器
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public <T> String exportMultiForBean(ExportMultiSheetListener<T> listener, ExcelEnum excelEnum) {
        return exportExcel(listener, excelEnum, true);
    }

    /**
     * 导出Map多个sheet
     *
     * @param listener  数据监听器
     * @param excelEnum excel导入类型
     * @return 文件名
     */
    public String exportMultiForMap(ExportMultiSheetListener<LinkedHashMap<String, Object>> listener, ExcelEnum excelEnum) {
        return exportExcel(listener, excelEnum, false);
    }


    /**
     * 导出excel(核心方法)
     *
     * @param listener  数据监听器
     * @param excelEnum excel导入类型
     * @param isBean    是否是实体（非实体为map）
     * @return 文件名
     */
    @SuppressWarnings({"unchecked"})
    private <T> String exportExcel(ExportMultiSheetListener<T> listener, ExcelEnum excelEnum, boolean isBean) {
        Assert.notNullTip(listener, "listener");
        Assert.notNullTip(excelEnum, "excelEnum");
        Workbook book = null;
        FileOutputStream fos = null;
        try {
            if (excelEnum.getClazz().equals(SXSSFWorkbook.class)) {
                book = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
            } else {
                book = (Workbook) excelEnum.getClazz().newInstance();
            }
            //创建列类型字典
            Map<Object, CellStyle> cellStyleMap = new HashMap<>();
            //创建标题字体
            Font font = book.createFont();
            font.setBold(true);
            //创建默认的CellStyle
            CellStyle commonCellStyle = book.createCellStyle();
            commonCellStyle.setAlignment(HorizontalAlignment.CENTER);
            commonCellStyle.setFont(font);
            cellStyleMap.put("common", commonCellStyle);
            // 数据sheet页
            Set<String> dataSheets = new HashSet<>();
            int count = 0;
            //调用处理数据前操作
            listener.beforeProcessData(book);
            //准备写入数据
            while (true) {
                Map<String, List<T>> originData = listener.getData(count++);
                //检查数据是否为空（为空停止获取数据）
                if (originData == null || originData.size() <= 0
                        || originData.values().stream().filter(CollectionUtils::isNotEmpty).count() <= 0) {
                    break;
                }
                //解析数据
                for (String sheetName : originData.keySet()) {
                    List<T> originRows = originData.get(sheetName);
                    if (CollectionUtils.isEmpty(originRows)) {
                        continue;
                    }
                    List<List<ExcelColumn>> rows;
                    if (isBean) {
                        rows = ExcelColumnConvert.convertForBean(originRows);
                    } else {
                        rows = ExcelColumnConvert.convertForMap((List<LinkedHashMap<String, Object>>) originRows);
                    }
                    //获取当前sheet
                    Sheet sheet = book.getSheet(sheetName);
                    if (sheet == null) {
                        sheet = book.createSheet(sheetName);
                    }
                    int rowIndex = sheet.getPhysicalNumberOfRows();
                    for (List<ExcelColumn> columns : rows) {
                        //新增标题
                        if (!dataSheets.contains(sheetName) && dataSheets.add(sheetName)) {
                            Row excelRow = sheet.createRow(rowIndex++);
                            //起始列标识
                            int cellIndex = (excelRow.getLastCellNum() < 0 ? 0 : excelRow.getLastCellNum());
                            for (ExcelColumn column : columns) {
                                //设置列宽
                                sheet.setColumnWidth(cellIndex, column.getWidth() * 20);
                                Cell cell = excelRow.createCell(cellIndex);
                                //设置样式
                                cell.setCellStyle(cellStyleMap.get("common"));
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
                            CellStyle cellStyle = cellStyleMap.get(column.getType());
                            if (cellStyle == null) {
                                //创建cellStyle
                                cellStyle = getExportDefaultCellStyle(column, book);
                                cellStyleMap.put(column.getType(), cellStyle);
                            }
                            //设置样式
                            cell.setCellStyle(cellStyle);
                            //如果为空直接放入为空字符串
                            if (column.getValue() == null) {
                                cell.setCellValue("");
                                continue;
                            }
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
                            //后置单元格处理（处理批注，个性化颜色等等功能）
                            listener.processingData(column, cell);
                        }
                    }
                }
            }
            //写入数据结束
            //调用处理数据后操作
            listener.afterProcessData(book);
            //随机文件名称
            String fileName = RandomUtil.getUUID() + excelEnum.getSuffix();
            fos = new FileOutputStream(new File(filePath, fileName));
            book.write(fos);
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            StreamUtil.closeStream(fos);
            StreamUtil.closeStream(book);
        }
    }


    /**
     * 导入excel
     *
     * @param fileName       文件名
     * @param excelEnum      excel类型
     * @param importCallBack 回调函数
     * @return 处理返回的数据
     */
    public <T, S> List<T> importExcel(String fileName, ExcelEnum excelEnum, ImportSingleSheetCallBack<T, S> importCallBack) {
        return this.importExcel(fileName, excelEnum, null, null, 2000, importCallBack);
    }

    /**
     * 导入excel
     *
     * @param fileName       文件名
     * @param excelEnum      excel类型
     * @param sheetName      sheet名称
     * @param batchSize      每批次数量
     * @param importCallBack 回调函数
     * @return 处理返回的数据
     */
    public <T, S> List<T> importExcel(String fileName, ExcelEnum excelEnum, String sheetName, Integer batchSize, ImportSingleSheetCallBack<T, S> importCallBack) {
        return this.importExcel(fileName, excelEnum, sheetName, null, batchSize, importCallBack);
    }

    /**
     * 导入excel
     *
     * @param fileName       文件名
     * @param excelEnum      excel类型
     * @param sheetName      sheet名称
     * @param titleRowIndex  标题行Index
     * @param batchSize      每批次数量
     * @param importCallBack 回调函数
     * @return 处理返回的数据
     */
    public <T, S> List<T> importExcel(String fileName, ExcelEnum excelEnum, String sheetName, int titleRowIndex, Integer batchSize, ImportSingleSheetCallBack<T, S> importCallBack) {
        Assert.notNullTip(sheetName, "sheetName");
        Map<String, Integer> sheetTitleRowIndexMap = new HashMap<String, Integer>() {{
            put(sheetName, titleRowIndex);
        }};
        return this.importExcel(fileName, excelEnum, sheetName, sheetTitleRowIndexMap, batchSize, importCallBack);
    }

    /**
     * 导入excel(核心方法)
     *
     * @param fileName              文件名
     * @param excelEnum             excel类型
     * @param sheetName             sheet名称
     * @param sheetTitleRowIndexMap 每个sheet页的标题行Index
     * @param batchSize             每批次数量
     * @param importCallBack        回调函数
     * @return 处理返回的数据
     */
    @SuppressWarnings("unchecked")
    private <T, S> List<T> importExcel(String fileName, ExcelEnum excelEnum, String sheetName,
                                       Map<String, Integer> sheetTitleRowIndexMap,
                                       Integer batchSize, ImportSingleSheetCallBack<T, S> importCallBack) {
        Assert.notNullTip(fileName, "fileName");
        Assert.notNullTip(importCallBack, "callBack");
        Workbook book = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath, fileName);
            Assert.state(file.exists(), "file not found!");
            if (excelEnum.getClazz().equals(HSSFWorkbook.class)) {
                fis = new FileInputStream(file);
                book = new HSSFWorkbook(fis);
            } else {
                book = StreamingReader.builder().rowCacheSize(ROW_ACCESS_WINDOW_SIZE).open(file);
            }
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
            Class<S> clazz = (Class<S>) ClassUtil.getGenericSuperClassTypes(importCallBack.getClass(), 1);
            List<S> params = new ArrayList<>();
            List<T> result = new ArrayList<>();
            for (Sheet sheet : sheets) {
                if (sheet == null) {
                    continue;
                }
                //标题行Index
                Integer titleRowIndex = sheetTitleRowIndexMap == null ? null : sheetTitleRowIndexMap.get(sheet.getSheetName());
                titleRowIndex = titleRowIndex == null ? 0 : titleRowIndex;
                List<String> titles = new ArrayList<>();
                for (Row row : sheet) {
                    if (row.getRowNum() <= titleRowIndex) {
                        if (row.getRowNum() == titleRowIndex) {
                            for (Cell cell : row) {
                                titles.add(cell.getStringCellValue());
                            }
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
                        List<T> processResult = importCallBack.processData(sheet.getSheetName(), params);
                        if (!CollectionUtils.isEmpty(processResult)) {
                            result.addAll(processResult);
                        }
                        params.clear();
                    }
                }
                //批处理数据
                if (!CollectionUtils.isEmpty(params)) {
                    List<T> processResult = importCallBack.processData(sheet.getSheetName(), params);
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
            StreamUtil.closeStream(book);
            StreamUtil.closeStream(fis);
        }
    }

    /**
     * 获取默认的单元格样式
     * @param column 单元格数据
     * @param workbook 工作簿
     * @return 单元格样式
     */
    public static CellStyle getExportDefaultCellStyle(ExcelColumn column, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (Date.class.equals(column.getType())) {
            cellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        }
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 获取批注信息
     * @param cell 单元格
     * @param author 作者
     * @param text 批注信息
     * @return 批注信息
     */
    public static Comment getExportDefaultComment(Cell cell, String author, String text) {
        Drawing<?> drawing = cell.getSheet().createDrawingPatriarch();
        Comment comment = drawing.createCellComment(drawing.createAnchor(0, 0, 0, 0, cell.getColumnIndex(), cell.getRowIndex(),
                cell.getColumnIndex() + 3, cell.getRowIndex() + 5));
        if (cell instanceof SXSSFCell || cell instanceof XSSFCell) {
            comment.setString(new XSSFRichTextString(text));
        } else if (cell instanceof HSSFCell){
            comment.setString(new HSSFRichTextString(text));
        }
        comment.setAuthor(author);
        return comment;
    }
}
