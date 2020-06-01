package matrix.module.pay.service.impl;

import matrix.module.common.helper.Assert;
import matrix.module.common.utils.DateUtil;
import matrix.module.jdbc.annotation.TargetDataSource;
import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.constants.PayConstant;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.service.MatrixPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WangCheng
 */
@Service
@TargetDataSource("${pay.db}")
public class MatrixPayServiceImpl implements MatrixPayService {

    private static final String MATRIX_PAY_SELECT = "SELECT " +
            "ORDER_ID AS orderId, PAY_ID AS payId, PRICE as price, BODY as body, " +
            "URL as url, ACTUAL_PRICE as actualPrice, OUT_TRADE_NO as outTradeNo, " +
            "PAY_MODE as payMode, PAY_CHANNEL as payChannel, ORDER_BY as orderBy, " +
            "CREATE_TIME as createTime, UPDATE_TIME as updateTime, STATUS as status " +
            "FROM matrix_pay ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void savePayEntity(MatrixPayEntity payEntity) {
        String sql = "INSERT INTO matrix_pay (PAY_ID, ORDER_ID, PRICE, BODY, URL, ACTUAL_PRICE," +
                "OUT_TRADE_NO, PAY_MODE, PAY_CHANNEL, ORDER_BY, CREATE_TIME, UPDATE_TIME, STATUS)" +
                "VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?)";
        String createTime = DateUtil.formatDateStrByPattern(payEntity.getCreateTime() == null ? new Date() : payEntity.getCreateTime(), DateUtil.TimeFormat.LongTimeStandard);
        String updateTime = DateUtil.formatDateStrByPattern(payEntity.getUpdateTime() == null ? new Date() : payEntity.getUpdateTime(), DateUtil.TimeFormat.LongTimeStandard);
        jdbcTemplate.update(sql, payEntity.getPayId(), payEntity.getOrderId(),
                payEntity.getPrice(), payEntity.getBody(), payEntity.getUrl(),
                payEntity.getActualPrice(), payEntity.getOutTradeNo(), payEntity.getPayMode(), payEntity.getPayChannel(),
                payEntity.getOrderBy(), createTime, updateTime, payEntity.getStatus());
    }

    @Override
    public List<MatrixPayEntity> changePayed(List<QueryPayResponse> queryPayResponses) {
        Assert.state(!CollectionUtils.isEmpty(queryPayResponses), "queryPayResponses not be null");
        Object[] payIds = new Object[queryPayResponses.size()];
        List<String> payIdsPlaceholder = new ArrayList<>();
        List<Object[]> params = new ArrayList<>();
        for (int i = 0; i < queryPayResponses.size(); i++) {
            payIdsPlaceholder.add("?");
            QueryPayResponse queryPayResponse = queryPayResponses.get(i);
            payIds[i] = queryPayResponse.getPayId();
            Object[] param = new Object[]{queryPayResponse.getOutTradeNo(),
                    BigDecimal.valueOf(Double.valueOf(queryPayResponse.getActualPrice())).setScale(2, BigDecimal.ROUND_HALF_UP),
                    PayConstant.PAYED, queryPayResponse.getPayId(), PayConstant.NOPAY};
            params.add(param);
        }
        jdbcTemplate.batchUpdate("UPDATE matrix_pay SET OUT_TRADE_NO = ?, ACTUAL_PRICE = ?, " +
                "STATUS = ?, UPDATE_TIME = NOW() WHERE PAY_ID = ? AND STATUS = ?", params);
        String querySql = MATRIX_PAY_SELECT + " WHERE PAY_ID IN (" + String.join(",", payIdsPlaceholder) + ")";
        return jdbcTemplate.query(querySql, payIds, new BeanPropertyRowMapper<>(MatrixPayEntity.class));
    }

    @Override
    public MatrixPayEntity getPayEntityByPayId(String payId) {
        List<MatrixPayEntity> list = jdbcTemplate.query(MATRIX_PAY_SELECT + " WHERE PAY_ID = ?", new Object[]{payId}, new BeanPropertyRowMapper<>(MatrixPayEntity.class));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<MatrixPayEntity> getPayEntityByPayed(String orderId, String payChannel) {
        return jdbcTemplate.query(MATRIX_PAY_SELECT + "WHERE ORDER_ID = ? AND PAY_CHANNEL = ? AND STATUS = ?",
                new Object[]{orderId, payChannel, PayConstant.PAYED}, new BeanPropertyRowMapper<>(MatrixPayEntity.class));
    }

    @Override
    public List<MatrixPayEntity> getPayEntityByPayedForOutTradeNo(String outTradeNo, String payChannel) {
        return jdbcTemplate.query(MATRIX_PAY_SELECT + "WHERE OUT_TRADE_NO = ? AND PAY_CHANNEL = ? AND STATUS = ?",
                new Object[]{outTradeNo, payChannel, PayConstant.PAYED}, new BeanPropertyRowMapper<>(MatrixPayEntity.class));
    }

    @Override
    public List<MatrixPayEntity> getPayEntityByNoPay(String orderId, String payChannel) {
        return jdbcTemplate.query(MATRIX_PAY_SELECT + "WHERE ORDER_ID = ? AND PAY_CHANNEL = ? AND STATUS = ?",
                new Object[]{orderId, payChannel, PayConstant.NOPAY}, new BeanPropertyRowMapper<>(MatrixPayEntity.class));
    }
}
