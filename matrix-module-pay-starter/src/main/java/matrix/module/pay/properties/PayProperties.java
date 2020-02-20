package matrix.module.pay.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@ConfigurationProperties(prefix = "pay")
@Data
@Accessors(chain = true)
public class PayProperties {

    private String notifyDomain;

    private AlipayProperties alipay;

    private WepayProperties wepay;

    @Data
    @Accessors(chain = true)
    public static class AlipayProperties {
        private boolean enabled = false;

        private boolean test = false;

        private String appId;

        private String signType;

        private String returnUrl;

        private String privateKey;

        private String publicKey;
    }

    @Data
    @Accessors(chain = true)
    public static class WepayProperties {

        private boolean enabled = false;

        private String appId;

        private String mchId;

        private String key;

        private String certPassword;

        private String certPath;

    }

}
