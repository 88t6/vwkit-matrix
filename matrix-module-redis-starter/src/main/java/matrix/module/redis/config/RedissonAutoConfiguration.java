package matrix.module.redis.config;

import matrix.module.common.exception.ServiceException;
import matrix.module.redis.properties.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author wangcheng
 * 2021/4/22
 **/
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(value = {"redis.enabled", "redis.redisson"})
public class RedissonAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        int timeout = redisProperties.getTimeout().intValue() * 1000;
        if (redisProperties.getStandalone().isEnabled()) {
            //单机模式
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress("redis://" + redisProperties.getStandalone().getHost() + ":" + redisProperties.getStandalone().getPort())
                    .setTimeout(timeout)
                    .setDatabase(redisProperties.getStandalone().getDatabase());
            if(!StringUtils.isEmpty(redisProperties.getStandalone().getPassword())) {
                serverConfig.setPassword(redisProperties.getStandalone().getPassword());
            }
            return Redisson.create(config);
        }
        if (redisProperties.getCluster().isEnabled()) {
            //集群模式
            ClusterServersConfig serverConfig = config.useClusterServers()
                    .setTimeout(timeout);
            if (!StringUtils.isEmpty(redisProperties.getCluster().getNodes())) {
                String[] nodes = redisProperties.getCluster().getNodes().split(",");
                for (String node: nodes) {
                    serverConfig.addNodeAddress("redis://" + node);
                }
            }
            if(!StringUtils.isEmpty(redisProperties.getCluster().getPassword())) {
                serverConfig.setPassword(redisProperties.getCluster().getPassword());
            }
            return Redisson.create(config);
        }
        if (redisProperties.getSentinel().isEnabled()) {
            //哨兵模式
            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .setTimeout(timeout)
                    .setDatabase(redisProperties.getSentinel().getDatabase());
            if (!StringUtils.isEmpty(redisProperties.getSentinel().getNodes())) {
                String[] nodes = redisProperties.getSentinel().getNodes().split(",");
                for (String node: nodes) {
                    serverConfig.addSentinelAddress("redis://" + node);
                }
            }
            if(!StringUtils.isEmpty(redisProperties.getSentinel().getPassword())) {
                serverConfig.setPassword(redisProperties.getSentinel().getPassword());
            }
            return Redisson.create(config);
        }
        throw new ServiceException("standalone, sentinel, cluster 不允许全部同时停用");
    }
}
