package matrix.module.pay.builder.body;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wangcheng
 */
@Data
@Accessors(chain = true)
public class WepayPayBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jsApiParams;

    private String appParams;

    private String url;

    private Long qrCodeWidth;

    private String payModeCode;
}
