package matrix.module.oplog.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@ConfigurationProperties(prefix = "op-log")
@Data
@Accessors(chain = true)
public class OpLogProperties {

    private boolean enabled = false;

    private String db = "master";
}
