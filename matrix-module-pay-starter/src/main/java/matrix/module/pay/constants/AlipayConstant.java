package matrix.module.pay.constants;

import matrix.module.common.utils.RandomUtil;

/**
 * @author wangcheng
 */
public class AlipayConstant {

    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    public static final String TEST_GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";

    public static final String FORMAT = "JSON";

    public static final String NOTIFY_URI = RandomUtil.getUUID();

    public static final String ALIPAY_PC_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    public static final String ALIPAY_H5_PRODUCT_CODE = "QUICK_WAP_WAY";

    public static final String ALIPAY_APP_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    public static final String QR_PAY_MODE = "4";

    public static final String SUCCESS_RETURN_CODE = "success";

    public static final String FAIL_RETURN_CDDE = "failure";

    /**
     * 支付宝支付交易成功标识
     */
    public static final String PAY_SUCCESS_FLAG = "TRADE_SUCCESS";

    /**
     * 支付宝交易完成。
     */
    public static final String PAY_FINISHED_FLAG = "TRADE_FINISHED";

    /**
     * 支付宝退款成功
     */
    public static final String REFUND_SUCCESS_FLAG = "SUCCESS";
}
