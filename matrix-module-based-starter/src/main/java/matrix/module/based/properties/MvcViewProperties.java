package matrix.module.based.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@ConfigurationProperties(prefix = "mvc")
@Data
@Accessors(chain = true)
public class MvcViewProperties implements Serializable {

    private boolean enabled = false;

    private String staticPath;

    private String index;

}
