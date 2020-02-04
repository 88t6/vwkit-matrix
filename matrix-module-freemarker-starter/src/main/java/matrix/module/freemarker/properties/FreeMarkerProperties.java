package matrix.module.freemarker.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@ConfigurationProperties(prefix = "freemarker")
@Data
@Accessors(chain = true)
public class FreeMarkerProperties implements Serializable {

    private boolean enabled = false;

    private boolean cache = true;

    private Integer updateDelay = 0;

    private String templatePath = "classpath:/templates/";

    private String prefix;

    private String suffix = ".ftl";
}
