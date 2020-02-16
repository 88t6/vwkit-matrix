package matrix.module.jdbc.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/14
 */
@ConfigurationProperties(prefix = "flyway-db")
@Data
@Accessors(chain = true)
public class FlywayDbProperties implements Serializable {

    //是否启用
    private boolean enabled = false;

    //脚本的文件名前缀。 默认值： V 。
    private String sqlMigrationPrefix = "V";

    //脚本的分割符 默认双下划线
    private String sqlMigrationSeparator = "__";

    //脚本的后缀 默认 .sql
    private String sqlMigrationSuffix = ".sql";

    //Flyway使用的Schema元数据表名称 flyway_version
    private String table = "flyway_version";

    //在运行迁移时是否要自动验证。 默认值： true
    private boolean validateOnMigrate = true;

    //是否允许乱序
    private boolean outOfOrder = false;
}
