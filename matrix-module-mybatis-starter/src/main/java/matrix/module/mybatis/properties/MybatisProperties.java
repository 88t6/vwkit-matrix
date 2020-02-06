package matrix.module.mybatis.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

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

    private Params db1;
    private Params db2;
    private Params db3;
    private Params db4;
    private Params db5;
    private Params db6;
    private Params db7;
    private Params db8;
    private Params db9;
    private Params db10;

    @Data
    @Accessors(chain = true)
    public static class Params implements Serializable {
        // Mapper Java文件路径
        public String mapperPackage;
        // 实体路径
        public String typeAliasesPackage;
        //xml文件路径
        public String mapperLocations;
        private boolean enabled = false;
    }
}
