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
 * @author: wangcheng
 * @date 2019/4/26
 */
public class PayEntityConvert {

    public static final MatrixPayEntity convert(PayVo payVo, String body, String payChannel, String payModeCode) {
        MatrixPayEntity payEntity = new MatrixPayEntity()
                .setOrderId(payVo.getOrderId())
                .setPayId(payVo.getPayId())
                .setPrice(payVo.getPrice())
                .setBody(body);
        if (WepayTemplate.class.getSimpleName().equals(payChannel)) {
            //app和wejsapi支付方式直接返回PrepayId
            if (PayMode.APP.getCode().equals(payModeCode) || PayMode.WEJSAPI.getCode().equals(payModeCode)) {
                WepayPayBody payBody = JacksonUtil.parseJson(body, WepayPayBody.class);
                payEntity.setUrl(payBody.getPrepayId());
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
