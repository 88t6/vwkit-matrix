package matrix.module.pay.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.jdbc.annotation.MatrixColumn;
import matrix.module.jdbc.annotation.MatrixTable;

import java.math.BigDecimal;

/**
 * @author wangcheng
 * @date 2019/4/29
 */
@Data
@Accessors(chain = true)
@MatrixTable("matrix_refund")
public class MatrixRefundEntity extends MatrixBaseEntity {

    @MatrixColumn("PAY_ID")
    private String payId;

    @MatrixColumn("ORDER_ID")
    private String orderId;

    @MatrixColumn("REFUND_PRICE")
    private BigDecimal refundPrice;

    @MatrixColumn("REFUND_ID")
    private String refundId;
}
