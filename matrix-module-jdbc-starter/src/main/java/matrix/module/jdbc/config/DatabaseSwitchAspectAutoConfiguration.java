package matrix.module.jdbc.config;

import matrix.module.jdbc.properties.JdbcProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled"})
@Aspect
public class DatabaseSwitchAspectAutoConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private JdbcProperties jdbcProperties;

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Service)")
    public void service(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Component)")
    public void component(JoinPoint point) {
        process(point);
    }

    @Before("@annotation(org.springframework.stereotype.Repository)")
    public void repository(JoinPoint point) {
        process(point);
    }

    private void process(JoinPoint point) {
        Object target = point.getTarget();
        System.out.println(target.getClass().getName());
    }
}
