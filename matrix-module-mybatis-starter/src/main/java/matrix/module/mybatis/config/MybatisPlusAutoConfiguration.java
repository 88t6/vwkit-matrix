package matrix.module.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.mybatis.properties.MybatisProperties;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
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
 * @date 2020/2/5
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ConditionalOnProperty(value = {"mybatis.enabled"})
@MapperScan(basePackages = "${mybatis.db1.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db1SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db2.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db2SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db3.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db3SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db4.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db4SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db5.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db5SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db6.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db6SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db7.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db7SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db8.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db8SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db9.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db9SqlSessionFactory")
@MapperScan(basePackages = "${mybatis.db10.mapper-package:E8316547E656EF451921A497D6FE665D}", sqlSessionFactoryRef = "db10SqlSessionFactory")
public class MybatisPlusAutoConfiguration {

    private static Logger logger = LogManager.getLogger(MybatisPlusAutoConfiguration.class);

    @Autowired
    private MybatisProperties properties;

    @Resource
    private DataSource dataSource;

    @Primary
    @Bean("db1SqlSessionFactory")
    @Qualifier("db1SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db1.enabled"})
    public SqlSessionFactory db1SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb1();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db2SqlSessionFactory")
    @Qualifier("db2SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db2.enabled"})
    public SqlSessionFactory db2SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb2();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db3SqlSessionFactory")
    @Qualifier("db3SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db3.enabled"})
    public SqlSessionFactory db3SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb3();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db4SqlSessionFactory")
    @Qualifier("db4SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db4.enabled"})
    public SqlSessionFactory db4SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb4();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db5SqlSessionFactory")
    @Qualifier("db5SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db5.enabled"})
    public SqlSessionFactory db5SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb5();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db6SqlSessionFactory")
    @Qualifier("db6SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db6.enabled"})
    public SqlSessionFactory db6SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb6();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db7SqlSessionFactory")
    @Qualifier("db7SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db7.enabled"})
    public SqlSessionFactory db7SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb7();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db8SqlSessionFactory")
    @Qualifier("db8SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db8.enabled"})
    public SqlSessionFactory db8SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb8();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db9SqlSessionFactory")
    @Qualifier("db9SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db9.enabled"})
    public SqlSessionFactory db9SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb9();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    @Bean("db10SqlSessionFactory")
    @Qualifier("db10SqlSessionFactory")
    @ConditionalOnProperty(value = {"mybatis.db10.enabled"})
    public SqlSessionFactory db10SqlSessionFactory() throws Exception {
        MybatisProperties.Params params = properties.getDb10();
        return this.getSqlSessionFactory(dataSource, params.getTypeAliasesPackage(), params.getMapperLocations());
    }

    private SqlSessionFactory getSqlSessionFactory(DataSource dataSource, String typeAliasesPackage,
                                                   String mapperLocations) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactory.setPlugins(paginationInterceptor());
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(new GlobalConfig.DbConfig()
                .setIdType(IdType.INPUT));
        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
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
