package matrix.module.pay.config;

import matrix.module.common.helper.Assert;
import matrix.module.jdbc.config.DatabaseAutoConfiguration;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.constants.WepayConstant;
import matrix.module.pay.controller.ForwardPayController;
import matrix.module.pay.controller.PayNotifyController;
import matrix.module.pay.properties.PayProperties;
import matrix.module.pay.sdk.WepayClient;
import matrix.module.pay.service.impl.MatrixPayServiceImpl;
import matrix.module.pay.service.impl.MatrixRefundServiceImpl;
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
@ConditionalOnProperty(value = "{pay.wepay.enabled}")
public class WepayConfiguration {

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
