package matrix.module.pay.constants;

import matrix.module.common.utils.RandomUtil;

/**
 * author: wangcheng
 */
public class WepayConstant {

    public static final String NOTIFY_URI = RandomUtil.getUUID();

    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public static final String QUERY_PAY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    public static final String QUERY_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

    public static final String RETURN_CODE = "SUCCESS";

    public static final String JSAPI_PAY = "JSAPI";

    public static final String NATIVE_PAY = "NATIVE";

    public static final String APP_PAY = "APP";

    public static final String H5_PAY = "MWEB";

    public static final String SUCCESS_RETURN_CODE = "<xml>" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>" +
            "  <return_msg><![CDATA[OK]]></return_msg>" +
            "</xml>";

    public static final String FAIL_RETURN_CODE = "<xml>" +
            "  <return_code><![CDATA[FAIL]]></return_code>" +
            "  <return_msg><![CDATA[FAIL]]></return_msg>" +
            "</xml>";

}
