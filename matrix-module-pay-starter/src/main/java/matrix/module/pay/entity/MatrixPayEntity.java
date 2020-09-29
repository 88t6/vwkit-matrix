package matrix.module.pay.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author wangcheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MatrixPayEntity extends MatrixBaseEntity {

    private String payId;

    private String orderId;

    private BigDecimal price;

    private String body;

    private String url;

    private BigDecimal actualPrice;

    private String outTradeNo;

    private String payMode;

    private String payChannel;
}
