package matrix.module.pay.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.based.serializer.DateTimeSerializer;
import matrix.module.pay.constants.Constant;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Data
@Accessors(chain = true)
public class MatrixBaseEntity implements Serializable {

    private Integer status = Constant.DISABLE;

    private Integer orderBy = 0;

    @JsonSerialize(using = DateTimeSerializer.class)
    private Date createTime = new Date();

    @JsonSerialize(using = DateTimeSerializer.class)
    private Date updateTime = new Date();
}
