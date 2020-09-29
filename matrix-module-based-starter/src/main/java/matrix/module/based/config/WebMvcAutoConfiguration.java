package matrix.module.based.config;

import matrix.module.based.properties.MvcViewProperties;
import matrix.module.common.helper.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangcheng
 */
@Configuration
@EnableConfigurationProperties(MvcViewProperties.class)
@ConditionalOnProperty(value = {"mvc.enabled"})
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private MvcViewProperties mvcViewProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticPath = mvcViewProperties.getStaticPath();
        Assert.notNullTip(staticPath, "mvc.static-path");
        registry.addResourceHandler("/static/**").addResourceLocations(staticPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String index = mvcViewProperties.getIndex();
        Assert.notNullTip(index, "mvc.index");
        registry.addViewController("/").setViewName("forward:" + index);
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
