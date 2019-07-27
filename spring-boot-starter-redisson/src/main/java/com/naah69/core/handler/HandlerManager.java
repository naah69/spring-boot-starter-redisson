package com.naah69.core.handler;

import com.naah69.core.config.redisson.RedissonClientHelper;
import com.naah69.core.config.redisson.RedissonConnectionFactory;
import com.naah69.core.util.ApplicationContextUtil;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 助手管理
 *
 * @author xsx
 * @date 2019/5/23
 * @since 1.8
 */
final class HandlerManager {
    /**
     * redis模板(用于对象)
     */
    @SuppressWarnings("unchecked")
    private static final RedisTemplate<String, Object> REDIS_TEMPLATE = ApplicationContextUtil.getContext().getBean("redisTemplate", RedisTemplate.class);
    /**
     * redis模板(用于字符串)
     */
    private static final StringRedisTemplate STRING_REDIS_TEMPLATE = ApplicationContextUtil.getContext().getBean("stringRedisTemplate", StringRedisTemplate.class);
    /**
     * 默认KEY
     */
    private static final String DEFAULT_KEY = "default";
    /**
     * 默认数据库索引
     */
    private static final int DEFAULT_DB_INDEX = ApplicationContextUtil.getContext().getBean(RedisProperties.class).getDatabase();
    /**
     * 助手容器
     */
    private final ConcurrentMap<HandlerType, ConcurrentMap<String, RedisHandler>> container = initContainer();
    /**
     * 集群助手实例
     */
    private final ClusterHandler clusterHandler = new ClusterHandler(REDIS_TEMPLATE);


    /**
     * 助手管理构造
     */
    HandlerManager() {
    }

    /**
     * 获取默认KEY
     *
     * @return 返回默认KEY
     */
    String getDefaultKey() {
        return DEFAULT_KEY;
    }

    /**
     * 获取助手
     *
     * @param key  KEY
     * @param type 助手类型
     * @return 返回助手
     */
    RedisHandler getHandler(String key, HandlerType type) {
        // 若是集群助手类型，则直接返回
        if (type == HandlerType.CLUSTER) {
            return clusterHandler;
        }
        ConcurrentMap<String, RedisHandler> map = this.container.get(type);
        RedisHandler handler = map.get(key);
        if (handler != null) {
            return handler;
        }
        synchronized (this.container) {
            handler = map.get(key);
            if (handler == null) {
                RedisHandler instance = this.getHandlerInstance(key, type);
                handler = map.putIfAbsent(key, instance);
                if (handler == null) {
                    handler = instance;
                }
            }
        }
        return handler;
    }

    /**
     * 获取默认的对象模板
     *
     * @return 返回默认的对象模板
     */
    RedisTemplate<String, Object> getDefaultRedisTemplate() {
        return REDIS_TEMPLATE;
    }

    /**
     * 获取默认的字符串模板
     *
     * @return 返回默认的字符串模板
     */
    StringRedisTemplate getDefaultStringRedisTemplate() {
        return STRING_REDIS_TEMPLATE;
    }

    /**
     * 获取连接工厂
     *
     * @param dbIndex 数据库索引
     * @return 返回连接工厂
     */
    static RedisConnectionFactory getConnectionFactory(int dbIndex) {
        RedisConnectionFactory redisConnectionFactory = ApplicationContextUtil.getContext().getBean(RedisConnectionFactory.class);
        if (redisConnectionFactory instanceof LettuceConnectionFactory) {
            ((LettuceConnectionFactory) redisConnectionFactory).setDatabase(dbIndex);
        } else if (redisConnectionFactory instanceof JedisConnectionFactory) {
            JedisConnectionFactory factory = (JedisConnectionFactory) redisConnectionFactory;
            if (factory.isRedisSentinelAware()) {
                RedisSentinelConfiguration sentinelConfiguration = factory.getSentinelConfiguration();
                if (sentinelConfiguration != null) {
                    sentinelConfiguration.setDatabase(dbIndex);
                }
            } else {
                RedisStandaloneConfiguration standaloneConfiguration = factory.getStandaloneConfiguration();
                if (standaloneConfiguration != null) {
                    standaloneConfiguration.setDatabase(dbIndex);
                }
            }
        } else if (redisConnectionFactory instanceof RedissonConnectionFactory) {
            redisConnectionFactory = new RedissonConnectionFactory(
                    RedissonClientHelper.createClient(dbIndex)
            );
        } else {
            throw new RuntimeException("no support connection factory");
        }
        return redisConnectionFactory;
    }

    /**
     * 创建对象模板
     *
     * @param dbIndex 数据库索引
     * @return 返回对象模板
     */
    @SuppressWarnings("unchecked")
    static RedisTemplate<String, Object> createRedisTemplate(int dbIndex) {
        return createTemplate(dbIndex, false);
    }

    /**
     * 创建字符串模板
     *
     * @param dbIndex 数据库索引
     * @return 返回字符串模板
     */
    static StringRedisTemplate createStringRedisTemplate(int dbIndex) {
        return (StringRedisTemplate) createTemplate(dbIndex, true);
    }

    /**
     * 创建模板
     *
     * @param dbIndex  数据库索引
     * @param isString 是否为字符串模板
     * @return 返回模板
     */
    static RedisTemplate createTemplate(int dbIndex, boolean isString) {
        return initRedisTemplate(getConnectionFactory(dbIndex), isString);
    }

    /**
     * 创建模板
     *
     * @param dbIndex 数据库索引
     * @return 返回模板
     */
    static List<RedisTemplate> createTemplate(int dbIndex) {
        RedisConnectionFactory connectionFactory = getConnectionFactory(dbIndex);
        return Arrays.asList(
                initRedisTemplate(connectionFactory, false),
                initRedisTemplate(connectionFactory, true)
        );
    }

    /**
     * 初始化模板
     *
     * @param factory  连接工厂
     * @param isString 是否为字符串模板
     * @return 返回模板
     */
    @SuppressWarnings("unchecked")
    private static RedisTemplate initRedisTemplate(RedisConnectionFactory factory, boolean isString) {
        RedisTemplate redisTemplate;
        if (isString) {
            redisTemplate = new StringRedisTemplate(factory);
        } else {
            redisTemplate = new RedisTemplate<String, Object>();
            redisTemplate.setKeySerializer(REDIS_TEMPLATE.getKeySerializer());
            redisTemplate.setValueSerializer(REDIS_TEMPLATE.getValueSerializer());
            redisTemplate.setHashKeySerializer(REDIS_TEMPLATE.getHashKeySerializer());
            redisTemplate.setHashValueSerializer(REDIS_TEMPLATE.getHashValueSerializer());
            redisTemplate.setConnectionFactory(factory);
            redisTemplate.afterPropertiesSet();
        }
        return redisTemplate;
    }

    /**
     * 初始化容器
     *
     * @return 返回容器
     */
    private ConcurrentMap<HandlerType, ConcurrentMap<String, RedisHandler>> initContainer() {
        String dbIndex = String.valueOf(DEFAULT_DB_INDEX);
        ConcurrentMap<HandlerType, ConcurrentMap<String, RedisHandler>> container = new ConcurrentHashMap<>(32);
        HandlerType[] types = HandlerType.values();
        RedisHandler handler;
        for (HandlerType type : types) {
            ConcurrentHashMap<String, RedisHandler> handlerMap = new ConcurrentHashMap<>(256);
            container.put(type, handlerMap);
            // 初始化跳过redLock
            if (type == HandlerType.REDISLOCK) {
                continue;
            }
            handler = this.getHandlerInstance(dbIndex, type);
            handlerMap.put(getDefaultKey(), Objects.requireNonNull(handler));
            handlerMap.put(dbIndex, handler);

        }
        return container;
    }

    /**
     * 获取助手实例
     *
     * @param key  KEY
     * @param type 助手类型
     * @return 返回实例
     */
    @SuppressWarnings("unchecked")
    private RedisHandler getHandlerInstance(String key, HandlerType type) {
        Class clz = type.getTypeClass();
        try {
            Constructor constructor = clz.getDeclaredConstructor(Integer.class);
            constructor.setAccessible(true);
            RedisHandler handler;
            if (type == HandlerType.REDISLOCK && DEFAULT_KEY.equalsIgnoreCase(key)) {
                handler = (RedisHandler) constructor.newInstance(DEFAULT_DB_INDEX);
            } else {
                handler = (RedisHandler) constructor.newInstance(Integer.valueOf(key));
            }
            return handler;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
