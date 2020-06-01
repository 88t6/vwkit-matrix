package matrix.module.pay.service;

import matrix.module.pay.entity.MatrixRefundEntity;

import java.util.List;

/**
 * @author WangCheng
 */
public interface MatrixRefundService {

    /**
     * 保存退款实体
     *
     * @param refundEntity
     */
    void saveRefundEntity(MatrixRefundEntity refundEntity);

    /**
     * 更新为退款成功
     *
     * @param refundIds
     * @return List
     */
    List<MatrixRefundEntity> changeRefunded(List<String> refundIds);

    /**
     * 查询未退款的实体
     *
     * @param payId
     * @return List
     */
    List<MatrixRefundEntity> getRefundEntityByNoRefund(String payId);

    /**
     * 查询已退款的实体
     *
     * @param payId
     * @return List
     */
    List<MatrixRefundEntity> getRefundEntityByRefunded(String payId);
}
