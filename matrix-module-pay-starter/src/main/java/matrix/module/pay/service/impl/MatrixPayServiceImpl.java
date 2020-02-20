package matrix.module.pay.service.impl;

import matrix.module.pay.builder.response.QueryPayResponse;
import matrix.module.pay.entity.MatrixPayEntity;
import matrix.module.pay.service.MatrixPayService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Service
public class MatrixPayServiceImpl implements MatrixPayService {

    @Override
    public void savePayEntity(MatrixPayEntity payEntity) {

    }

    @Override
    public List<MatrixPayEntity> changePayed(List<QueryPayResponse> queryPayResponses) {
        return null;
    }

    @Override
    public MatrixPayEntity getPayEntityByPayId(String payId) {
        return null;
    }

    @Override
    public List<MatrixPayEntity> getPayEntityByPayed(String orderId, String payChannel) {
        return null;
    }

    @Override
    public List<MatrixPayEntity> getPayEntityByNoPay(String orderId, String payChannel) {
        return null;
    }
}
