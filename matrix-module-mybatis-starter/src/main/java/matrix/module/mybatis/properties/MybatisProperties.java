package matrix.module.mybatis.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangCheng
 */
@ConfigurationProperties(prefix = "mybatis")
@Data
@Accessors(chain = true)
public class MybatisProperties {

    private boolean enabled = false;

    private ExecutorType executorType;

    // 实体路径
    public String typeAliasesPackage;

    //xml文件路径
    public String mapperLocations;
}
