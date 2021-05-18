package matrix.module.pay.controller;

import matrix.module.based.utils.JacksonUtil;
import matrix.module.common.helper.QrCodeHelper;
import matrix.module.pay.builder.body.WepayPayBody;
import matrix.module.pay.converter.HtmlConvert;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.service.MatrixPayService;
import matrix.module.pay.templates.AlipayTemplate;
import matrix.module.pay.templates.WepayTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangcheng
 */
@RestController
public class ForwardPayController {

    @Autowired
    private MatrixPayService payService;

    @GetMapping("/forward-pay/{payChannel}/{payId}")
    public String pay(@PathVariable String payChannel, @PathVariable String payId,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        MatrixPayEntity payEntity = payService.getPayEntityByPayId(payId);
        if (AlipayTemplate.class.getSimpleName().equals(payChannel)) {
            return HtmlConvert.bodyConvert(payEntity.getBody());
        } else if (WepayTemplate.class.getSimpleName().equals(payChannel)) {
            WepayPayBody body = JacksonUtil.parseJson(payEntity.getBody(), WepayPayBody.class);
            if (PayMode.PC.getCode().equals(body.getPayModeCode())
                    || PayMode.QrCode.getCode().equals(body.getPayModeCode())) {
                String image = QrCodeHelper.getInstance(body.getQrCodeWidth().intValue(), body.getQrCodeWidth().intValue(), null)
                        .getImageBase64(body.getUrl());
                return HtmlConvert.imageConvert(image);
            } else if (PayMode.H5.getCode().equals(body.getPayModeCode())) {
                response.sendRedirect(body.getUrl());
            }
        }
        return "error";
    }
}
