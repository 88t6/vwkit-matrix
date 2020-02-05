package matrix.module.jdbc.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

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

    private JdbcParam db1 = new JdbcParam();

    private JdbcParam db1Slave = new JdbcParam();

    private JdbcParam db2 = new JdbcParam();

    private JdbcParam db3 = new JdbcParam();

    private JdbcParam db4 = new JdbcParam();

    private JdbcParam db5 = new JdbcParam();

    private JdbcParam db6 = new JdbcParam();

    private JdbcParam db7 = new JdbcParam();

    private JdbcParam db8 = new JdbcParam();

    private JdbcParam db9 = new JdbcParam();

    private JdbcParam db10 = new JdbcParam();

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

        private String basePackage;

        private String url;

        private String username;

        private String password;
    }
}
