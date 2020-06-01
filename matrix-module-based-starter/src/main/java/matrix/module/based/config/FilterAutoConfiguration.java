package matrix.module.based.config;

import matrix.module.based.filter.CommonFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@Configuration
public class FilterAutoConfiguration {

    @Bean
    public FilterRegistrationBean<CommonFilter> registerCommonFilter() {
        FilterRegistrationBean<CommonFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CommonFilter());
        registration.setOrder(0);
        registration.addUrlPatterns("/*");
        registration.setName("commonFilter");
        Map<String, String> params = new HashMap<>();
        params.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico");
        registration.setInitParameters(params);
        registration.setOrder(1);
        return registration;
    }
}
