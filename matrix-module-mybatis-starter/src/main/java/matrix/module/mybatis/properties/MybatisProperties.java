package matrix.module.mybatis.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@ConfigurationProperties(prefix = "mybatis")
@Data
@Accessors(chain = true)
public class MybatisProperties {

    private boolean enabled = false;

    private ExecutorType executorType;

    private Params master = new Params();

    private Params slave = new Params();

    private Map<String, Params> dbList = new HashMap<>();

    @Data
    @Accessors(chain = true)
    public static class Params implements Serializable {

        private boolean enabled = false;

        // 实体路径
        public String typeAliasesPackage;

        //xml文件路径
        public String mapperLocations;
    }
}
