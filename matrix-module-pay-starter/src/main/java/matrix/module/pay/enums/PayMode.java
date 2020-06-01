package matrix.module.pay.enums;

/**
 * @author wangcheng
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
