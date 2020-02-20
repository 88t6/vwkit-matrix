package matrix.module.pay.builder.request;

import java.io.Serializable;

/**
 * @author wangcheng
 * @date 2019/4/29
 */
public class QueryRefundRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String refundId;

    private String payId;

    public QueryRefundRequest(String refundId, String payId) {
        this.refundId = refundId;
        this.payId = payId;
    }

    public String getRefundId() {
        return refundId;
    }

    public QueryRefundRequest setRefundId(String refundId) {
        this.refundId = refundId;
        return this;
    }

    public String getPayId() {
        return payId;
    }

    public QueryRefundRequest setPayId(String payId) {
        this.payId = payId;
        return this;
    }
}
