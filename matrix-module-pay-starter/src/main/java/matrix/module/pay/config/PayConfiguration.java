package matrix.module.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.jdbc.utils.DynamicDataSourceHolder;
import matrix.module.pay.constants.AlipayConstant;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.constants.WepayConstant;
import matrix.module.pay.controller.ForwardPayController;
import matrix.module.pay.controller.PayNotifyController;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.sdk.WepayClient;
import matrix.module.pay.service.impl.MatrixPayServiceImpl;
import matrix.module.pay.service.impl.MatrixRefundServiceImpl;
import matrix.module.pay.templates.AlipayTemplate;
import matrix.module.pay.templates.WepayTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

/**
 * @author wangcheng
 * @date 2020-02-27
 */
@Configuration
@EnableConfigurationProperties(PayProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ImportAutoConfiguration({MatrixPayServiceImpl.class,
        MatrixRefundServiceImpl.class,
        ForwardPayController.class,
        PayNotifyController.class})
@ConditionalOnProperty(value = {"pay.enabled"})
public class PayConfiguration {

    @Autowired
    private PayProperties payProperties;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        if (jdbcTemplate == null) {
            throw new ServiceException("jdbcTemplate not found!");
        }
        DynamicDataSourceHolder.setDataSource(payProperties.getDb() + "DB");
        // 创建matrix_pay表
        try {
            jdbcTemplate.execute("select 1 from matrix_pay");
        } catch (BadSqlGrammarException e) {
            //新建数据表
            String createTableSql = "CREATE TABLE matrix_pay (" +
                    "  `PAY_ID` VARCHAR(255) NOT NULL COMMENT '支付ID', " +
                    "  `ORDER_ID` VARCHAR(255) NOT NULL COMMENT '订单号', " +
                    "  `PRICE` DECIMAL(20, 2) NOT NULL COMMENT '支付金额', " +
                    "  `BODY` TEXT NOT NULL COMMENT '支付请求体', " +
                    "  `URL` VARCHAR(255) NOT NULL COMMENT '支付跳转url', " +
                    "  `ACTUAL_PRICE` DECIMAL(20, 2) COMMENT '实际支付金额', " +
                    "  `OUT_TRADE_NO` VARCHAR(255) COMMENT '第三方订单号', " +
                    "  `PAY_MODE` VARCHAR(20) NOT NULL COMMENT '支付方式', " +
                    "  `PAY_CHANNEL` VARCHAR(20) NOT NULL COMMENT '支付渠道', " +
                    "  `ORDER_BY` INT(10) NOT NULL DEFAULT 0 COMMENT '排序字段', " +
                    "  `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间', " +
                    "  `UPDATE_TIME` DATETIME NOT NULL COMMENT '更新时间', " +
                    "  `STATUS` INT(1) NOT NULL DEFAULT 0 COMMENT '状态(0:禁用,1:启用,-1:删除)', " +
                    "  PRIMARY KEY (`PAY_ID`)" +
                    ")";
            jdbcTemplate.execute(createTableSql);
        }
        // 创建matrix_refund表
        try {
            jdbcTemplate.execute("select 1 from matrix_refund");
        } catch (BadSqlGrammarException e) {
            //新建数据表
            String createTableSql = "CREATE TABLE matrix_refund (" +
                    "  `REFUND_ID` VARCHAR(255) NOT NULL COMMENT '退款ID', " +
                    "  `PAY_ID` VARCHAR(255) NOT NULL COMMENT '支付ID', " +
                    "  `ORDER_ID` VARCHAR(255) NOT NULL COMMENT '订单号', " +
                    "  `REFUND_PRICE` DECIMAL(20, 2) NOT NULL COMMENT '退款金额', " +
                    "  `ORDER_BY` INT(10) NOT NULL DEFAULT 0 COMMENT '排序字段', " +
                    "  `CREATE_TIME` DATETIME NOT NULL COMMENT '创建时间', " +
                    "  `UPDATE_TIME` DATETIME NOT NULL COMMENT '更新时间', " +
                    "  `STATUS` INT(1) NOT NULL DEFAULT 0 COMMENT '状态(0:禁用,1:启用,-1:删除)', " +
                    "  PRIMARY KEY (`REFUND_ID`)" +
                    ")";
            jdbcTemplate.execute(createTableSql);
        }
        DynamicDataSourceHolder.clearDataSource();
    }

    @Configuration
    @EnableConfigurationProperties(PayProperties.class)
    @AutoConfigureAfter(DatabaseAutoConfiguration.class)
    @ConditionalOnProperty(value = {"pay.alipay.enabled"})
    public static class AlipayConfiguration {
        private static final Logger logger = LogManager.getLogger(AlipayConfiguration.class);

        @Autowired
        private PayProperties payProperties;

        @Bean
        public AlipayTemplate alipayTemplate() {
            Assert.isNotNull(payProperties.getNotifyDomain(), "pay.notify-domain");
            PayProperties.AlipayProperties alipay = payProperties.getAlipay();
            Assert.isNotNull(alipay.getAppId(), "pay.alipay.app-id");
            Assert.isNotNull(alipay.getSignType(), "pay.alipay.sign-type");
            Assert.isNotNull(alipay.getReturnUrl(), "pay.alipay.return-url");
            Assert.isNotNull(alipay.getPrivateKey(), "pay.alipay.private-key");
            Assert.isNotNull(alipay.getPublicKey(), "pay.alipay.public-key");
            String gatewayUrl = alipay.isTest() ? AlipayConstant.TEST_GATEWAY_URL : AlipayConstant.GATEWAY_URL;
            logger.info("Alipay Notify Pay Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + AlipayConstant.NOTIFY_URI);
            AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, alipay.getAppId(),
                    alipay.getPrivateKey(), AlipayConstant.FORMAT,
                    BaseCodeConstant.DEFAULT_CHARSET, alipay.getPublicKey(), alipay.getSignType());
            return new AlipayTemplate(alipayClient);
        }
    }

    @Configuration
    @EnableConfigurationProperties(PayProperties.class)
    @AutoConfigureAfter(DatabaseAutoConfiguration.class)
    @ConditionalOnProperty(value = {"pay.wepay.enabled"})
    public static class WepayConfiguration {

        private static final Logger logger = LogManager.getLogger(WepayConfiguration.class);

        @Autowired
        private PayProperties payProperties;

        @Bean
        public WepayTemplate wepayTemplate() {
            Assert.isNotNull(payProperties.getNotifyDomain(), "pay.notify-domain");
            PayProperties.WepayProperties wepay = payProperties.getWepay();
            Assert.isNotNull(wepay.getAppId(), "pay.wepay.app-id");
            Assert.isNotNull(wepay.getMchId(), "pay.wepay.mch-id");
            Assert.isNotNull(wepay.getKey(), "pay.wepay.key");
            Assert.isNotNull(wepay.getCertPassword(), "pay.wepay.cert-password");
            Assert.isNotNull(wepay.getCertPath(), "pay.wepay.cert-path");
            logger.info("Wepay Notify Pay Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + WepayConstant.NOTIFY_URI);
            logger.info("Wepay Notify Refund Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_RETURN_URL_PREFIX + WepayConstant.NOTIFY_URI);
            WepayClient wepayClient = new WepayClient(wepay.getAppId(), wepay.getMchId(), wepay.getKey(), wepay.getCertPassword(), wepay.getCertPath());
            return new WepayTemplate(wepayClient);
        }

    }

}
