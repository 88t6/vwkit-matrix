package matrix.module.oplog.service;

import matrix.module.oplog.entity.OpLogEntity;

/**
 * @author wangcheng
 * date 2020-03-14
 */
public interface MatrixOpLogService {

    void saveOpLog(OpLogEntity opLogEntity);
}
