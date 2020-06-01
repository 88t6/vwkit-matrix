package matrix.module.pay.service;


import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.entity.MatrixPayEntity;

import java.util.List;

/**
 * @author WangCheng
 */
public interface MatrixPayService {

    /**
     * 保存支付实体
     *
     * @param payEntity
     */
    void savePayEntity(MatrixPayEntity payEntity);

    /**
     * 更新为支付成功
     *
     * @param queryPayResponses
     * @return List
     */
    List<MatrixPayEntity> changePayed(List<QueryPayResponse> queryPayResponses);

    /**
     * 根据payId获取实体
     *
     * @param payId
     * @return MatrixPayEntity
     */
    MatrixPayEntity getPayEntityByPayId(String payId);

    /**
     * 查询已支付的实体
     *
     * @param orderId
     * @param payChannel
     * @return List
     */
    List<MatrixPayEntity> getPayEntityByPayed(String orderId, String payChannel);

    /**
     * 查询已支付的实体
     *
     * @param outTradeNo
     * @param payChannel
     * @return List
     */
    List<MatrixPayEntity> getPayEntityByPayedForOutTradeNo(String outTradeNo, String payChannel);

    /**
     * 查询未支付的实体
     *
     * @param orderId
     * @param payChannel
     * @return List
     */
    List<MatrixPayEntity> getPayEntityByNoPay(String orderId, String payChannel);


}
