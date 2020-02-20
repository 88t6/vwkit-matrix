package matrix.module.pay.converter;

import matrix.module.pay.entity.MatrixRefundEntity;
import matrix.module.pay.vo.RefundVo;

/**
 * @author wangcheng
 * @date 2019/4/29
 */
public class RefundEntityConvert {

    public static MatrixRefundEntity convert(RefundVo refundVo, String orderId, Integer status) {
        MatrixRefundEntity refundEntity = new MatrixRefundEntity()
                .setRefundId(refundVo.getRefundId())
                .setRefundPrice(refundVo.getRefundAmount())
                .setPayId(refundVo.getPayId())
                .setOrderId(orderId);
        refundEntity.setStatus(status);
        return refundEntity;
    }
}
