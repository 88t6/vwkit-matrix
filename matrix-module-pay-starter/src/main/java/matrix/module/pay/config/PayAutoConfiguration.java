package matrix.module.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
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
import matrix.module.pay.controller.WepayController;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.service.impl.MatrixPayServiceImpl;
import matrix.module.pay.service.impl.MatrixRefundServiceImpl;
import matrix.module.pay.templates.AlipayTemplate;
import matrix.module.pay.templates.WepayTemplate;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

/**
 * @author wangcheng
 */
@Configuration
@EnableConfigurationProperties(PayProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ImportAutoConfiguration({MatrixPayServiceImpl.class,
        MatrixRefundServiceImpl.class,
        ForwardPayController.class,
        PayNotifyController.class})
@ConditionalOnProperty(value = {"pay.enabled"})
public class PayAutoConfiguration {

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
            Assert.notNullTip(payProperties.getNotifyDomain(), "pay.notify-domain");
            PayProperties.AlipayProperties alipay = payProperties.getAlipay();
            Assert.notNullTip(alipay.getAppId(), "pay.alipay.app-id");
            Assert.notNullTip(alipay.getSignType(), "pay.alipay.sign-type");
            Assert.notNullTip(alipay.getReturnUrl(), "pay.alipay.return-url");
            Assert.notNullTip(alipay.getPrivateKey(), "pay.alipay.private-key");
            Assert.notNullTip(alipay.getPublicKey(), "pay.alipay.public-key");
            String gatewayUrl = alipay.isTest() ? AlipayConstant.TEST_GATEWAY_URL : AlipayConstant.GATEWAY_URL;
            logger.info("Alipay Notify Pay Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + AlipayConstant.NOTIFY_URI);
            AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, alipay.getAppId(),
                    alipay.getPrivateKey(), AlipayConstant.FORMAT,
                    BaseCodeConstant.DEFAULT_CHARSET, alipay.getPublicKey(), alipay.getSignType());
            return new AlipayTemplate(alipayClient);
        }
    }

    @Configuration
    @ConditionalOnClass(WxPayService.class)
    @ImportAutoConfiguration(WepayController.class)
    @EnableConfigurationProperties(PayProperties.class)
    @AutoConfigureAfter(DatabaseAutoConfiguration.class)
    @ConditionalOnProperty(value = {"pay.wepay.enabled"})
    public static class WepayConfiguration {

        private static final Logger logger = LogManager.getLogger(WepayConfiguration.class);

        @Autowired
        private PayProperties payProperties;

        @Bean
        @Order(1)
        public WxPayConfig wxPayConfig() {
            WxPayConfig payConfig = new WxPayConfig();
            PayProperties.WepayProperties wepay = payProperties.getWepay();
            Assert.notNullTip(wepay.getAppId(), "pay.wepay.app-id");
            Assert.notNullTip(wepay.getMchId(), "pay.wepay.mch-id");
            Assert.notNullTip(wepay.getMchKey(), "pay.wepay.mch-key");
            Assert.notNullTip(wepay.getKeyPath(), "pay.wepay.key-path");
            Assert.notNullTip(wepay.getSecret(), "pay.wepay.secret");
            payConfig.setAppId(StringUtils.trimToNull(wepay.getAppId()));
            payConfig.setMchId(StringUtils.trimToNull(wepay.getMchId()));
            payConfig.setMchKey(StringUtils.trimToNull(wepay.getMchKey()));
            payConfig.setSubAppId(StringUtils.trimToNull(wepay.getSubAppId()));
            payConfig.setSubMchId(StringUtils.trimToNull(wepay.getSubMchId()));
            payConfig.setKeyPath(StringUtils.trimToNull(wepay.getKeyPath()));
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            return payConfig;
        }

        @Bean
        @Order(2)
        public WxPayService wxPayService(WxPayConfig wxPayConfig) {
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            return wxPayService;
        }

        @Bean
        @Order(3)
        public WxMpService wxMpService() {
            WxMpDefaultConfigImpl wpConfig = new WxMpDefaultConfigImpl();
            wpConfig.setAppId(StringUtils.trimToNull(payProperties.getWepay().getAppId()));
            wpConfig.setSecret(StringUtils.trimToNull(payProperties.getWepay().getSecret()));
            WxMpService wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(wpConfig);
            return wxMpService;
        }

        @Bean
        @Order(4)
        public WepayTemplate wepayTemplate() {
            Assert.notNullTip(payProperties.getNotifyDomain(), "pay.notify-domain");
            logger.info("Wepay Notify Pay Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_PAY_URL_PREFIX + WepayConstant.NOTIFY_URI);
            logger.info("Wepay Notify Refund Url:" + payProperties.getNotifyDomain() + PayConstant.NOTIFY_RETURN_URL_PREFIX + WepayConstant.NOTIFY_URI);
            return new WepayTemplate();
        }

    }

}
