package matrix.module.based;

import matrix.module.common.helper.Assert;
import matrix.module.common.utils.PropertiesUtil;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;

/**
 * @author wangcheng
 */
@SpringBootApplication
public class MatrixApplication extends SpringApplication {

    protected static Properties properties;

    static {
        properties = PropertiesUtil.get(MatrixApplication.class, "/default.properties");
        /**
         * 开启热部署
         */
        properties.put("spring.devtools.restart.enabled", false);
        /**
         * 关掉默认favicon
         */
        properties.put("spring.mvc.favicon.enabled", false);
        /**
         * close autoconfigure
         */
        StringBuilder classes = new StringBuilder();
        classes.append("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,");
        classes.append("org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,");
        classes.append("org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,");
        classes.append("org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,");
        classes.append("org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration");
        properties.put("spring.autoconfigure.exclude", classes.toString());
    }

    public MatrixApplication() {
        super(MatrixApplication.class);
        this.setDefaultProperties(MatrixApplication.properties);
        this.setBannerMode(Mode.OFF);
    }

    public MatrixApplication(Class<?>... primarySources) {
        super(MatrixApplication.getClassList(primarySources));
        this.setDefaultProperties(MatrixApplication.properties);
        this.setBannerMode(Mode.OFF);
    }

    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        return new MatrixApplication(primarySource).run(args);
    }

    public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
        return new MatrixApplication(primarySources).run(args);
    }

    public static void main(String[] args) throws Exception {
        MatrixApplication.run(new Class<?>[0], args);
    }

    protected static Class<?>[] getClassList(Class<?>[] classes) {
        Assert.isNotNull(classes, "Class不允许为空");
        Class<?>[] classList = new Class<?>[1 + classes.length];
        classList[0] = MatrixApplication.class;
        for (int i = 1; i <= classes.length; i++) {
            classList[i] = classes[i - 1];
        }
        return classList;
    }

    public ConfigurableApplicationContext run(String... args) {
        return super.run(args);
    }
}
