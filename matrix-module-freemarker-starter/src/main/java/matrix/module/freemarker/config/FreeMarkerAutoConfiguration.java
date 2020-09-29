package matrix.module.freemarker.config;

import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.helper.Assert;
import matrix.module.freemarker.properties.FreeMarkerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author wangcheng
 */
@Configuration
@EnableConfigurationProperties(FreeMarkerProperties.class)
@ConditionalOnProperty(value = {"freemarker.enabled"})
public class FreeMarkerAutoConfiguration {

    @Autowired
    private FreeMarkerProperties freeMarkerProperties;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        Integer updateDelay = freeMarkerProperties.getUpdateDelay();
        Assert.notNullTip(updateDelay, "freemarker.update-delay");
        String templatePath = freeMarkerProperties.getTemplatePath();
        Assert.notNullTip(templatePath, "freemarker.template-path");
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPaths(templatePath);
        configurer.setPreferFileSystemAccess(true);
        configurer.setDefaultEncoding(BaseCodeConstant.DEFAULT_CHARSET);
        Properties settings = new Properties();
        Map<String, String> map = new HashMap<>();
        map.put("classic_compatible", "true");
        map.put("default_encoding", BaseCodeConstant.DEFAULT_CHARSET);
        map.put("output_encoding", BaseCodeConstant.DEFAULT_CHARSET);
        map.put("locale", "zh_CN");
        map.put("date_format", "yyyy-MM-dd");
        map.put("time_format", "yyyy-MM-dd HH:mm:ss");
        map.put("number_format", "#");
        map.put("template_update_delay", String.valueOf(updateDelay));
        map.put("template_exception_handler", "rethrow");
        settings.putAll(map);
        configurer.setFreemarkerSettings(settings);
        return configurer;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        String prefix = freeMarkerProperties.getPrefix();
        String suffix = freeMarkerProperties.getSuffix();
        Assert.notNullTip(suffix, "freemarker.suffix");
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setRequestContextAttribute("context");
        resolver.setAllowRequestOverride(true);
        resolver.setAllowSessionOverride(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setExposeSessionAttributes(true);
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setCache(freeMarkerProperties.isCache());
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        return resolver;
    }
}
