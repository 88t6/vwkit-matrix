package matrix.module.pay.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangcheng
 */
public class RequestUtil {

    private static final Logger logger = LogManager.getLogger(RequestUtil.class);

    /**
     * 解析 request params 。
     *
     * @param request
     * @return Map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, String> parse(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> dataMap = new HashMap(map.size());
        try {
            String key;
            String[] values;
            String valueStr;
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                key = entry.getKey();
                values = entry.getValue();
                if (values == null || values.length <= 0) {
                    dataMap.put(key, "");
                    continue; // next map
                } else {
                    valueStr = Arrays.stream(values).collect(Collectors.joining(","));
                    dataMap.put(key, valueStr);
                }
            }
        } catch (Exception e) {
            logger.error("解析request.getParameterMap()异常", e);
            throw new RuntimeException(e);
        }
        return dataMap;
    }
}
