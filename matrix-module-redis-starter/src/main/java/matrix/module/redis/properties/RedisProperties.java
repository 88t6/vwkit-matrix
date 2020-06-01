package matrix.module.redis.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangCheng
 */
@ConfigurationProperties(prefix = "redis")
@Data
@Accessors(chain = true)
public class RedisProperties {

    private boolean enabled = false;

    private Long defaultExpire;

    private Standalone standalone;

    private Sentinel sentinel;

    private Cluster cluster;

    @Data
    @Accessors(chain = true)
    public static class Standalone {

        private boolean enabled = false;

        private String host;

        private String port;

        private String password;

        private Integer database;
    }

    @Data
    @Accessors(chain = true)
    public static class Sentinel {

        private boolean enabled = false;

        private String master;

        private String nodes;

        private String password;

        private Integer database;
    }

    @Data
    @Accessors(chain = true)
    public static class Cluster {

        private boolean enabled = false;

        private String nodes;

        private String password;

        private Integer maxRedirects = 0;
    }

}
