package matrix.module.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wangcheng
 * 2020/7/17
 **/
@Data
@Accessors(chain = true)
public class ExcelColumn implements Serializable {

    /**
     * 单元格名称
     */
    private String name;

    /**
     * 单元格中的值
     */
    private Object value;

    /**
     * 单元格宽度
     */
    private int width = 200;

    /**
     * 单元格类型
     */
    private Class<?> type = null;

}
