package matrix.module.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.helper.Assert;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.pay.constants.AlipayConstant;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.controller.ForwardPayController;
import matrix.module.pay.controller.PayNotifyController;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.service.impl.MatrixPayServiceImpl;
import matrix.module.pay.service.impl.MatrixRefundServiceImpl;
import matrix.module.pay.templates.AlipayTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Configuration
@EnableConfigurationProperties(PayProperties.class)
@AutoConfigureAfter(DatabaseAutoConfiguration.class)
@ImportAutoConfiguration({MatrixPayServiceImpl.class,
        MatrixRefundServiceImpl.class,
        ForwardPayController.class,
        PayNotifyController.class})
@ConditionalOnProperty(value = "{pay.alipay.enabled}")
public class AlipayConfiguration {

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
