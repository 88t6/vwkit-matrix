package matrix.module.pay.enums;

import matrix.module.common.exception.ServiceException;

/**
 * @author wangcheng
 * @date 2019/4/26
 */
public enum PayMode {

    PC("PC", "PC网站"),
    H5("H5", "H5页面"),
    APP("APP", "手机APP"),
    QrCode("QrCode", "二维码"),
    WEJSAPI("WE_JSAPI", "微信jsapi");

    private String code;

    private String name;

    PayMode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static PayMode getByCode(String code) {
        for (PayMode payMode : PayMode.values()) {
            if (payMode.getCode().equals(code)) {
                return payMode;
            }
        }
        throw new ServiceException("支付方式不存在");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
