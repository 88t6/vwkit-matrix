package matrix.module.jdbc.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/4
 */
@ConfigurationProperties(prefix = "jdbc")
@Data
@Accessors(chain = true)
public class JdbcProperties implements Serializable {

    private boolean enabled = false;

    private String driverClass;

    private DruidParam druid;

    private JdbcParam master = new JdbcParam();

    private JdbcParam slave = new JdbcParam();

    private Map<String, JdbcParam> dbList = new HashMap<>();

    @Data
    @Accessors(chain = true)
    public static class DruidParam implements Serializable {

        private boolean enabled = false;

        private String contextPath;

        private String username;

        private String password;
    }

    @Data
    @Accessors(chain = true)
    public static class JdbcParam implements Serializable {

        private boolean enabled = false;

        private String basePackages;

        private String url;

        private String username;

        private String password;
    }
}
