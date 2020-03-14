package matrix.module.oplog.service.impl;

import matrix.module.common.utils.DateUtil;
import matrix.module.jdbc.annotation.TargetDataSource;
import matrix.module.oplog.entity.OpLogEntity;
import matrix.module.oplog.service.MatrixOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author wangcheng
 * @date 2020-03-14
 */
@Service
@TargetDataSource("${op-log.db}")
public class MatrixOpLogServiceImpl implements MatrixOpLogService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Async
    public void saveOpLog(OpLogEntity opLogEntity) {
        String sql = "INSERT INTO matrix_oplog (ID, NAME, URI, REQUEST, RESPONSE, CREATE_TIME)" +
                "VALUES (?, ?, ?, ?, ? ,?)";
        String createTime = DateUtil.formatDateStrByPattern(opLogEntity.getCreateTime() == null ? new Date() : opLogEntity.getCreateTime(), DateUtil.TimeFormat.LongTimeStandard);
        jdbcTemplate.update(sql, opLogEntity.getId(), opLogEntity.getName(), opLogEntity.getUri(),
                opLogEntity.getRequest(), opLogEntity.getResponse(), createTime);
    }
}