package matrix.module.pay.enums;

import matrix.module.pay.constants.AlipayConstant;
import matrix.module.pay.constants.WepayConstant;

/**
 * @author wangcheng
 */
public enum PayNotify {

    Alipay(AlipayConstant.NOTIFY_URI, "alipayTemplate"),
    Wepay(WepayConstant.NOTIFY_URI, "wepayTemplate");

    private String id;

    private String beanName;

    PayNotify(String id, String beanName) {
        this.id = id;
        this.beanName = beanName;
    }

    public static String getBeanNameById(String id) {
        PayNotify[] payNotifies = PayNotify.values();
        for (PayNotify payNotify : payNotifies) {
            if (payNotify.getId().equals(id)) {
                return payNotify.getBeanName();
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
