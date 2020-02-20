package matrix.module.pay.service.impl;

import matrix.module.jdbc.annotation.TargetDataSource;
import matrix.module.pay.entity.MatrixRefundEntity;
import matrix.module.pay.service.MatrixRefundService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Service
@TargetDataSource("${pay.db}")
public class MatrixRefundServiceImpl implements MatrixRefundService {

    @Override
    public void saveRefundEntity(MatrixRefundEntity refundEntity) {

    }

    @Override
    public List<MatrixRefundEntity> changeRefunded(List<String> refundIds) {
        return null;
    }

    @Override
    public List<MatrixRefundEntity> getRefundEntityByNoRefund(String payId) {
        return null;
    }

    @Override
    public List<MatrixRefundEntity> getRefundEntityByRefunded(String payId) {
        return null;
    }
}
