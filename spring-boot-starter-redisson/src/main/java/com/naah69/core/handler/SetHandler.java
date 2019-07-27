package com.naah69.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 无序集合助手
 *
 * @author xsx
 * @since 1.8
 */
public final class SetHandler implements RedisHandler {
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
    private SetOperations<String, Object> setOperations;
    /**
     * 字符串模板
     */
    private SetOperations<String, String> stringSetOperations;

    /**
     * 无序集合助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    SetHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.setOperations = redisTemplate.opsForSet();
        this.stringSetOperations = stringRedisTemplate.opsForSet();
    }

    /**
     * 新增对象
     *
     * @param key    键
     * @param values 对象
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/sadd">Redis Documentation: SADD</a>
     * @since redis 1.0.0
     */
    public Long addAsObj(String key, Object... values) {
        return this.setOperations.add(key, values);
    }

    /**
     * 新增字符串
     *
     * @param key    键
     * @param values 字符串
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/sadd">Redis Documentation: SADD</a>
     * @since redis 1.0.0
     */
    public Long add(String key, String... values) {
        return this.stringSetOperations.add(key, values);
    }

    /**
     * 弹出对象
     *
     * @param key 键
     * @param <T> 对象类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T popAsObj(String key) {
        return (T) this.setOperations.pop(key);
    }

    /**
     * 弹出对象
     *
     * @param type 返回值类型
     * @param key  键
     * @param <T>  返回类型
     * @return 返回对象
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since redis 1.0.0
     */
    @SuppressWarnings("unchecked")
    public <T> T popAsObj(Class<T> type, String key) {
        Object value = this.setOperations.pop(key);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 弹出字符串
     *
     * @param key 键
     * @return 返回字符串
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since redis 1.0.0
     */
    public String pop(String key) {
        return this.stringSetOperations.pop(key);
    }

    /**
     * 弹出对象
     *
     * @param key   键
     * @param count 对象个数
     * @return 返回对象列表
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since redis 3.2.0
     */
    public List popAsObj(String key, Long count) {
        return this.setOperations.pop(key, count);
    }

    /**
     * 弹出字符串
     *
     * @param key   键
     * @param count 字符串个数
     * @return 返回字符串列表
     * @see <a href="http://redis.io/commands/spop">Redis Documentation: SPOP</a>
     * @since redis 3.2.0
     */
    public List<String> pop(String key, Long count) {
        return this.stringSetOperations.pop(key, count);
    }

    /**
     * 移除对象
     *
     * @param key    键
     * @param values 对象
     * @return 返回移除对象数量
     * @see <a href="http://redis.io/commands/srem">Redis Documentation: SREM</a>
     * @since redis 1.0.0
     */
    public Long removeAsObj(String key, Object... values) {
        return this.setOperations.remove(key, values);
    }

    /**
     * 移除字符串
     *
     * @param key    键
     * @param values 字符串
     * @return 返回移除字符串数量
     * @see <a href="http://redis.io/commands/srem">Redis Documentation: SREM</a>
     * @since redis 1.0.0
     */
    public Long remove(String key, String... values) {
        return this.stringSetOperations.remove(key, (Object[]) values);
    }

    /**
     * 移动对象
     *
     * @param key     键
     * @param destKey 目标键
     * @param value   对象
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/smove">Redis Documentation: SMOVE</a>
     * @since redis 1.0.0
     */
    public Boolean moveAsObj(String key, String destKey, Object value) {
        return this.setOperations.move(key, value, destKey);
    }

    /**
     * 移动字符串
     *
     * @param key     键
     * @param destKey 目标键
     * @param value   字符串
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/smove">Redis Documentation: SMOVE</a>
     * @since redis 1.0.0
     */
    public Boolean move(String key, String destKey, String value) {
        return this.stringSetOperations.move(key, value, destKey);
    }

    /**
     * 获取对象数量
     *
     * @param key 键
     * @return 返回对象数量
     * @see <a href="http://redis.io/commands/scard">Redis Documentation: SCARD</a>
     * @since redis 1.0.0
     */
    public Long sizeAsObj(String key) {
        return this.setOperations.size(key);
    }

    /**
     * 获取字符串数量
     *
     * @param key 键
     * @return 返回字符串数量
     * @see <a href="http://redis.io/commands/scard">Redis Documentation: SCARD</a>
     * @since redis 1.0.0
     */
    public Long size(String key) {
        return this.stringSetOperations.size(key);
    }

    /**
     * 是否包含对象
     *
     * @param key   键
     * @param value 对象
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     * @since redis 1.0.0
     */
    public Boolean containsAsObj(String key, Object value) {
        return this.setOperations.isMember(key, value);
    }

    /**
     * 是否包含字符串
     *
     * @param key   键
     * @param value 字符串
     * @return 返回布尔值, 存在true, 不存在false
     * @see <a href="http://redis.io/commands/sismember">Redis Documentation: SISMEMBER</a>
     * @since redis 1.0.0
     */
    public Boolean contains(String key, String value) {
        return this.stringSetOperations.isMember(key, value);
    }

    /**
     * 获取不重复的随机对象
     *
     * @param key   键
     * @param count 数量
     * @return 返回不重复的随机对象集合
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    public Set distinctRandomMembersAsObj(String key, Long count) {
        return this.setOperations.distinctRandomMembers(key, count);
    }

    /**
     * 获取不重复的随机字符串
     *
     * @param key   键
     * @param count 数量
     * @return 返回不重复的随机字符串集合
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    public Set<String> distinctRandomMembers(String key, Long count) {
        return this.stringSetOperations.distinctRandomMembers(key, count);
    }

    /**
     * 获取可重复的随机对象
     *
     * @param key   键
     * @param count 数量
     * @return 返回不重复的随机对象集合
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    public List randomMembersAsObj(String key, Long count) {
        return this.setOperations.randomMembers(key, count);
    }

    /**
     * 获取可重复的随机字符串
     *
     * @param key   键
     * @param count 数量
     * @return 返回不重复的随机字符串集合
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    public List<String> randomMembers(String key, Long count) {
        return this.stringSetOperations.randomMembers(key, count);
    }

    /**
     * 获取可重复的随机对象
     *
     * @param key 键
     * @param <T> 对象类型
     * @return 返回可重复的随机对象
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    @SuppressWarnings("unchecked")
    public <T> T randomMemberAsObj(String key) {
        return (T) this.setOperations.randomMember(key);
    }

    /**
     * 获取可重复的随机对象
     *
     * @param type 返回值类型
     * @param key  键
     * @param <T>  返回类型
     * @return 返回可重复的随机对象
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    @SuppressWarnings("unchecked")
    public <T> T randomMemberAsObj(Class<T> type, String key) {
        Object value = this.setOperations.randomMember(key);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 获取可重复的随机字符串
     *
     * @param key 键
     * @return 返回可重复的随机字符串
     * @see <a href="http://redis.io/commands/srandmember">Redis Documentation: SRANDMEMBER</a>
     * @since redis 2.6.0
     */
    public String randomMember(String key) {
        return this.stringSetOperations.randomMember(key);
    }

    /**
     * 获取对象集合
     *
     * @param key 键
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
     * @since redis 1.0.0
     */
    public Set membersAsObj(String key) {
        return this.setOperations.members(key);
    }

    /**
     * 获取字符串集合
     *
     * @param key 键
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/smembers">Redis Documentation: SMEMBERS</a>
     * @since redis 1.0.0
     */
    public Set<String> members(String key) {
        return this.stringSetOperations.members(key);
    }

    /**
     * 取对象差集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的对象差集
     * @see <a href="http://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     * @since redis 1.0.0
     */
    public Set differenceAsObj(String key, String... otherKys) {
        return this.setOperations.difference(key, Arrays.asList(otherKys));
    }

    /**
     * 取字符串差集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的字符串差集
     * @see <a href="http://redis.io/commands/sdiff">Redis Documentation: SDIFF</a>
     * @since redis 1.0.0
     */
    public Set<String> difference(String key, String... otherKys) {
        return this.stringSetOperations.difference(key, Arrays.asList(otherKys));
    }

    /**
     * 取对象差集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回差集对象个数
     * @see <a href="http://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     * @since redis 1.0.0
     */
    public Long differenceAndStoreAsObj(String key, String storeKey, String... otherKys) {
        return this.setOperations.differenceAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取字符串差集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回差集字符串个数
     * @see <a href="http://redis.io/commands/sdiffstore">Redis Documentation: SDIFFSTORE</a>
     * @since redis 1.0.0
     */
    public Long differenceAndStore(String key, String storeKey, String... otherKys) {
        return this.stringSetOperations.differenceAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取对象交集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的对象交集
     * @see <a href="http://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     * @since redis 1.0.0
     */
    public Set intersectAsObj(String key, String... otherKys) {
        return this.setOperations.intersect(key, Arrays.asList(otherKys));
    }

    /**
     * 取字符串交集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的字符串交集
     * @see <a href="http://redis.io/commands/sinter">Redis Documentation: SINTER</a>
     * @since redis 1.0.0
     */
    public Set<String> intersect(String key, String... otherKys) {
        return this.stringSetOperations.intersect(key, Arrays.asList(otherKys));
    }

    /**
     * 取对象交集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     * @since redis 1.0.0
     */
    public Long intersectAndStoreAsObj(String key, String storeKey, String... otherKys) {
        return this.setOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取字符串交集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/sinterstore">Redis Documentation: SINTERSTORE</a>
     * @since redis 1.0.0
     */
    public Long intersectAndStore(String key, String storeKey, String... otherKys) {
        return this.stringSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取对象并集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的对象交集
     * @see <a href="http://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     * @since redis 1.0.0
     */
    public Set unionAsObj(String key, String... otherKys) {
        return this.setOperations.union(key, Arrays.asList(otherKys));
    }

    /**
     * 取字符串并集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回与其他集合的字符串交集
     * @see <a href="http://redis.io/commands/sunion">Redis Documentation: SUNION</a>
     * @since redis 1.0.0
     */
    public Set<String> union(String key, String... otherKys) {
        return this.stringSetOperations.union(key, Arrays.asList(otherKys));
    }

    /**
     * 取对象并集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回并集对象个数
     * @see <a href="http://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     * @since redis 1.0.0
     */
    public Long unionAndStoreAsObj(String key, String storeKey, String... otherKys) {
        return this.setOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取字符串并集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回并集字符串个数
     * @see <a href="http://redis.io/commands/sunionstore">Redis Documentation: SUNIONSTORE</a>
     * @since redis 1.0.0
     */
    public Long unionAndStore(String key, String storeKey, String... otherKys) {
        return this.stringSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 匹配对象
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配对象
     * @see <a href="http://redis.io/commands/sscan">Redis Documentation: SSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor scanAsObj(String key, Long count, String pattern) {
        return this.setOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
    }

    /**
     * 匹配字符串
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配字符串
     * @see <a href="http://redis.io/commands/sscan">Redis Documentation: SSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor<String> scan(String key, Long count, String pattern) {
        return this.stringSetOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
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
