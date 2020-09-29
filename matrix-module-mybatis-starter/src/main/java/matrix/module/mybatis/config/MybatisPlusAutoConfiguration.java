package matrix.module.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import matrix.module.common.helper.Assert;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.mybatis.properties.MybatisProperties;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author WangCheng
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ConditionalOnProperty(value = {"mybatis.enabled"})
public class MybatisPlusAutoConfiguration {

    private static Logger logger = LogManager.getLogger(MybatisPlusAutoConfiguration.class);

    @Autowired
    private MybatisProperties properties;

    @Resource
    private DataSource dataSource;

    @Bean("sqlSessionFactory")
    @Qualifier("sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return getSqlSessionFactory(dataSource, properties.getTypeAliasesPackage(), properties.getMapperLocations());
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    private SqlSessionFactory getSqlSessionFactory(DataSource dataSource, String typeAliasesPackage, String mapperLocations) throws Exception {
        Assert.notNullTip(typeAliasesPackage, "mybatis.type-aliases-package");
        Assert.notNullTip(mapperLocations, "mybatis.mapper-locations");
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactory.setPlugins(new PaginationInterceptor());
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        globalConfig.setDbConfig(new GlobalConfig.DbConfig()
                .setIdType(IdType.INPUT));
        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations));
        return sqlSessionFactory.getObject();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
                return;
            }
            logger.debug("Searching for mappers annotated with @Mapper");
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug("Using auto-configuration base package '{}'", pkg));
            }
            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }
            scanner.setAnnotationClass(Mapper.class);
            scanner.registerFilters();
            scanner.doScan(StringUtils.toStringArray(packages));
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    @Configuration
    @Import({AutoConfiguredMapperScannerRegistrar.class})
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            logger.debug("No {} found.", MapperFactoryBean.class.getName());
        }
    }
}
