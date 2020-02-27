package matrix.module.pay.service.impl;

import matrix.module.common.helper.Assert;
import matrix.module.common.helper.encrypt.MD5;
import matrix.module.common.utils.DateUtil;
import matrix.module.common.utils.RandomUtil;
import matrix.module.jdbc.annotation.TargetDataSource;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.entity.MatrixRefundEntity;
import matrix.module.pay.service.MatrixRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Service
@TargetDataSource("${pay.db}")
public class MatrixRefundServiceImpl implements MatrixRefundService {

    private static final String MATRIX_REFUND_SELECT = "SELECT " +
            "ORDER_ID AS orderId, PAY_ID AS payId, REFUND_PRICE as refundPrice, " +
            "REFUND_ID as refundId, ORDER_BY as orderBy, " +
            "CREATE_TIME as createTime, UPDATE_TIME as updateTime, STATUS as status " +
            "FROM matrix_refund ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveRefundEntity(MatrixRefundEntity refundEntity) {
        String sql = "INSERT INTO matrix_refund (REFUND_ID, ORDER_ID, PAY_ID, REFUND_PRICE," +
                "ORDER_BY, CREATE_TIME, UPDATE_TIME, STATUS)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String createTime = DateUtil.formatDateStrByPattern(refundEntity.getCreateTime() == null ? new Date() : refundEntity.getCreateTime(), DateUtil.TimeFormat.LongTimeStandard);
        String updateTime = DateUtil.formatDateStrByPattern(refundEntity.getUpdateTime() == null ? new Date() : refundEntity.getUpdateTime(), DateUtil.TimeFormat.LongTimeStandard);
        jdbcTemplate.update(sql, refundEntity.getRefundId(), refundEntity.getOrderId(),
                refundEntity.getPayId(), refundEntity.getRefundPrice(),
                refundEntity.getOrderBy(), createTime, updateTime, refundEntity.getStatus());
    }

    @Override
    public List<MatrixRefundEntity> changeRefunded(List<String> refundIds) {
        Assert.state(!CollectionUtils.isEmpty(refundIds), "refundIds not be null");
        List<String> refundIdsPlaceholder = new ArrayList<>();
        for (int i = 0; i < refundIds.size(); i++) {
            refundIdsPlaceholder.add("?");
        }
        List<Object> params = new ArrayList<>();
        params.add(PayConstant.REFUNDED);
        params.addAll(refundIds);
        params.add(PayConstant.REFUNDING);
        jdbcTemplate.update("UPDATE matrix_refund SET STATUS = ?, UPDATE_TIME = NOW() WHERE REFUND_ID IN (" + String.join(",", refundIdsPlaceholder) + ") AND STATUS = ?", params);
        String querySql = MATRIX_REFUND_SELECT + " WHERE REFUND_ID IN (" + String.join(",", refundIdsPlaceholder) + ")";
        return jdbcTemplate.query(querySql, refundIds.toArray(), new BeanPropertyRowMapper<>(MatrixRefundEntity.class));
    }

    @Override
    public List<MatrixRefundEntity> getRefundEntityByNoRefund(String payId) {
        String querySql = MATRIX_REFUND_SELECT + " WHERE REFUND_ID = ? AND STATUS != ?";
        return jdbcTemplate.query(querySql, new Object[]{payId, PayConstant.REFUNDED}, new BeanPropertyRowMapper<>(MatrixRefundEntity.class));
    }

    @Override
    public List<MatrixRefundEntity> getRefundEntityByRefunded(String payId) {
        String querySql = MATRIX_REFUND_SELECT + " WHERE REFUND_ID = ? AND STATUS = ?";
        return jdbcTemplate.query(querySql, new Object[]{payId, PayConstant.REFUNDED}, new BeanPropertyRowMapper<>(MatrixRefundEntity.class));
    }
}
