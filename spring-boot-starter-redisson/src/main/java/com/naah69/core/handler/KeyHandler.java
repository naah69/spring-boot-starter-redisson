package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 键助手
 *
 * @author xsx
 * @date 2019/4/25
 * @since 1.8
 */
public final class KeyHandler implements RedisHandler {
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 键助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    KeyHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
    }

    /**
     * 是否存在key(对象)
     *
     * @param key 键
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @since redis 1.0.0
     */
    public Boolean hasKeyAsObj(String key) {
        return this.redisTemplate.hasKey(key);
    }

    /**
     * 是否存在key(字符串)
     *
     * @param key 键
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @since redis 1.0.0
     */
    public Boolean hasKey(String key) {
        return this.stringRedisTemplate.hasKey(key);
    }

    /**
     * 移除对象key
     *
     * @param keys 键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/del">Redis Documentation: DEL</a>
     * @since redis 1.0.0
     */
    public Long removeAsObj(String... keys) {
        return this.redisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 移除字符串key
     *
     * @param keys 键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/del">Redis Documentation: DEL</a>
     * @since redis 1.0.0
     */
    public Long remove(String... keys) {
        return this.stringRedisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 移除存在的对象key
     *
     * @param keys 键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/unlink">Redis Documentation: UNLINK</a>
     * @since redis 4.0.0
     */
    public Long unlinkAsObj(String... keys) {
        return this.redisTemplate.unlink(Arrays.asList(keys));
    }

    /**
     * 移除存在的字符串key
     *
     * @param keys 键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/unlink">Redis Documentation: UNLINK</a>
     * @since redis 4.0.0
     */
    public Long unlink(String... keys) {
        return this.stringRedisTemplate.unlink(Arrays.asList(keys));
    }

    /**
     * 设置对象过期时间
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
     * @see <a href="http://redis.io/commands/pexpire">Redis Documentation: PEXPIRE</a>
     * @since redis 1.0.0
     */
    public Boolean expireAsObj(String key, long timeout, TimeUnit timeUnit) {
        return this.redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 设置字符串过期时间
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
     * @see <a href="http://redis.io/commands/pexpire">Redis Documentation: PEXPIRE</a>
     * @since redis 1.0.0
     */
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return this.stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 设置对象过期时间
     *
     * @param key  键
     * @param date 过期时间
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
     * @see <a href="http://redis.io/commands/pexpireat">Redis Documentation: PEXPIREAT</a>
     * @since redis 1.2.0
     */
    public Boolean expireAtAsObj(String key, Date date) {
        return this.redisTemplate.expireAt(key, date);
    }

    /**
     * 设置字符串过期时间
     *
     * @param key  键
     * @param date 过期时间
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
     * @see <a href="http://redis.io/commands/pexpireat">Redis Documentation: PEXPIREAT</a>
     * @since redis 1.2.0
     */
    public Boolean expireAt(String key, Date date) {
        return this.stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 获取对象过期时间
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 返回对象过期时间
     * @see <a href="http://redis.io/commands/ttl">Redis Documentation: TTL</a>
     * @since redis 1.0.0
     */
    public Long getExpireAsObj(String key, TimeUnit timeUnit) {
        return this.redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 获取字符串过期时间
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 返回字符串过期时间
     * @see <a href="http://redis.io/commands/ttl">Redis Documentation: TTL</a>
     * @since redis 1.0.0
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return this.stringRedisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 获取对象存储数据类型
     *
     * @param key 键
     * @return 返回对象存储数据类型
     * @see <a href="http://redis.io/commands/type">Redis Documentation: TYPE</a>
     * @since redis 1.0.0
     */
    public DataType getTypeAsObj(String key) {
        return this.redisTemplate.type(key);
    }

    /**
     * 获取字符串存储数据类型
     *
     * @param key 键
     * @return 返回字符串存储数据类型
     * @see <a href="http://redis.io/commands/type">Redis Documentation: TYPE</a>
     * @since redis 1.0.0
     */
    public DataType getType(String key) {
        return this.stringRedisTemplate.type(key);
    }

    /**
     * 对象的键集合
     *
     * @param pattern 键规则
     * @return 返回对象键的集合
     * @see <a href="http://redis.io/commands/keys">Redis Documentation: KEYS</a>
     * @since redis 1.0.0
     */
    public Set<String> keysAsObj(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    /**
     * 字符串的键集合
     *
     * @param pattern 键规则
     * @return 返回字符串键的集合
     * @see <a href="http://redis.io/commands/keys">Redis Documentation: KEYS</a>
     * @since redis 1.0.0
     */
    public Set<String> keys(String pattern) {
        return this.stringRedisTemplate.keys(pattern);
    }

    /**
     * 对象的键存在的数量
     *
     * @param keys 键
     * @return 返回对象键的数量
     * @see <a href="http://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @since redis 1.0.0
     */
    public Long keysCountAsObj(String... keys) {
        return this.redisTemplate.countExistingKeys(Arrays.asList(keys));
    }

    /**
     * 字符串的键存在的数量
     *
     * @param keys 键
     * @return 返回字符串键的数量
     * @see <a href="http://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @since redis 1.0.0
     */
    public Long keysCount(String... keys) {
        return this.stringRedisTemplate.countExistingKeys(Arrays.asList(keys));
    }

    /**
     * 获取对象随机key
     *
     * @return 返回对象随机的键
     * @see <a href="http://redis.io/commands/randomkey">Redis Documentation: RANDOMKEY</a>
     * @since redis 1.0.0
     */
    public String randomKeyAsObj() {
        String o;
        try {
            o = this.redisTemplate.randomKey();
        } catch (SerializationException e) {
            o = null;
        }
        return o;
    }

    /**
     * 获取字符串随机key
     *
     * @return 返回字符串随机的键
     * @see <a href="http://redis.io/commands/randomkey">Redis Documentation: RANDOMKEY</a>
     * @since redis 1.0.0
     */
    public String randomKey() {
        return this.stringRedisTemplate.randomKey();
    }

    /**
     * 重命名对象key
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @see <a href="http://redis.io/commands/rename">Redis Documentation: RENAME</a>
     * @since redis 1.0.0
     */
    public void renameAsObj(String oldKey, String newKey) {
        this.redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 重命名字符串key
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @see <a href="http://redis.io/commands/rename">Redis Documentation: RENAME</a>
     * @since redis 1.0.0
     */
    public void rename(String oldKey, String newKey) {
        this.stringRedisTemplate.rename(oldKey, newKey);
    }

    /**
     * 重命名对象key如果存在
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
     * @since redis 1.0.0
     */
    public Boolean renameAsObjIfAbsent(String oldKey, String newKey) {
        return this.redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 重命名字符串key如果存在
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
     * @since redis 1.0.0
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return this.stringRedisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 持久化对象
     *
     * @param key 键
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/persist">Redis Documentation: PERSIST</a>
     * @since redis 2.2.0
     */
    public Boolean persistAsObj(String key) {
        return this.redisTemplate.persist(key);
    }

    /**
     * 持久化字符串
     *
     * @param key 键
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/persist">Redis Documentation: PERSIST</a>
     * @since redis 2.2.0
     */
    public Boolean persist(String key) {
        return this.stringRedisTemplate.persist(key);
    }

    /**
     * 移动对象到指定数据库
     *
     * @param dbIndex 数据库索引
     * @param key     键
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/move">Redis Documentation: MOVE</a>
     * @since redis 1.0.0
     */
    public Boolean moveAsObj(int dbIndex, String key) {
        return this.redisTemplate.move(key, dbIndex);
    }

    /**
     * 移动字符串到指定数据库
     *
     * @param dbIndex 数据库索引
     * @param key     键
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/move">Redis Documentation: MOVE</a>
     * @since redis 1.0.0
     */
    public Boolean move(int dbIndex, String key) {
        return this.stringRedisTemplate.move(key, dbIndex);
    }

    /**
     * 当前数据库中键的数量
     *
     * @return 返回键的数量
     * @see <a href="http://redis.io/commands/dbsize">Redis Documentation: DBSIZE</a>
     * @since redis 1.0.0
     */
    public Long count() {
        Long count;
        try {
            count = this.redisTemplate.getRequiredConnectionFactory().getConnection().dbSize();
        } catch (IllegalStateException ex) {
            count = this.stringRedisTemplate.getRequiredConnectionFactory().getConnection().dbSize();
        }
        return count;
    }

    /**
     * 获取序列化对象
     *
     * @param key 键
     * @return 返回字节数组
     * @see <a href="http://redis.io/commands/dump">Redis Documentation: DUMP</a>
     * @since redis 2.6.0
     */
    public byte[] dumpAsObj(String key) {
        return this.redisTemplate.dump(key);
    }

    /**
     * 获取序列化字符串
     *
     * @param key 键
     * @return 返回字节数组
     * @see <a href="http://redis.io/commands/dump">Redis Documentation: DUMP</a>
     * @since redis 2.6.0
     */
    public byte[] dump(String key) {
        return this.stringRedisTemplate.dump(key);
    }

    /**
     * 序列化存储对象
     *
     * @param key     键
     * @param value   对象字节
     * @param timeout 过期时间
     * @param unit    单位
     * @param replace 是否替换
     * @see <a href="http://redis.io/commands/restore">Redis Documentation: RESTORE</a>
     * @since redis 2.6.0
     */
    public void restoreAsObj(String key, byte[] value, Long timeout, TimeUnit unit, boolean replace) {
        this.redisTemplate.restore(key, value, timeout, unit, replace);
    }

    /**
     * 序列化存储字符串
     *
     * @param key     键
     * @param value   字符串字节
     * @param timeout 过期时间
     * @param unit    单位
     * @param replace 是否替换
     * @see <a href="http://redis.io/commands/restore">Redis Documentation: RESTORE</a>
     * @since redis 2.6.0
     */
    public void restore(String key, byte[] value, Long timeout, TimeUnit unit, boolean replace) {
        this.stringRedisTemplate.restore(key, value, timeout, unit, replace);
    }

    /**
     * 对象最后访问时间更新
     *
     * @param keys 键
     * @return 返回更新数量
     * @see <a href="http://redis.io/commands/touch">Redis Documentation: TOUCH</a>
     * @since redis 3.2.1
     */
    public Long touchAsObj(String... keys) {
        return this.redisTemplate
                .getRequiredConnectionFactory()
                .getConnection()
                .keyCommands()
                .touch(ConvertUtil.toByteArray(this.redisTemplate.getKeySerializer(), keys));
    }

    /**
     * 字符串最后访问时间更新
     *
     * @param keys 键
     * @return 返回更新数量
     * @see <a href="http://redis.io/commands/touch">Redis Documentation: TOUCH</a>
     * @since redis 3.2.1
     */
    public Long touch(String... keys) {
        return this.stringRedisTemplate
                .getRequiredConnectionFactory()
                .getConnection()
                .keyCommands()
                .touch(ConvertUtil.toByteArray(this.stringRedisTemplate.getKeySerializer(), keys));
    }

    /**
     * 排序(对象)
     *
     * @param key 键
     * @return 返回排序后的列表
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public List sortAsObj(String key) {
        return this.redisTemplate.sort(
                SortQueryBuilder.sort(key)
                        .by("")
                        .order(SortParameters.Order.ASC)
                        .alphabetical(true)
                        .limit(0L, Long.MAX_VALUE)
                        .build()
        );
    }

    /**
     * 排序(字符串)
     *
     * @param key 键
     * @return 返回排序后的列表
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public List<String> sort(String key) {
        return this.stringRedisTemplate.sort(
                SortQueryBuilder.sort(key)
                        .by("")
                        .order(SortParameters.Order.ASC)
                        .alphabetical(true)
                        .limit(0L, Long.MAX_VALUE)
                        .build()
        );
    }

    /**
     * 排序并覆盖(对象)
     *
     * @param key 键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndCoverAsObj(String key) {
        return this.sortAndStoreAsObj(key, key);
    }

    /**
     * 排序并覆盖(字符串)
     *
     * @param key 键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndCover(String key) {
        return this.sortAndStore(key, key);
    }

    /**
     * 排序并保存(对象)
     *
     * @param key      键
     * @param storeKey 保存键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndStoreAsObj(String key, String storeKey) {
        return this.redisTemplate.sort(
                SortQueryBuilder.sort(key)
                        .by("")
                        .order(SortParameters.Order.ASC)
                        .alphabetical(true)
                        .limit(0L, Long.MAX_VALUE)
                        .build(),
                storeKey
        );
    }

    /**
     * 排序并保存(字符串)
     *
     * @param key      键
     * @param storeKey 保存键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndStore(String key, String storeKey) {
        return this.stringRedisTemplate.sort(
                SortQueryBuilder.sort(key)
                        .by("")
                        .order(SortParameters.Order.ASC)
                        .alphabetical(true)
                        .limit(0L, Long.MAX_VALUE)
                        .build(),
                storeKey
        );
    }

    /**
     * 排序(对象)
     *
     * @param query 排序对象
     * @return 返回排序后的列表
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public List sortAsObj(SortQuery<String> query) {
        return this.redisTemplate.sort(query);
    }

    /**
     * 排序(字符串)
     *
     * @param query 排序对象
     * @return 返回排序后的列表
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public List<String> sort(SortQuery<String> query) {
        return this.stringRedisTemplate.sort(query);
    }

    /**
     * 排序并覆盖(对象)
     *
     * @param query 排序对象
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndCoverAsObj(SortQuery<String> query) {
        return this.sortAndStoreAsObj(query, query.getKey());
    }

    /**
     * 排序并覆盖(字符串)
     *
     * @param query 排序对象
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndCover(SortQuery<String> query) {
        return this.sortAndStore(query, query.getKey());
    }

    /**
     * 排序并保存(对象)
     *
     * @param query    排序对象
     * @param storeKey 保存键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndStoreAsObj(SortQuery<String> query, String storeKey) {
        return this.redisTemplate.sort(query, storeKey);
    }

    /**
     * 排序并保存(字符串)
     *
     * @param query    排序对象
     * @param storeKey 保存键
     * @return 返回保存的列表长度
     * @see <a href="http://redis.io/commands/sort">Redis Documentation: SORT</a>
     * @since redis 1.0.0
     */
    public Long sortAndStore(SortQuery<String> query, String storeKey) {
        return this.stringRedisTemplate.sort(query, storeKey);
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
