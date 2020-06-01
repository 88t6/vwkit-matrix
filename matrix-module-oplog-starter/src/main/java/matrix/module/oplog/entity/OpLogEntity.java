package matrix.module.oplog.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.based.serializer.DateTimeSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@Data
@Accessors(chain = true)
public class OpLogEntity implements Serializable {

    private String id;

    private String name;

    private String uri;

    private String request;

    private String response;

    private String userId;

    @JsonSerialize(using = DateTimeSerializer.class)
    private Date createTime;

}
