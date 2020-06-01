package matrix.module.jpa.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangCheng
 */
@ConfigurationProperties(prefix = "jpa")
@Data
@Accessors(chain = true)
public class JpaProperties {

    private boolean enabled = false;

    private String dialect;

    private boolean showSql = false;

    private String basePackage;
}
