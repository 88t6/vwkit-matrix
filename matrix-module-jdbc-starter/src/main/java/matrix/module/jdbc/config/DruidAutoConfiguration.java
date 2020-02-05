package matrix.module.jdbc.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import matrix.module.jdbc.properties.JdbcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
@Configuration
@EnableConfigurationProperties(JdbcProperties.class)
@ConditionalOnProperty(value = {"jdbc.enabled", "jdbc.druid.enabled"})
public class DruidAutoConfiguration {

    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean
    public ServletRegistrationBean<StatViewServlet> registerDruidServlet() {
        JdbcProperties.DruidParam druidParam = jdbcProperties.getDruid();
        ServletRegistrationBean<StatViewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new StatViewServlet());
        registration.setName("DruidStatView");
        registration.setEnabled(true);
        Map<String, String> params = new HashMap<>();
        params.put("loginUsername", druidParam.getUsername());
        params.put("loginPassword", druidParam.getPassword());
        registration.setInitParameters(params);
        registration.setUrlMappings(Collections.singletonList(druidParam.getContextPath()));
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> registerDruidFilter() {
        JdbcProperties.DruidParam druidParam = jdbcProperties.getDruid();
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new WebStatFilter());
        registration.addUrlPatterns("/*");
        registration.setName("druidFilter");
        Map<String, String> params = new HashMap<>();
        params.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico," + druidParam.getContextPath());
        registration.setInitParameters(params);
        registration.setOrder(1);
        return registration;
    }
}
