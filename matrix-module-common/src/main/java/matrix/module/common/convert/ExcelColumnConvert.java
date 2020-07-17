package matrix.module.common.convert;

import matrix.module.common.annotation.Excel;
import matrix.module.common.bean.ExcelColumn;
import matrix.module.common.helper.Assert;
import matrix.module.common.utils.StringUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
}
