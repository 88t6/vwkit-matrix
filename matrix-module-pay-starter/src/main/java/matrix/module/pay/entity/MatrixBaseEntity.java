package matrix.module.pay.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.based.serializer.DateTimeSerializer;
import matrix.module.jdbc.annotation.MatrixColumn;
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

    @MatrixColumn("UNIQUE_ID")
    private String uniqueId;

    @MatrixColumn("STATUS")
    private Integer status = Constant.DISABLE;

    @MatrixColumn("ORDER_BY")
    private Integer orderBy = 0;

    @JsonSerialize(using = DateTimeSerializer.class)
    @MatrixColumn("CREATE_AT")
    private Date createAt = new Date();

    @JsonSerialize(using = DateTimeSerializer.class)
    @MatrixColumn("UPDATE_AT")
    private Date updateAt = new Date();
}
