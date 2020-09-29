package matrix.module.oplog.config;

import matrix.module.based.utils.JacksonUtil;
import matrix.module.based.utils.WebUtil;
import matrix.module.common.exception.GlobalControllerException;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.utils.RandomUtil;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import matrix.module.oplog.annotation.OpLog;
import matrix.module.oplog.entity.OpLogEntity;
import matrix.module.oplog.properties.OpLogProperties;
import matrix.module.oplog.service.MatrixOpLogService;
import matrix.module.oplog.service.impl.MatrixOpLogServiceImpl;
import matrix.module.oplog.utils.MatrixUserUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@Configuration
@EnableConfigurationProperties(OpLogProperties.class)
@AutoConfigureAfter({DatabaseAutoConfiguration.class})
@ConditionalOnProperty(value = {"jdbc.enabled", "op-log.enabled"})
public class OpLogAutoConfiguration {

    @Autowired
    private OpLogProperties opLogProperties;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (jdbcTemplate == null) {
            throw new ServiceException("jdbcTemplate not found!");
        }
        DynamicDataSourceHolder.setDataSource(opLogProperties.getDb() + "DB");
        // 创建matrix_oplog表
        try {
            jdbcTemplate.execute("select 1 from matrix_oplog");
        } catch (BadSqlGrammarException e) {
            //新建数据表
            String createTableSql = "CREATE TABLE matrix_oplog (" +
                    "  `ID` VARCHAR(255) NOT NULL COMMENT '主键ID', " +
                    "  `NAME` VARCHAR(255) COMMENT '动作名称', " +
                    "  `URI` TEXT NOT NULL COMMENT 'URI', " +
                    "  `REQUEST` TEXT COMMENT '请求参数', " +
                    "  `RESPONSE` TEXT COMMENT '返回参数', " +
                    "  `USER_ID` VARCHAR(255) COMMENT '用户ID', " +
                    "  `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间', " +
                    "  PRIMARY KEY (`ID`)" +
                    ")";
            jdbcTemplate.execute(createTableSql);
        }
    }

    @Configuration
    @ImportAutoConfiguration({MatrixOpLogServiceImpl.class})
    @Aspect
    @Order
    public static class OpLogAspectAutoConfiguration {

        private static final Logger logger = LogManager.getLogger(OpLogAspectAutoConfiguration.class);

        @Autowired(required = false)
        private MatrixOpLogService matrixOpLogService;

        @Around("@within(matrix.module.oplog.annotation.OpLog) " +
                "|| @annotation(matrix.module.oplog.annotation.OpLog)")
        public Object opLogAround(ProceedingJoinPoint joinPoint) {
            try {
                OpLog opLog = joinPoint.getTarget().getClass().getAnnotation(OpLog.class);
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                if (method.getAnnotation(OpLog.class) != null) {
                    opLog = method.getAnnotation(OpLog.class);
                }
                if (opLog == null) {
                    opLog = joinPoint.getTarget().getClass().getInterfaces()[0].getAnnotation(OpLog.class);
                }
                // 获取userId
                String userId = MatrixUserUtil.getUserId();
                if (StringUtils.isEmpty(userId)) {
                    logger.warn("userId not found please use matrix.module.oplog.utils.MatrixUserUtil.setUserId(String userId) to ThreadLocal");
                }
                OpLogEntity opLogEntity = new OpLogEntity()
                        .setId(RandomUtil.getUUID())
                        .setName(opLog.value())
                        .setUri(WebUtil.getRequest().getRequestURI())
                        .setUserId(userId)
                        .setCreateTime(new Date());
                //获取请求参数
                Object[] args = joinPoint.getArgs();
                if (args != null && args.length > 0) {
                    List<String> request = new ArrayList<>();
                    for (Object arg : args) {
                        if (arg instanceof Serializable) {
                            request.add(arg instanceof String ? arg.toString() : JacksonUtil.toJsonString(arg));
                        }
                    }
                    opLogEntity.setRequest(request.size() == 1 ? request.get(0) : JacksonUtil.toJsonString(request));
                }
                Object result = joinPoint.proceed(joinPoint.getArgs());
                if (result instanceof Serializable) {
                    opLogEntity.setResponse(JacksonUtil.toJsonString(result));
                }
                matrixOpLogService.saveOpLog(opLogEntity);
                return result;
            } catch (Throwable e) {
                throw new ServiceException(e);
            }
        }
    }
}
