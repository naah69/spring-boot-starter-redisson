package com.naah69.core.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * redisson连接配置
 * 重写org.redisson.spring.starter.RedissonAutoConfiguration
 *
 * @author xsx
 * @author naah
 * @since 1.8
 */
@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@ConditionalOnProperty(prefix = "spring.redis.redisson", name = "enable", havingValue = "true")
@EnableConfigurationProperties({RedissonProperties.class, RedisProperties.class})
public class RedissonAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonAutoConfiguration.class);
    @Autowired
    private RedissonProperties redissonProperties;
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ApplicationContext ctx;

    public RedissonAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    public RedisConnectionFactory redisConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean({RedissonClient.class})
    public RedissonClient redisson() {
        RedissonClient redissonClient = Redisson.create(this.createConfig(null));
        return Redisson.create(this.createConfig(null));
    }

    public RedissonConnectionConfiguration createConfig(Integer dbIndex) {
        if (dbIndex == null) {
            dbIndex = this.redisProperties.getDatabase();
        }
        RedissonConnectionConfiguration redissonConnectionConfiguration;
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, this.redisProperties);
        int timeout;
        Method nodesMethod;
        if (null == timeoutValue) {
            timeout = 0;
        } else if (!(timeoutValue instanceof Integer)) {
            nodesMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = ((Long) ReflectionUtils.invokeMethod(nodesMethod, timeoutValue)).intValue();
        } else {
            timeout = (Integer) timeoutValue;
        }

        if (!this.redissonProperties.judgeNull()) {
            try {
                redissonConnectionConfiguration = RedissonConnectionConfiguration.fromJSON(redissonProperties.toJSON());
            } catch (Exception e) {
                LOGGER.error("can not parse redissonConnectionConfiguration", e);
                throw new IllegalArgumentException("Can't parse redissonConnectionConfiguration", e);
            }
        } else if (this.redisProperties.getSentinel() != null) {
            nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, this.redisProperties.getSentinel());
            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = this.convert(Arrays.asList(((String) nodesValue).split(",")));
            } else {
                nodes = this.convert((List) nodesValue);
            }

            redissonConnectionConfiguration = new RedissonConnectionConfiguration();
            redissonConnectionConfiguration.useSentinelServers().setMasterName(this.redisProperties.getSentinel().getMaster()).addSentinelAddress(nodes).setDatabase(dbIndex).setConnectTimeout(timeout).setPassword(this.redisProperties.getPassword());
        } else {
            Method method;
            if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, this.redisProperties) != null) {
                Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, this.redisProperties);
                method = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
                List<String> nodesObject = (List) ReflectionUtils.invokeMethod(method, clusterObject);
                String[] nodes = this.convert(nodesObject);
                redissonConnectionConfiguration = new RedissonConnectionConfiguration();
                redissonConnectionConfiguration.useClusterServers().addNodeAddress(nodes).setConnectTimeout(timeout).setPassword(this.redisProperties.getPassword());
            } else {
                redissonConnectionConfiguration = new RedissonConnectionConfiguration();
                String prefix = "redis://";
                method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
                if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, this.redisProperties)) {
                    prefix = "rediss://";
                }

                redissonConnectionConfiguration.useSingleServer().setAddress(prefix + this.redisProperties.getHost() + ":" + this.redisProperties.getPort()).setConnectTimeout(timeout).setDatabase(dbIndex).setPassword(this.redisProperties.getPassword());
            }
        }
        return redissonConnectionConfiguration;
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        Iterator var3 = nodesObject.iterator();

        while (true) {
            while (var3.hasNext()) {
                String node = (String) var3.next();
                if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                    nodes.add("redis://" + node);
                } else {
                    nodes.add(node);
                }
            }
            return nodes.toArray(new String[nodes.size()]);
        }
    }
}