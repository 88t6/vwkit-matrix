package matrix.module.common.convert;

import com.alibaba.fastjson.JSONObject;
import matrix.module.common.annotation.Excel;
import matrix.module.common.bean.ExcelColumn;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * excel列转换器
 * @author wangcheng
 * 2020/7/17
 **/
public class ExcelColumnConvert {

    /**
     * bean类型转换
     * @param items 数据
     * @return 导出的excel数据
     */
    public static List<List<ExcelColumn>> convertForBean(List<?> items) {
        List<List<ExcelColumn>> result = new ArrayList<>();
        for (Object item : items) {
            //创建行
            List<ExcelColumn> row = new ArrayList<>();
            Field[] fields = item.getClass().getDeclaredFields();
            Assert.state(fields.length > 0, "field null");
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Excel annotation = field.getDeclaredAnnotation(Excel.class);
                    if (annotation == null) {
                        continue;
                    }
                    //创建列
                    ExcelColumn column = new ExcelColumn();
                    //设置列名
                    column.setName(StringUtil.isNotEmpty(annotation.value()) ? annotation.value() : field.getName());
                    //设置值
                    column.setValue(field.get(item));
                    //设置宽度
                    column.setWidth(annotation.width());
                    //设置类型
                    column.setType(field.getType());
                    row.add(column);
                } catch (Exception e) {
                    //无需操作
                }
            }
            result.add(row);
        }
        return result;
    }

    /**
     * map类型转换
     * @param items 数据
     * @return 导出的excel数据
     */
    public static List<List<ExcelColumn>> convertForMap(List<LinkedHashMap<String, Object>> items) {
        List<List<ExcelColumn>> result = new ArrayList<>();
        for (LinkedHashMap<String, Object> item : items) {
            //创建行
            List<ExcelColumn> row = new ArrayList<>();
            for (String name: item.keySet()) {
                //创建列
                ExcelColumn column = new ExcelColumn();
                //设置列名
                column.setName(name);
                //设置值
                column.setValue(item.get(name));
                //设置类型
                column.setType(item.get(name).getClass());
                row.add(column);
            }
            result.add(row);
        }
        return result;
    }

    /**
     * 转换为列值
     *
     * @param cell excel cell
     * @return cell value
     */
    public static Object convertCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String cellType = cell.getCellType().name();
        if (CellType._NONE.name().equals(cellType)) {
            return null;
        }
        if (CellType.NUMERIC.name().equals(cellType)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else {
                return cell.getNumericCellValue();
            }
        }
        if (CellType.STRING.name().equals(cellType)) {
            return cell.getStringCellValue();
        }
        if (CellType.FORMULA.name().equals(cellType)) {
            throw new ServiceException(
                    String.format("excel parse error not support formula %s by row:%d cell:%d",
                            cell.getCellFormula(), cell.getRowIndex() + 1, cell.getColumnIndex() + 1));
        }
        if (CellType.BLANK.name().equals(cellType)) {
            return "";
        }
        if (CellType.BOOLEAN.name().equals(cellType)) {
            return cell.getBooleanCellValue();
        }
        if (CellType.ERROR.name().equals(cellType)) {
            throw new ServiceException("excel import error code: " + cell.getErrorCellValue());
        }
        return cell.getStringCellValue();
    }

    /**
     * 将json转换为泛型的值
     *
     * @param jsonObject json对象
     * @param clazz      泛型
     * @return new 泛型对象
     */
    public static <T> T convertJsonToGeneric(JSONObject jsonObject, Class<T> clazz) {
        try {
            if (Map.class.isAssignableFrom(clazz)) {
                return JSONObject.parseObject(jsonObject.toJSONString(), clazz);
            } else {
                //实体模式
                T t = clazz.newInstance();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Excel annotation = field.getDeclaredAnnotation(Excel.class);
                    if (annotation == null) {
                        continue;
                    }
                    try {
                        String key = StringUtil.isNotEmpty(annotation.value()) ? annotation.value() : field.getName();
                        if (Date.class.equals(field.getType())) {
                            field.set(t, jsonObject.getDate(key));
                        } else if (Double.class.equals(field.getType())) {
                            field.set(t, jsonObject.getDoubleValue(key));
                        } else if (Boolean.class.equals(field.getType())) {
                            field.set(t, jsonObject.getBooleanValue(key));
                        } else if (Integer.class.equals(field.getType())) {
                            field.set(t, jsonObject.getInteger(key));
                        } else {
                            field.set(t, jsonObject.getString(key));
                        }
                    } catch (Exception e) {
                        //无需操作
                    }
                }
                return t;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
