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

    private JdbcParam master = new JdbcParam();

    private JdbcParam slave1 = new JdbcParam();

    private JdbcParam slave2 = new JdbcParam();

    private JdbcParam slave3 = new JdbcParam();

    private JdbcParam slave4 = new JdbcParam();

    private JdbcParam slave5 = new JdbcParam();

    private JdbcParam slave6 = new JdbcParam();

    private JdbcParam slave7 = new JdbcParam();

    private JdbcParam slave8 = new JdbcParam();

    private JdbcParam slave9 = new JdbcParam();

    private JdbcParam slave10 = new JdbcParam();

    @Data
    @Accessors(chain = true)
    public static class JdbcParam implements Serializable {

        private boolean enabled = false;

        private String url;

        private String username;

        private String password;
    }
}
