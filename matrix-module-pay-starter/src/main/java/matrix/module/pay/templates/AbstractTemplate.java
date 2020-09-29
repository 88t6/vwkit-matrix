package matrix.module.pay.templates;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.pay.builder.request.QueryRefundRequest;
import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.builder.response.QueryRefundResponse;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.converter.PayEntityConvert;
import matrix.module.pay.converter.RefundEntityConvert;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.entity.MatrixRefundEntity;
import matrix.module.pay.enums.PayMode;
import matrix.module.pay.interfaces.PayInterface;
import matrix.module.pay.service.MatrixPayService;
import matrix.module.pay.service.MatrixRefundService;
import matrix.module.pay.vo.PayVo;
import matrix.module.pay.vo.RefundVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author WangCheng
 */
public abstract class AbstractTemplate {

    private static final Logger logger = LogManager.getLogger(AbstractTemplate.class);

    @Autowired
    private MatrixPayService payService;

    @Autowired
    private MatrixRefundService refundService;

    @Autowired(required = false)
    private PayInterface payInterface;

    /**
     * 调用支付
     *
     * @param payMode 支付方式
     * @param payVo 支付VO
     * @return payUrl
     */
    protected abstract String invokePay(PayMode payMode, PayVo payVo);

    /**
     * 调用退款
     *
     * @param refundVo 退款VO
     * @return refund status
     */
    protected abstract Integer invokeRefund(RefundVo refundVo);

    /**
     * 调用支付查询
     *
     * @param payIds 支付IDS
     * @return List
     */
    protected abstract List<QueryPayResponse> invokeQueryPay(List<String> payIds);

    /**
     * 调用退款查询
     *
     * @param queryRefundRequests 查询退款参数
     * @return List
     */
    protected abstract List<String> invokeQueryRefund(List<QueryRefundRequest> queryRefundRequests);

    /**
     * 解析支付通知
     *
     * @param request 请求
     * @return String
     */
    public abstract String parsePayNotify(HttpServletRequest request);

    /**
     * 解析退款通知
     *
     * @param request 请求
     * @return String
     */
    public abstract String parseRefundNotify(HttpServletRequest request);

    /**
     * 支付
     *
     * @param payMode 支付方式
     * @param payVo 支付VO
     */
    public String doPay(PayMode payMode, PayVo payVo) {
        payVo.validate(payMode);
        String body = this.invokePay(payMode, payVo);
        if (!StringUtils.isEmpty(body)) {
            MatrixPayEntity payEntity = PayEntityConvert.convert(payVo, body, this.getClass().getSimpleName(), payMode.getCode())
                    .setPayMode(payMode.getCode())
                    .setPayChannel(this.getClass().getSimpleName());
            payService.savePayEntity(payEntity);
            return payEntity.getUrl();
        }
        return null;
    }

    /**
     * 退款
     *
     * @param refundVo 退款VO
     *
     */
    public void doRefund(RefundVo refundVo) {
        refundVo.validate();
        MatrixPayEntity payEntity = null;
        if (refundVo.getPayId() != null) {
            payEntity = payService.getPayEntityByPayId(refundVo.getPayId());
        } else {
            List<MatrixPayEntity> matrixPayEntities = payService.getPayEntityByPayedForOutTradeNo(refundVo.getOutTradeNo(), this.getClass().getSimpleName());
            if (CollectionUtils.isEmpty(matrixPayEntities) || matrixPayEntities.size() > 1) {
                throw new ServiceException("支付存在多条，或不存在支付订单");
            }
            payEntity = matrixPayEntities.get(0);
            refundVo.setPayId(payEntity.getPayId());
        }
        Assert.notNullTip(payEntity, "payEntity");
        Integer refundStatus = this.invokeRefund(refundVo);
        MatrixRefundEntity refundEntity = RefundEntityConvert.convert(refundVo, payEntity.getOrderId(), refundStatus);
        refundService.saveRefundEntity(refundEntity);
    }

    /**
     * 支付查询(根据支付流水)
     *
     * @param payId 支付ID
     */
    public MatrixPayEntity doQueryPayByPayId(String payId) {
        Assert.notNullTip(payId, "payId");
        MatrixPayEntity payEntity = payService.getPayEntityByPayId(payId);
        Assert.notNullTip(payEntity, "payEntity");
        if (PayConstant.PAYED.equals(payEntity.getStatus())) {
            return payEntity;
        }
        List<QueryPayResponse> list = this.invokeQueryPay(Collections.singletonList(payId));
        if (!CollectionUtils.isEmpty(list)) {
            List<MatrixPayEntity> payEntities = payService.changePayed(list);
            return !CollectionUtils.isEmpty(payEntities) ? payEntities.get(0) : null;
        }
        return null;
    }

    /**
     * 支付查询(根据订单号)
     *
     * @param orderId 订单号
     */
    public List<MatrixPayEntity> doQueryPayByOrderId(String orderId) {
        Assert.notNullTip(orderId, "orderId");
        String payChannel = this.getClass().getSimpleName();
        List<MatrixPayEntity> noPayList = payService.getPayEntityByNoPay(orderId, payChannel);
        if (!CollectionUtils.isEmpty(noPayList)) {
            List<String> payIds = new ArrayList<>();
            for (MatrixPayEntity payEntity : noPayList) {
                payIds.add(payEntity.getPayId());
            }
            List<QueryPayResponse> list = this.invokeQueryPay(payIds);
            if (!CollectionUtils.isEmpty(list)) {
                payService.changePayed(list);
            }
        }
        return payService.getPayEntityByPayed(orderId, payChannel);
    }

    /**
     * 退款查询(根据支付流水)
     *
     * @param payId 支付ID
     * @return List
     */
    public List<MatrixRefundEntity> doQueryRefundByPayId(String payId) {
        Assert.notNullTip(payId, "payId");
        List<MatrixRefundEntity> noRefundList = refundService.getRefundEntityByNoRefund(payId);
        if (!CollectionUtils.isEmpty(noRefundList)) {
            List<QueryRefundRequest> refundRequests = new ArrayList<>();
            for (MatrixRefundEntity refundEntity : noRefundList) {
                refundRequests.add(new QueryRefundRequest(refundEntity.getRefundId(), refundEntity.getPayId()));
            }
            List<String> list = this.invokeQueryRefund(refundRequests);
            if (!CollectionUtils.isEmpty(list)) {
                refundService.changeRefunded(list);
            }
        }
        return refundService.getRefundEntityByRefunded(payId);
    }

    /**
     * 支付成功回调
     *
     * @param queryPayResponse 查询支付返回
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean callbackPaySuccess(QueryPayResponse queryPayResponse) {
        List<MatrixPayEntity> payEntities = payService.changePayed(Collections.singletonList(queryPayResponse));
        if (CollectionUtils.isEmpty(payEntities)) {
            return false;
        }
        if (payInterface != null) {
            return payInterface.paySuccess(payEntities);
        } else {
            logger.warn("No Bean interface PayInterface");
        }
        return true;
    }

    /**
     * 退款成功回调
     *
     * @param queryRefundResponse 退款回调
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean callbackRefundSuccess(QueryRefundResponse queryRefundResponse) {
        List<MatrixRefundEntity> refundEntities = refundService.changeRefunded(Collections.singletonList(queryRefundResponse.getRefundId()));
        if (CollectionUtils.isEmpty(refundEntities)) {
            return false;
        }
        if (payInterface != null) {
            return payInterface.refundSuccess(refundEntities);
        } else {
            logger.warn("No Bean interface PayInterface");
        }
        return true;
    }

}
