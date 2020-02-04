package matrix.module.freemarker.config;

import matrix.module.common.helper.Assert;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author wangcheng
 */
@Configuration
@ConditionalOnProperty(value = {"view.freemarker.enabled"}, matchIfMissing = false)
public class FreeMarkerAutoConfiguration implements EnvironmentAware {

    private Environment env;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        String updateDelay = env.getProperty("view.freemarker.update-delay");
        String templatePath = env.getProperty("view.freemarker.template-path");
        Assert.isNotNull(updateDelay, "view.freemarker.update-delay");
        Assert.isNotNull(templatePath, "view.freemarker.template-path");
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPaths(new String[]{templatePath});
        configurer.setPreferFileSystemAccess(true);
        configurer.setDefaultEncoding("UTF-8");
        Properties settings = new Properties();
        Map<String, String> map = new HashMap<String, String>();
        map.put("classic_compatible", "true");
        map.put("default_encoding", "UTF-8");
        map.put("output_encoding", "UTF-8");
        map.put("locale", "zh_CN");
        map.put("date_format", "yyyy-MM-dd");
        map.put("time_format", "yyyy-MM-dd HH:mm:ss");
        map.put("number_format", "#");
        map.put("template_update_delay", updateDelay);
        map.put("template_exception_handler", "rethrow");
        settings.putAll(map);
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        String cache = env.getProperty("view.freemarker.cache");
        String prefix = env.getProperty("view.freemarker.prefix");
        String suffix = env.getProperty("view.freemarker.suffix");
        Assert.isNotNull(cache, "view.freemarker.cache");
        Assert.isNotNull(suffix, "view.freemarker.suffix");
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setRequestContextAttribute("context");
        resolver.setAllowRequestOverride(true);
        resolver.setAllowSessionOverride(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setCache("true".equals(cache));
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        return resolver;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
