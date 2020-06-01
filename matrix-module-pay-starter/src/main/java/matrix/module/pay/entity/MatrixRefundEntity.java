package matrix.module.pay.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author wangcheng
 */
@Data
@Accessors(chain = true)
public class MatrixRefundEntity extends MatrixBaseEntity {

    private String refundId;

    private String payId;

    private String orderId;

    private BigDecimal refundPrice;
}
