package matrix.module.pay.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import matrix.module.jdbc.annotation.MatrixColumn;
import matrix.module.jdbc.annotation.MatrixTable;

import java.math.BigDecimal;

/**
 * @author wangcheng
 * @date 2018/12/25
 */
@Data
@Accessors(chain = true)
@MatrixTable("matrix_pay")
public class MatrixPayEntity extends MatrixBaseEntity {

    @MatrixColumn("ORDER_ID")
    private String orderId;

    @MatrixColumn("PAY_ID")
    private String payId;

    @MatrixColumn("PRICE")
    private BigDecimal price;

    @MatrixColumn("BODY")
    private String body;

    @MatrixColumn("URL")
    private String url;

    @MatrixColumn("ACTUAL_PRICE")
    private BigDecimal actualPrice;

    @MatrixColumn("OUT_TRADE_NO")
    private String outTradeNo;

    @MatrixColumn("PAY_MODE")
    private String payMode;

    @MatrixColumn("PAY_CHANNEL")
    private String payChannel;
}
