package matrix.module.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WangCheng
 * @date 2020/2/5
 */
public class ExcludeAutoConfigurationFilter implements AutoConfigurationImportFilter {

    private static final Set<String> SHOULD_SKIP = new HashSet<>(Collections.singletonList(
            "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"));

    @Override
    public boolean[] match(String[] classNames, AutoConfigurationMetadata metadata) {
        boolean[] matches = new boolean[classNames.length];
        for (int i = 0; i < classNames.length; i++) {
            matches[i] = !SHOULD_SKIP.contains(classNames[i]);
        }
        return matches;
    }
}
