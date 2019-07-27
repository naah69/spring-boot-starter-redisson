package com.naah69.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 哈希助手
 *
 * @author xsx
 * @since 1.8
 */
public final class HashHandler implements RedisHandler {
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 对象模板
     */
    private HashOperations<String, String, Object> hashOperations;
    /**
     * 字符串模板
     */
    private HashOperations<String, String, String> stringHashOperations;

    /**
     * 哈希助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    HashHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.hashOperations = redisTemplate.opsForHash();
        this.stringHashOperations = stringRedisTemplate.opsForHash();
    }

    /**
     * 存入对象
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   对象
     * @see <a href="http://redis.io/commands/hset">Redis Documentation: HSET</a>
     * @since redis 2.0.0
     */
    public void putAsObj(String key, String hashKey, Object value) {
        this.hashOperations.put(key, hashKey, value);
    }

    /**
     * 存入字符串
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   字符串
     * @see <a href="http://redis.io/commands/hset">Redis Documentation: HSET</a>
     * @since redis 2.0.0
     */
    public void put(String key, String hashKey, String value) {
        this.stringHashOperations.put(key, hashKey, value);
    }

    /**
     * 存入对象如果不存在
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   对象
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/hsetnx">Redis Documentation: HSETNX</a>
     * @since redis 2.0.0
     */
    public Boolean putIfAbsentAsObj(String key, String hashKey, Object value) {
        return this.hashOperations.putIfAbsent(key, hashKey, value);
    }

    /**
     * 存入字符串如果不存在
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   字符串
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/hsetnx">Redis Documentation: HSETNX</a>
     * @since redis 2.0.0
     */
    public Boolean putIfAbsent(String key, String hashKey, String value) {
        return this.stringHashOperations.putIfAbsent(key, hashKey, value);
    }

    /**
     * 存入对象集合
     *
     * @param key 键
     * @param map 对象集合
     * @see <a href="http://redis.io/commands/hmset">Redis Documentation: HMSET</a>
     * @since redis 2.0.0
     */
    public void putAllAsObj(String key, Map<String, Object> map) {
        this.hashOperations.putAll(key, map);
    }

    /**
     * 存入字符串集合
     *
     * @param key 键
     * @param map 字符串集合
     * @see <a href="http://redis.io/commands/hmset">Redis Documentation: HMSET</a>
     * @since redis 2.0.0
     */
    public void putAll(String key, Map<String, String> map) {
        this.stringHashOperations.putAll(key, map);
    }

    /**
     * 获取对象
     *
     * @param key     键
     * @param hashKey hash键
     * @param <T>     返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/hget">Redis Documentation: HGET</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T getAsObj(String key, String hashKey) {
        return (T) this.hashOperations.get(key, hashKey);
    }

    /**
     * 获取对象
     *
     * @param type    返回值类型
     * @param key     键
     * @param hashKey hash键
     * @param <T>     返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/hget">Redis Documentation: HGET</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T getAsObj(Class<T> type, String key, String hashKey) {
        Object value = this.hashOperations.get(key, hashKey);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 获取字符串
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/hget">Redis Documentation: HGET</a>
     * @since redis 2.0.0
     */
    public String get(String key, String hashKey) {
        return this.stringHashOperations.get(key, hashKey);
    }

    /**
     * 批量获取对象
     *
     * @param key      键
     * @param hashKeys hash键
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/hmget">Redis Documentation: HMGET</a>
     * @since redis 2.0.0
     */
    public List mgetAsObj(String key, String... hashKeys) {
        return this.hashOperations.multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 批量获取字符串
     *
     * @param key      键
     * @param hashKeys hash键
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/hmget">Redis Documentation: HMGET</a>
     * @since redis 2.0.0
     */
    public List<String> mget(String key, String... hashKeys) {
        return this.stringHashOperations.multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 移除对象
     *
     * @param key      键
     * @param hashKeys hash键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/hdel">Redis Documentation: HDEL</a>
     * @since redis 2.0.0
     */
    public Long removeAsObj(String key, String... hashKeys) {
        return this.hashOperations.delete(key, (Object[]) hashKeys);
    }

    /**
     * 移除字符串
     *
     * @param key      键
     * @param hashKeys hash键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/hdel">Redis Documentation: HDEL</a>
     * @since redis 2.0.0
     */
    public Long remove(String key, String... hashKeys) {
        return this.stringHashOperations.delete(key, (Object[]) hashKeys);
    }

    /**
     * 获取对象集合
     *
     * @param key 键
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/hgetall">Redis Documentation: HGETALL</a>
     * @since redis 2.0.0
     */
    public Map<String, Object> entriesAsObj(String key) {
        return this.hashOperations.entries(key);
    }

    /**
     * 获取字符串集合
     *
     * @param key 键
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/hgetall">Redis Documentation: HGETALL</a>
     * @since redis 2.0.0
     */
    public Map<String, String> entries(String key) {
        return this.stringHashOperations.entries(key);
    }

    /**
     * 获取对象hash键集合
     *
     * @param key 键
     * @return 返回对象字典键集合
     * @see <a href="http://redis.io/commands/hkeys">Redis Documentation: HKEYS</a>
     * @since redis 2.0.0
     */
    public Set keysAsObj(String key) {
        return this.hashOperations.keys(key);
    }

    /**
     * 获取字符串hash键集合
     *
     * @param key 键
     * @return 返回字符串字典键集合
     * @see <a href="http://redis.io/commands/hkeys">Redis Documentation: HKEYS</a>
     * @since redis 2.0.0
     */
    public Set<String> keys(String key) {
        return this.stringHashOperations.keys(key);
    }

    /**
     * 获取对象集合
     *
     * @param key 键
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/hvals">Redis Documentation: HVALS</a>
     * @since redis 2.0.0
     */
    public List valuesAsObj(String key) {
        return this.hashOperations.values(key);
    }

    /**
     * 获取字符串集合
     *
     * @param key 键
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/hvals">Redis Documentation: HVALS</a>
     * @since redis 2.0.0
     */
    public List<String> values(String key) {
        return this.stringHashOperations.values(key);
    }

    /**
     * 获取字符串长度
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回字符串长度
     * @see <a href="http://redis.io/commands/hstrlen">Redis Documentation: HSTRLEN</a>
     * @since redis 3.2.0
     */
    public Long lengthOfValue(String key, String hashKey) {
        return this.stringHashOperations.lengthOfValue(key, hashKey);
    }

    /**
     * 获取对象数量
     *
     * @param key 键
     * @return 返回对象数量
     * @see <a href="http://redis.io/commands/hlen">Redis Documentation: HLEN</a>
     * @since redis 2.0.0
     */
    public Long sizeAsObj(String key) {
        return this.hashOperations.size(key);
    }

    /**
     * 获取字符串数量
     *
     * @param key 键
     * @return 返回字符串数量
     * @see <a href="http://redis.io/commands/hlen">Redis Documentation: HLEN</a>
     * @since redis 2.0.0
     */
    public Long size(String key) {
        return this.stringHashOperations.size(key);
    }

    /**
     * 是否包含对象的key
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/hexits">Redis Documentation: HEXISTS</a>
     * @since redis 2.0.0
     */
    public Boolean hasKeyAsObj(String key, String hashKey) {
        return this.hashOperations.hasKey(key, hashKey);
    }

    /**
     * 是否包含字符串的key
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/hexits">Redis Documentation: HEXISTS</a>
     * @since redis 2.0.0
     */
    public Boolean hasKey(String key, String hashKey) {
        return this.stringHashOperations.hasKey(key, hashKey);
    }

    /**
     * 自增
     * 已过期，请使用 {@link NumberHandler#addDouble(String, String, double)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @param data    步长
     * @return 返回自增后的值
     * @see <a href="http://redis.io/commands/hincrbyfloat">Redis Documentation: HINCRBYFLOAT</a>
     * @since redis 2.6.0
     */
    @Deprecated
    public Double increment(String key, String hashKey, Double data) {
        return this.stringHashOperations.increment(key, hashKey, data);
    }

    /**
     * 自增
     * 已过期，请使用 {@link NumberHandler#addLong(String, String, long)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @param data    步长
     * @return 返回自增后的值
     * @see <a href="http://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
     * @since redis 2.0.0
     */
    @Deprecated
    public Long increment(String key, String hashKey, Long data) {
        return this.stringHashOperations.increment(key, hashKey, data);
    }

    /**
     * 自增
     * 已过期，请使用 {@link NumberHandler#incrementLong(String, String)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回自增后的值
     * @see <a href="http://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
     * @since redis 2.0.0
     */
    @Deprecated
    public Long increment(String key, String hashKey) {
        return this.stringHashOperations.increment(key, hashKey, 1L);
    }

    /**
     * 递减
     * 已过期，请使用 {@link NumberHandler#subtractDouble(String, String, double)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @param data    步长
     * @return 返回递减后的值
     * @see <a href="http://redis.io/commands/hincrbyfloat">Redis Documentation: HINCRBYFLOAT</a>
     * @since redis 2.6.0
     */
    @Deprecated
    public Double decrement(String key, String hashKey, Double data) {
        return this.stringHashOperations.increment(key, hashKey, -data);
    }

    /**
     * 递减
     * 已过期，请使用 {@link NumberHandler#subtractLong(String, String, long)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @param data    步长
     * @return 返回递减后的值
     * @see <a href="http://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
     * @since redis 2.0.0
     */
    @Deprecated
    public Long decrement(String key, String hashKey, Long data) {
        return this.stringHashOperations.increment(key, hashKey, -data);
    }

    /**
     * 递减
     * 已过期，请使用 {@link NumberHandler#decrementLong(String, String)} 替换
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回递减后的值
     * @see <a href="http://redis.io/commands/hincrby">Redis Documentation: HINCRBY</a>
     * @since redis 2.0.0
     */
    @Deprecated
    public Long decrement(String key, String hashKey) {
        return this.stringHashOperations.increment(key, hashKey, -1L);
    }

    /**
     * 匹配对象
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配对象
     * @see <a href="http://redis.io/commands/hscan">Redis Documentation: HSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor<Map.Entry<String, Object>> scanAsObj(String key, Long count, String pattern) {
        return this.hashOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
    }

    /**
     * 匹配字符串
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配字符串
     * @see <a href="http://redis.io/commands/hscan">Redis Documentation: HSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor<Map.Entry<String, String>> scan(String key, Long count, String pattern) {
        return this.stringHashOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
    }

    /**
     * 获取spring redis模板
     *
     * @return 返回对象模板
     */
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取spring string redis模板
     *
     * @return 返回字符串模板
     */
    public StringRedisTemplate getStringRedisTemplate() {
        return this.stringRedisTemplate;
    }
}
