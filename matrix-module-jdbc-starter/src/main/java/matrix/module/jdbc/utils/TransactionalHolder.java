package matrix.module.jdbc.utils;

import com.alibaba.druid.util.StringUtils;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author WangCheng
 * @date 2020/2/12
 */
public class TransactionalHolder {

    public static final String TRANSACTIONAL_FLAG = "transactional";

    public static final String DATASOURCE_FLAG = "datasource";

    public static final ThreadLocal<TransactionTemplate> transactionTemplateHolder = new ThreadLocal<>();

    /**
     * transactional 事务优先(则将开始事务传递给数据源开启)
     * datasource 数据源切换优先(原事务开启不变)
     * 空代表刚进来
     */
    private static final ThreadLocal<String> priorityHolder = new ThreadLocal<>();

    public static boolean setPriority(String flag) {
        if (StringUtils.isEmpty(priorityHolder.get())) {
            priorityHolder.set(flag);
            return true;
        }
        return false;
    }

    public static TransactionTemplate getTransactionTemplate() {
        return transactionTemplateHolder.get();
    }

    public static void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        transactionTemplateHolder.set(transactionTemplate);
    }
}
