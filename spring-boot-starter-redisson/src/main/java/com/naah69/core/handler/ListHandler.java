package com.naah69.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 列表助手
 *
 * @author xsx
 * @since 1.8
 */
public final class ListHandler implements RedisHandler {
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
    private ListOperations<String, Object> listOperations;
    /**
     * 字符串模板
     */
    private ListOperations<String, String> stringListOperations;

    /**
     * 列表助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    ListHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.listOperations = redisTemplate.opsForList();
        this.stringListOperations = stringRedisTemplate.opsForList();
    }

    /**
     * 获取对象列表数量
     *
     * @param key 键
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
     * @since redis 1.0.0
     */
    public Long sizeAsObj(String key) {
        return this.listOperations.size(key);
    }

    /**
     * 获取列表数量
     *
     * @param key 键
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
     * @since redis 1.0.0
     */
    public Long size(String key) {
        return this.stringListOperations.size(key);
    }

    /**
     * 获取所有对象
     *
     * @param key 键
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List getAllAsObj(String key) {
        return this.lrangeAsObj(key, 0L, -1L);
    }

    /**
     * 获取所有字符串
     *
     * @param key 键
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List<String> getAll(String key) {
        return this.lrange(key, 0L, -1L);
    }

    /**
     * 从左获取范围内的对象
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List lrangeAsObj(String key, Long startIndex, Long endIndex) {
        return this.listOperations.range(key, startIndex, endIndex);
    }

    /**
     * 从左获取范围内的字符串
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List<String> lrange(String key, Long startIndex, Long endIndex) {
        return this.stringListOperations.range(key, startIndex, endIndex);
    }

    /**
     * 从右获取范围内的对象
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List rrangeAsObj(String key, Long startIndex, Long endIndex) {
        List list = this.lrangeAsObj(key, -endIndex - 1, -startIndex - 1);
        Collections.reverse(list);
        return list;
    }

    /**
     * 从右获取范围内的字符串
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     * @since redis 1.0.0
     */
    public List<String> rrange(String key, Long startIndex, Long endIndex) {
        List<String> list = this.lrange(key, -endIndex - 1, -startIndex - 1);
        Collections.reverse(list);
        return list;
    }

    /**
     * 从左移除对象
     *
     * @param key   键
     * @param count 个数
     * @param value 对象
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
     * @since redis 1.0.0
     */
    public Long lremoveAsObj(String key, Long count, Object value) {
        return this.listOperations.remove(key, count, value);
    }

    /**
     * 从左移除字符串
     *
     * @param key   键
     * @param count 个数
     * @param value 字符串
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
     * @since redis 1.0.0
     */
    public Long lremove(String key, Long count, String value) {
        return this.stringListOperations.remove(key, count, value);
    }

    /**
     * 从右移除对象
     *
     * @param key   键
     * @param count 个数
     * @param value 对象
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
     * @since redis 1.0.0
     */
    public Long rremoveAsObj(String key, Long count, Object value) {
        return this.lremoveAsObj(key, -count, value);
    }

    /**
     * 从右移除字符串
     *
     * @param key   键
     * @param count 个数
     * @param value 字符串
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
     * @since redis 1.0.0
     */
    public Long rremove(String key, Long count, String value) {
        return this.lremove(key, -count, value);
    }

    /**
     * 从左截取对象(会修改redis中列表)
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回截取的对象列表
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public List lsubListAsObj(String key, Long startIndex, Long endIndex) {
        this.listOperations.trim(key, startIndex, endIndex);
        return this.lrangeAsObj(key, startIndex, this.sizeAsObj(key));
    }

    /**
     * 从左截取字符串(会修改redis中列表)
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回截取的字符串列表
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public List<String> lsubList(String key, Long startIndex, Long endIndex) {
        this.stringListOperations.trim(key, startIndex, endIndex);
        return this.lrange(key, startIndex, this.size(key));
    }

    /**
     * 从右截取对象(会修改redis中列表)
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回截取的对象列表
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public List rsubListAsObj(String key, Long startIndex, Long endIndex) {
        this.listOperations.trim(key, -endIndex - 1, -startIndex - 1);
        int length = this.sizeAsObj(key).intValue();
        List<Object> list = new ArrayList<>(length);
        String temp = String.format("%s_temp", key);
        for (int i = 0; i < length; i++) {
            list.add(this.rpopAndrpushAsObj(key, temp));
        }
        this.redisTemplate.rename(temp, key);
        return list;
    }

    /**
     * 从右截取字符串(会修改redis中列表)
     *
     * @param key        键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 返回截取的字符串列表
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public List<String> rsubList(String key, Long startIndex, Long endIndex) {
        this.stringListOperations.trim(key, -endIndex - 1, -startIndex - 1);
        int length = this.size(key).intValue();
        List<String> list = new ArrayList<>(length);
        String temp = String.format("%s_temp", key);
        for (int i = 0; i < length; i++) {
            list.add(this.rpopAndrpush(key, temp));
        }
        this.stringRedisTemplate.rename(temp, key);
        return list;
    }

    /**
     * 从左修改指定索引的对象
     *
     * @param key   键
     * @param index 索引
     * @param value 对象
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public void lsetAsObj(String key, Long index, Object value) {
        this.listOperations.set(key, index, value);
    }

    /**
     * 从左修改指定索引的字符串
     *
     * @param key   键
     * @param index 索引
     * @param value 字符串
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public void lset(String key, Long index, String value) {
        this.stringListOperations.set(key, index, value);
    }

    /**
     * 从右修改指定索引的对象
     *
     * @param key   键
     * @param index 索引
     * @param value 对象
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public void rsetAsObj(String key, Long index, Object value) {
        this.listOperations.set(key, -index - 1, value);
    }

    /**
     * 从右修改指定索引的字符串
     *
     * @param key   键
     * @param index 索引
     * @param value 字符串
     * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     * @since redis 1.0.0
     */
    public void rset(String key, Long index, String value) {
        this.stringListOperations.set(key, -index - 1, value);
    }

    /**
     * 从左获取对象
     *
     * @param key   键
     * @param index 索引
     * @param <T>   对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T lgetAsObj(String key, Long index) {
        return (T) this.listOperations.index(key, index);
    }

    /**
     * 从左获取对象
     *
     * @param type  返回值类型
     * @param key   键
     * @param index 索引
     * @param <T>   返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T lgetAsObj(Class<T> type, String key, Long index) {
        Object value = this.listOperations.index(key, index);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从左获取字符串
     *
     * @param key   键
     * @param index 索引
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    public String lget(String key, Long index) {
        return this.stringListOperations.index(key, index);
    }

    /**
     * 从右获取对象
     *
     * @param key   键
     * @param index 索引
     * @param <T>   对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rgetAsObj(String key, Long index) {
        return (T) this.listOperations.index(key, -index - 1);
    }

    /**
     * 从右获取对象
     *
     * @param type  返回值类型
     * @param key   键
     * @param index 索引
     * @param <T>   返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rgetAsObj(Class<T> type, String key, Long index) {
        Object value = this.listOperations.index(key, -index - 1);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从右获取字符串
     *
     * @param key   键
     * @param index 索引
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
     * @since redis 1.0.0
     */
    public String rget(String key, Long index) {
        return this.stringListOperations.index(key, -index - 1);
    }

    /**
     * 从左插入对象
     *
     * @param key   键
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpushAsObj(String key, Object value) {
        return this.listOperations.leftPush(key, value);
    }

    /**
     * 按照中心点从左插入对象
     *
     * @param key   键
     * @param pivot 中心点对象
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpushAsObj(String key, Object pivot, Object value) {
        return this.listOperations.leftPush(key, pivot, value);
    }

    /**
     * 从左插入字符串
     *
     * @param key   键
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpush(String key, String value) {
        return this.stringListOperations.leftPush(key, value);
    }

    /**
     * 按照中心点从左插入字符串
     *
     * @param key   键
     * @param pivot 中心点字符串
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpush(String key, String pivot, String value) {
        return this.stringListOperations.leftPush(key, pivot, value);
    }

    /**
     * 从左插入多个对象
     *
     * @param key    键
     * @param values 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpushAllAsObj(String key, Object... values) {
        return this.listOperations.leftPushAll(key, values);
    }

    /**
     * 从左插入多个字符串
     *
     * @param key    键
     * @param values 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.0.0
     */
    public Long lpushAll(String key, String... values) {
        return this.stringListOperations.leftPushAll(key, values);
    }

    /**
     * 从左插入对象如果列表存在
     *
     * @param key   键
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
     * @since redis 2.2.0
     */
    public Long lpushIfPresentAsObj(String key, Object value) {
        return this.listOperations.leftPushIfPresent(key, value);
    }

    /**
     * 从左插入字符串如果列表存在
     *
     * @param key   键
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
     * @since redis 2.2.0
     */
    public Long lpushIfPresent(String key, String value) {
        return this.stringListOperations.leftPushIfPresent(key, value);
    }

    /**
     * 从左弹出对象
     *
     * @param key 键
     * @param <T> 对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T lpopAsObj(String key) {
        return (T) this.listOperations.leftPop(key);
    }

    /**
     * 从左弹出对象
     *
     * @param type 返回值类型
     * @param key  键
     * @param <T>  返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T lpopAsObj(Class<T> type, String key) {
        Object value = this.listOperations.leftPop(key);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从左弹出字符串
     *
     * @param key 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @since redis 1.0.0
     */
    public String lpop(String key) {
        return this.stringListOperations.leftPop(key);
    }

    /**
     * 从左弹出对象(阻塞)
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @param <T>     对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T blpopAsObj(String key, Long timeout, TimeUnit unit) {
        return (T) this.listOperations.leftPop(key, timeout, unit);
    }

    /**
     * 从左弹出对象(阻塞)
     *
     * @param type    返回值类型
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @param <T>     返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T blpopAsObj(Class<T> type, String key, Long timeout, TimeUnit unit) {
        Object value = this.listOperations.leftPop(key, timeout, unit);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从左弹出字符串(阻塞)
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
     * @since redis 2.0.0
     */
    public String blpop(String key, Long timeout, TimeUnit unit) {
        return this.stringListOperations.leftPop(key, timeout, unit);
    }

    /**
     * 从右插入对象
     *
     * @param key   键
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpushAsObj(String key, Object value) {
        return this.listOperations.rightPush(key, value);
    }

    /**
     * 从右插入字符串
     *
     * @param key   键
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpush(String key, String value) {
        return this.stringListOperations.rightPush(key, value);
    }

    /**
     * 从右插入对象
     *
     * @param key   键
     * @param pivot 中心点对象
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpushAsObj(String key, Object pivot, Object value) {
        return this.listOperations.rightPush(key, pivot, value);
    }

    /**
     * 从右插入字符串
     *
     * @param key   键
     * @param pivot 中心点字符串
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpush(String key, String pivot, String value) {
        return this.stringListOperations.rightPush(key, pivot, value);
    }

    /**
     * 从右插入对象如果列表存在
     *
     * @param key   键
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
     * @since redis 2.2.0
     */
    public Long rpushIfPresentAsObj(String key, Object value) {
        return this.listOperations.rightPushIfPresent(key, value);
    }

    /**
     * 从右插入字符串如果列表存在
     *
     * @param key   键
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
     * @since redis 2.2.0
     */
    public Long rpushIfPresent(String key, String value) {
        return this.stringListOperations.rightPushIfPresent(key, value);
    }

    /**
     * 从右插入对象
     *
     * @param key   键
     * @param value 对象
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpushAllAsObj(String key, Object... value) {
        return this.listOperations.rightPushAll(key, value);
    }

    /**
     * 从右插入字符串
     *
     * @param key   键
     * @param value 字符串
     * @return 返回列表数量
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.0.0
     */
    public Long rpushAll(String key, String... value) {
        return this.stringListOperations.rightPushAll(key, value);
    }

    /**
     * 从右弹出对象
     *
     * @param key 键
     * @param <T> 对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rpopAsObj(String key) {
        return (T) this.listOperations.rightPop(key);
    }

    /**
     * 从右弹出对象
     *
     * @param type 返回值类型
     * @param key  键
     * @param <T>  返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rpopAsObj(Class<T> type, String key) {
        Object value = this.listOperations.rightPop(key);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从右弹出字符串
     *
     * @param key 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @since redis 1.0.0
     */
    public String rpop(String key) {
        return this.stringListOperations.rightPop(key);
    }

    /**
     * 从右弹出对象(阻塞)
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @param <T>     对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T brpopAsObj(String key, Long timeout, TimeUnit unit) {
        return (T) this.listOperations.rightPop(key, timeout, unit);
    }

    /**
     * 从右弹出对象(阻塞)
     *
     * @param type    返回值类型
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @param <T>     返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     * @since redis 2.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T brpopAsObj(Class<T> type, String key, Long timeout, TimeUnit unit) {
        Object value = this.listOperations.rightPop(key, timeout, unit);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从右弹出字符串(阻塞)
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    单位
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
     * @since redis 2.0.0
     */
    public String brpop(String key, Long timeout, TimeUnit unit) {
        return this.stringListOperations.rightPop(key, timeout, unit);
    }

    /**
     * 从左弹出对象并从左插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @param <T>      对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.2.0
     */
    public <T> T lpopAndlpushAsObj(String key, String otherKey) {
        T t = this.lpopAsObj(key);
        this.lpushAsObj(otherKey, t);
        return t;
    }

    /**
     * 从左弹出字符串并从左插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     * @since redis 1.2.0
     */
    public String lpopAndlpush(String key, String otherKey) {
        String v = this.lpop(key);
        this.lpush(otherKey, v);
        return v;
    }

    /**
     * 从右弹出对象并从左插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @param <T>      对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
     * @since redis 1.2.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rpopAndlpushAsObj(String key, String otherKey) {
        return (T) this.listOperations.rightPopAndLeftPush(key, otherKey);
    }

    /**
     * 从右弹出对象并从左插入到另一个列表
     *
     * @param type     返回值类型
     * @param key      键
     * @param otherKey 键
     * @param <T>      返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
     * @since redis 1.2.0
     */
    @SuppressWarnings("unchecked")
    public <T> T rpopAndlpushAsObj(Class<T> type, String key, String otherKey) {
        Object value = this.listOperations.rightPopAndLeftPush(key, otherKey);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 从右弹出字符串并从左插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
     * @since redis 1.2.0
     */
    public String rpopAndlpush(String key, String otherKey) {
        return this.stringListOperations.rightPopAndLeftPush(key, otherKey);
    }

    /**
     * 从右弹出对象并从右插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @param <T>      对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.2.0
     */
    public <T> T rpopAndrpushAsObj(String key, String otherKey) {
        T t = this.rpopAsObj(key);
        this.rpushAsObj(otherKey, t);
        return t;
    }

    /**
     * 从右弹出字符串并从右插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.2.0
     */
    public String rpopAndrpush(String key, String otherKey) {
        String v = this.rpop(key);
        this.rpush(otherKey, v);
        return v;
    }

    /**
     * 从左弹出对象并从右插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @param <T>      对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.2.0
     */
    public <T> T lpopAndrpushAsObj(String key, String otherKey) {
        T t = this.lpopAsObj(key);
        this.rpushAsObj(otherKey, t);
        return t;
    }

    /**
     * 从左弹出字符串并从右插入到另一个列表
     *
     * @param key      键
     * @param otherKey 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     * @since redis 1.2.0
     */
    public String lpopAndrpush(String key, String otherKey) {
        String v = this.lpop(key);
        this.rpush(otherKey, v);
        return v;
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
