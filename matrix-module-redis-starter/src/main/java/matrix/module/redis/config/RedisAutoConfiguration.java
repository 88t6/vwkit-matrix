package matrix.module.redis.config;

import matrix.module.common.exception.ServiceException;
import matrix.module.common.helper.Assert;
import matrix.module.redis.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WangCheng
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
@ConditionalOnProperty(value = {"redis.enabled"})
public class RedisAutoConfiguration {

    public static final String DEFAULT_VALUE = "DEFAULT";

    public static final String DEFAULT_GENERATOR = "wiselyKeyGenerator";

    @Autowired
    private RedisProperties redisProperties;

    private static <T, S> RedisTemplate<T, S> createRedisTemplate(RedisConnectionFactory factory, Class<T> tClass, Class<S> sClass) {
        RedisTemplate<T, S> redisTemplate = new RedisTemplate<>();
        RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer();
        if (String.class.equals(tClass)) {
            RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
        } else {
            redisTemplate.setKeySerializer(redisSerializer);
            redisTemplate.setHashKeySerializer(redisSerializer);
        }
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    @Order(value = 1)
    public JedisConnectionFactory redisConnectionFactory() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        Duration duration = Duration.ofSeconds(60000);
        builder.readTimeout(duration).connectTimeout(duration);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(20);
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxWaitMillis(10000L);
        builder.usePooling().poolConfig(poolConfig);
        return this.getRedisConnectionFactory(builder.build());
    }

    private JedisConnectionFactory getRedisConnectionFactory(JedisClientConfiguration client) {
        if (redisProperties.getStandalone().isEnabled()) {
            RedisProperties.Standalone standaloneConfig = redisProperties.getStandalone();
            String hostName = standaloneConfig.getHost();
            Assert.isNotNull(hostName, "redis.hostname");
            Integer database = standaloneConfig.getDatabase();
            Assert.isNotNull(database, "redis.database");
            String password = standaloneConfig.getPassword();
            String port = standaloneConfig.getPort();
            Assert.isNotNull(port, "redis.port");
            RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration();
            standalone.setHostName(hostName);
            standalone.setDatabase(database);
            if (!StringUtils.isEmpty(password)) {
                standalone.setPassword(RedisPassword.of(password));
            }
            standalone.setPort(Integer.valueOf(port));
            return new JedisConnectionFactory(standalone, client);
        }
        if (redisProperties.getSentinel().isEnabled()) {
            RedisProperties.Sentinel sentinelConfig = redisProperties.getSentinel();
            String master = sentinelConfig.getMaster();
            Assert.isNotNull(master, "redis.sentinel.master");
            String nodes = sentinelConfig.getNodes();
            Assert.isNotNull(nodes, "redis.sentinel.nodes");
            String password = sentinelConfig.getPassword();
            Integer database = sentinelConfig.getDatabase();
            Assert.isNotNull(database, "redis.sentinel.database");
            RedisSentinelConfiguration sentinel = new RedisSentinelConfiguration();
            sentinel.master(master);
            sentinel.setSentinels(this.createRedisNodes(nodes));
            if (!StringUtils.isEmpty(password)) {
                sentinel.setPassword(RedisPassword.of(password));
            }
            sentinel.setDatabase(database);
            return new JedisConnectionFactory(sentinel, client);
        }
        if (redisProperties.getCluster().isEnabled()) {
            RedisProperties.Cluster clusterConfig = redisProperties.getCluster();
            String nodes = clusterConfig.getNodes();
            Assert.isNotNull(nodes, "redis.cluster.nodes");
            String password = clusterConfig.getPassword();
            Integer maxRedirects = clusterConfig.getMaxRedirects();
            Assert.isNotNull(maxRedirects, "redis.cluster.max-redirects");
            RedisClusterConfiguration cluster = new RedisClusterConfiguration();
            cluster.setClusterNodes(this.createRedisNodes(nodes));
            if (!StringUtils.isEmpty(password)) {
                cluster.setPassword(RedisPassword.of(password));
            }
            cluster.setMaxRedirects(maxRedirects);
            return new JedisConnectionFactory(cluster, client);
        }
        throw new ServiceException("standalone, sentinel, cluster 不允许全部同时停用");
    }

    private List<RedisNode> createRedisNodes(String data) {
        Assert.matchRegex("([^,:]+:\\d{1,5},)*([^,:]+:\\d{1,5}){1}",
                data, "格式为: host:port,host:port", null);
        List<RedisNode> nodes = new ArrayList<>();
        for (String text : data.split(",")) {
            if (!StringUtils.isEmpty(text)) {
                String[] parts = text.split(":");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            }
        }
        return nodes;
    }

    @Bean
    @Order(value = 2)
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Long expire = redisProperties.getDefaultExpire();
        expire = expire == null ? 7200L : expire;
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.disableKeyPrefix();
        config = config.disableCachingNullValues();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        config = config.entryTtl(Duration.ofSeconds(expire));
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }

    @Bean
    @Order(value = 3)
    public KeyGenerator wiselyKeyGenerator() {
        return (target, method, params) -> {
            List<String> cacheKey = new ArrayList<>();
            cacheKey.add(target.getClass().getName());
            cacheKey.add(method.getName());
            if (params != null && params.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Object obj : params) {
                    sb.append(obj != null ? String.valueOf(obj) : "NONE");
                }
                cacheKey.add(sb.toString());
            }
            return String.join(":", cacheKey);
        };
    }

    @Bean(name = "redisTemplate")
    @Order(value = 4)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    @Order(value = 5)
    public RedisTemplate<Object, Object> objectRedisTemplate(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, Object.class, Object.class);
    }

}
