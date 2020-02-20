package matrix.module.pay.constants;

/**
 * @author wangcheng
 * @date 2019/4/26
 */
public class PayConstant {

    public static final String NOTIFY_PAY_URL_PREFIX = "/notify-pay/";

    public static final String NOTIFY_RETURN_URL_PREFIX = "/notify-refund/";

    /**
     * 已支付
     */
    public static final Integer PAYED = 1;

    /**
     * 未支付
     */
    public static final Integer NOPAY = 0;

    /**
     * 支付关闭
     */
    public static final Integer CLOSEPAY = -1;

    /**
     * 已退款
     */
    public static final Integer REFUNDED = 1;

    /**
     * 退款中
     */
    public static final Integer REFUNDING = 0;

}
