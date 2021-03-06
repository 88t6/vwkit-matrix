package matrix.module.pay.converter;

import matrix.module.based.utils.JacksonUtil;
import matrix.module.pay.builder.body.WepayPayBody;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.templates.WepayTemplate;
import matrix.module.pay.vo.PayVo;
import org.springframework.util.StringUtils;


/**
 * @author wangcheng
 */
public class PayEntityConvert {

    public static final MatrixPayEntity convert(PayVo payVo, String body, String payChannel, String payModeCode) {
        MatrixPayEntity payEntity = new MatrixPayEntity()
                .setOrderId(payVo.getOrderId())
                .setPayId(payVo.getPayId())
                .setPrice(payVo.getPrice())
                .setBody(body);
        if (WepayTemplate.class.getSimpleName().equals(payChannel)) {
            WepayPayBody payBody = JacksonUtil.parseJson(body, WepayPayBody.class);
            //app和wejsapi支付方式直接返回请求参数
            if (PayMode.WEJSAPI.getCode().equals(payModeCode)) {
                payEntity.setUrl(payBody.getJsApiParams());
            } else if (PayMode.APP.getCode().equals(payModeCode)) {
                payEntity.setUrl(payBody.getAppParams());
            }
        }
        //如果url为空，则为跳转路径
        if (StringUtils.isEmpty(payEntity.getUrl())) {
            payEntity.setUrl(String.format("/forward-pay/%s/%s", payChannel, payVo.getPayId()));
        }
        payEntity.setStatus(PayConstant.NOPAY);
        return payEntity;
    }

}
