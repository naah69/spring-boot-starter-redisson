package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

import java.util.*;

/**
 * 有序集合助手
 *
 * @author xsx
 * @date 2019/4/15
 * @since 1.8
 */
public final class ZsetHandler implements RedisHandler {
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
    private ZSetOperations<String, Object> zSetOperations;
    /**
     * 字符串模板
     */
    private ZSetOperations<String, String> stringZSetOperations;

    /**
     * 有序集合助手
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    ZsetHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.zSetOperations = redisTemplate.opsForZSet();
        this.stringZSetOperations = stringRedisTemplate.opsForZSet();
    }

    /**
     * 新增对象
     *
     * @param key   键
     * @param value 对象
     * @param score 排序
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Boolean addAsObj(String key, Object value, double score) {
        return this.zSetOperations.add(key, value, score);
    }

    /**
     * 新增字符串
     *
     * @param key   键
     * @param value 字符串
     * @param score 排序
     * @return 返回布尔值, 成功true, 失败false
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Boolean add(String key, String value, double score) {
        return this.stringZSetOperations.add(key, value, score);
    }

    /**
     * 新增对象存在则更新
     *
     * @param key 键
     * @param map 对象字典
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Long addAsObj(String key, Map<Double, Object> map) {
        return this.zSetOperations.add(key, ConvertUtil.toTypedTupleSet(map));
    }

    /**
     * 新增字符串存在则更新
     *
     * @param key 键
     * @param map 字符串字典
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Long add(String key, Map<Double, String> map) {
        return this.stringZSetOperations.add(key, ConvertUtil.toTypedTupleSet(map));
    }

    /**
     * 新增对象存在则更新
     *
     * @param key    键
     * @param values 对象
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Long addAsObj(String key, Object... values) {
        return this.addAsObj(key, ConvertUtil.toMap(values));
    }

    /**
     * 新增字符串存在则更新
     *
     * @param key    键
     * @param values 字符串
     * @return 返回成功个数
     * @see <a href="http://redis.io/commands/zadd">Redis Documentation: ZADD</a>
     * @since redis 1.2.0
     */
    public Long add(String key, String... values) {
        return this.add(key, ConvertUtil.toMap(values));
    }

    /**
     * 获取对象数量
     *
     * @param key 键
     * @return 返回对象数量
     * @see <a href="http://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
     * @since redis 1.2.0
     */
    public Long sizeAsObj(String key) {
        return this.zSetOperations.zCard(key);
    }

    /**
     * 获取字符串数量
     *
     * @param key 键
     * @return 返回字符串数量
     * @see <a href="http://redis.io/commands/zcard">Redis Documentation: ZCARD</a>
     * @since redis 1.2.0
     */
    public Long size(String key) {
        return this.stringZSetOperations.zCard(key);
    }

    /**
     * 获取最小-最大之间分数的对象数量
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回对象数量
     * @see <a href="http://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
     * @since redis 1.2.0
     */
    public Long countAsObj(String key, Double min, Double max) {
        return this.zSetOperations.count(key, min, max);
    }

    /**
     * 获取最小-最大之间分数的字符串数量
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回字符串数量
     * @see <a href="http://redis.io/commands/zcount">Redis Documentation: ZCOUNT</a>
     * @since redis 1.2.0
     */
    public Long count(String key, Double min, Double max) {
        return this.stringZSetOperations.count(key, min, max);
    }

    /**
     * 正序获取范围内的对象
     *
     * @param key        键
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set ascRangeAsObj(String key, Long startIndex, Long endIndex) {
        return this.zSetOperations.range(key, startIndex, endIndex);
    }

    /**
     * 正序获取范围内的字符串
     *
     * @param key        键
     * @param startIndex 开始索引
     * @param endIndex   结束索引
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set<String> ascRange(String key, Long startIndex, Long endIndex) {
        return this.stringZSetOperations.range(key, startIndex, endIndex);
    }

    /**
     * 倒序获取范围内的对象
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set descRangeAsObj(String key, Long startSortIndex, Long endSortIndex) {
        return this.ascRangeAsObj(key, -endSortIndex - 1, startSortIndex - 1);
    }

    /**
     * 倒序获取范围内的字符串
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set<String> descRange(String key, Long startSortIndex, Long endSortIndex) {
        return this.ascRange(key, -endSortIndex - 1, -startSortIndex - 1);
    }

    /**
     * 正序获取范围内的对象
     *
     * @param key             键
     * @param startSortIndex  起始排序索引
     * @param isContainsStart 是否包含起始
     * @param endSortIndex    结束排序索引
     * @param isContainsEnd   是否包含结束
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     * @since redis 2.8.9
     */
    public Set ascRangeByLexAsObj(String key, Long startSortIndex, boolean isContainsStart, Long endSortIndex, boolean isContainsEnd) {
        return this.zSetOperations.rangeByLex(
                key,
                this.getRange(startSortIndex, isContainsStart, endSortIndex, isContainsEnd)
        );
    }

    /**
     * 正序获取范围内的字符串
     *
     * @param key             键
     * @param startSortIndex  起始排序索引
     * @param isContainsStart 是否包含起始
     * @param endSortIndex    结束排序索引
     * @param isContainsEnd   是否包含结束
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     * @since redis 2.8.9
     */
    public Set<String> ascRangeByLex(String key, Long startSortIndex, boolean isContainsStart, Long endSortIndex, boolean isContainsEnd) {
        return this.stringZSetOperations.rangeByLex(
                key,
                this.getRange(startSortIndex, isContainsStart, endSortIndex, isContainsEnd)
        );
    }

    /**
     * 正序获取范围内的对象
     *
     * @param key             键
     * @param startSortIndex  起始排序索引
     * @param isContainsStart 是否包含起始
     * @param endSortIndex    结束排序索引
     * @param isContainsEnd   是否包含结束
     * @param count           返回数量
     * @param offset          偏移量
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     * @since redis 2.8.9
     */
    public Set ascRangeByLexAsObj(String key, Long startSortIndex, boolean isContainsStart, Long endSortIndex, boolean isContainsEnd, Integer count, Integer offset) {
        return this.zSetOperations.rangeByLex(
                key,
                this.getRange(startSortIndex, isContainsStart, endSortIndex, isContainsEnd),
                RedisZSetCommands.Limit.limit().count(count).offset(offset)
        );
    }

    /**
     * 正序获取范围内的字符串
     *
     * @param key             键
     * @param startSortIndex  起始排序索引
     * @param isContainsStart 是否包含起始
     * @param endSortIndex    结束排序索引
     * @param isContainsEnd   是否包含结束
     * @param count           返回数量
     * @param offset          偏移量
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrangebylex">Redis Documentation: ZRANGEBYLEX</a>
     * @since redis 2.8.9
     */
    public Set<String> ascRangeByLex(String key, Long startSortIndex, boolean isContainsStart, Long endSortIndex, boolean isContainsEnd, Integer count, Integer offset) {
        return this.stringZSetOperations.rangeByLex(
                key,
                this.getRange(startSortIndex, isContainsStart, endSortIndex, isContainsEnd),
                RedisZSetCommands.Limit.limit().count(count).offset(offset)
        );
    }

    /**
     * 获取范围内的对象
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Set rangeByScoreAsObj(String key, Double min, Double max, Long count, Long offset) {
        return this.zSetOperations.rangeByScore(key, min, max, offset, count);
    }

    /**
     * 获取范围内的字符串
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Set<String> rangeByScore(String key, Double min, Double max, Long count, Long offset) {
        return this.stringZSetOperations.rangeByScore(key, min, max, offset, count);
    }

    /**
     * 获取范围内的对象(带分数)
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, Object> rangeByScoreAsObj(String key, Long startSortIndex, Long endSortIndex) {
        return ConvertUtil.toMap(this.zSetOperations.rangeWithScores(key, startSortIndex, endSortIndex));
    }

    /**
     * 获取范围内的字符串(带分数)
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, String> rangeByScore(String key, Long startSortIndex, Long endSortIndex) {
        return ConvertUtil.toMap(this.stringZSetOperations.rangeWithScores(key, startSortIndex, endSortIndex));
    }

    /**
     * 获取范围内的对象(带分数)
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Map<Double, Object> rangeByScoreWithScoresAsObj(String key, Double min, Double max) {
        return ConvertUtil.toMap(this.zSetOperations.rangeByScoreWithScores(key, min, max));
    }

    /**
     * 获取范围内的字符串(带分数)
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Map<Double, String> rangeByScoreWithScores(String key, Double min, Double max) {
        return ConvertUtil.toMap(this.stringZSetOperations.rangeByScoreWithScores(key, min, max));
    }

    /**
     * 获取范围内的对象(带分数)
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Map<Double, Object> rangeByScoreWithScoresAsObj(String key, Double min, Double max, Long count, Long offset) {
        return ConvertUtil.toMap(this.zSetOperations.rangeByScoreWithScores(key, min, max, offset, count));
    }

    /**
     * 获取范围内的字符串(带分数)
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/zrangebyscore">Redis Documentation: ZRANGEBYSCORE</a>
     * @since redis 1.0.5
     */
    public Map<Double, String> rangeByScoreWithScores(String key, Double min, Double max, Long count, Long offset) {
        return ConvertUtil.toMap(this.stringZSetOperations.rangeByScoreWithScores(key, min, max, offset, count));
    }

    /**
     * 获取集合对象
     *
     * @param key 键
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set getAllAsObj(String key) {
        return this.ascRangeAsObj(key, 0L, -1L);
    }

    /**
     * 获取集合字符串
     *
     * @param key 键
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Set<String> getAll(String key) {
        return this.ascRange(key, 0L, -1L);
    }

    /**
     * 获取集合对象(带分数)
     *
     * @param key 键
     * @return 返回对象字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, Object> getAllByScoreAsObj(String key) {
        return this.rangeByScoreAsObj(key, 0L, -1L);
    }

    /**
     * 获取集合字符串(带分数)
     *
     * @param key 键
     * @return 返回字符串字典
     * @see <a href="http://redis.io/commands/zrange">Redis Documentation: ZRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, String> getAllByScore(String key) {
        return this.rangeByScore(key, 0L, -1L);
    }

    /**
     * 获取当前对象排序索引
     *
     * @param key   键
     * @param value 对象
     * @return 返回对象排序索引
     * @see <a href="http://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
     * @since redis 2.0.0
     */
    public Long sortIndexAsObj(String key, Object value) {
        return this.zSetOperations.rank(key, value);
    }

    /**
     * 获取当前字符串排序索引
     *
     * @param key   键
     * @param value 字符串
     * @return 返回字符串排序索引
     * @see <a href="http://redis.io/commands/zrank">Redis Documentation: ZRANK</a>
     * @since redis 2.0.0
     */
    public Long sortIndex(String key, String value) {
        return this.stringZSetOperations.rank(key, value);
    }

    /**
     * 当前对象分数
     *
     * @param key   键
     * @param value 对象
     * @return 返回对象分数
     * @see <a href="http://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
     * @since redis 1.2.0
     */
    public Double scoreAsObj(String key, Object value) {
        return this.zSetOperations.score(key, value);
    }

    /**
     * 当前字符串分数
     *
     * @param key   键
     * @param value 字符串
     * @return 返回字符串分数
     * @see <a href="http://redis.io/commands/zscore">Redis Documentation: ZSCORE</a>
     * @since redis 1.2.0
     */
    public Double score(String key, String value) {
        return this.stringZSetOperations.score(key, value);
    }

    /**
     * 对象分数自增
     *
     * @param key   键
     * @param value 对象
     * @param score 自增分数
     * @return 返回对象分数
     * @see <a href="http://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
     * @since redis 1.2.0
     */
    public Double incrementScoreAsObj(String key, Object value, Double score) {
        return this.zSetOperations.incrementScore(key, value, score);
    }

    /**
     * 字符串分数自增
     *
     * @param key   键
     * @param value 字符串
     * @param score 自增分数
     * @return 返回字符串分数
     * @see <a href="http://redis.io/commands/zincrby">Redis Documentation: ZINCRBY</a>
     * @since redis 1.2.0
     */
    public Double incrementScore(String key, String value, Double score) {
        return this.stringZSetOperations.incrementScore(key, value, score);
    }

    /**
     * 移除对象
     *
     * @param key    键
     * @param values 对象
     * @return 返回对象移除数量
     * @see <a href="http://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     * @since redis 1.2.0
     */
    public Long removeAsObj(String key, Object... values) {
        return this.zSetOperations.remove(key, values);
    }

    /**
     * 移除字符串
     *
     * @param key    键
     * @param values 字符串
     * @return 返回字符串移除数量
     * @see <a href="http://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     * @since redis 1.2.0
     */
    public Long remove(String key, String... values) {
        return this.stringZSetOperations.remove(key, (Object[]) values);
    }

    /**
     * 正序移除范围内的对象
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回对象移除数量
     * @see <a href="http://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     * @since redis 2.0.0
     */
    public Long ascRemoveRangeAsObj(String key, Long startSortIndex, Long endSortIndex) {
        return this.zSetOperations.removeRange(key, startSortIndex, endSortIndex);
    }

    /**
     * 正序移除范围内的字符串
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回字符串移除数量
     * @see <a href="http://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     * @since redis 2.0.0
     */
    public Long ascRemoveRange(String key, Long startSortIndex, Long endSortIndex) {
        return this.stringZSetOperations.removeRange(key, startSortIndex, endSortIndex);
    }

    /**
     * 倒序移除范围内的对象
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回对象移除数量
     * @see <a href="http://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     * @since redis 2.0.0
     */
    public Long descRemoveRangeAsObj(String key, Long startSortIndex, Long endSortIndex) {
        return this.ascRemoveRangeAsObj(key, -endSortIndex - 1, -startSortIndex - 1);
    }

    /**
     * 倒序移除范围内的字符串
     *
     * @param key            键
     * @param startSortIndex 起始排序索引
     * @param endSortIndex   结束排序索引
     * @return 返回字符串移除数量
     * @see <a href="http://redis.io/commands/zremrangebyrank">Redis Documentation: ZREMRANGEBYRANK</a>
     * @since redis 2.0.0
     */
    public Long descRemoveRange(String key, Long startSortIndex, Long endSortIndex) {
        return this.ascRemoveRange(key, -endSortIndex - 1, -startSortIndex - 1);
    }

    /**
     * 移除范围内的对象
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回对象移除数量
     * @see <a href="http://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
     * @since redis 2.0.0
     */
    public Long removeRangeByScoreAsObj(String key, Double min, Double max) {
        return this.zSetOperations.removeRangeByScore(key, min, max);
    }

    /**
     * 移除范围内的字符串
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回字符串移除数量
     * @see <a href="http://redis.io/commands/zremrangebyscore">Redis Documentation: ZREMRANGEBYSCORE</a>
     * @since redis 2.0.0
     */
    public Long removeRangeByScore(String key, Double min, Double max) {
        return this.stringZSetOperations.removeRangeByScore(key, min, max);
    }

    /**
     * 取对象交集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStoreAsObj(String key, String storeKey, String... otherKys) {
        return this.zSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取字符串交集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStore(String key, String storeKey, String... otherKys) {
        return this.stringZSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取对象交集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param otherKys  其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStoreAsObj(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, String... otherKys) {
        return this.zSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey, aggregate);
    }

    /**
     * 取字符串交集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param weights   权重选项
     * @param otherKys  其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStore(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights, String... otherKys) {
        return this.stringZSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey, aggregate, weights);
    }

    /**
     * 取对象交集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param weights   权重选项
     * @param otherKys  其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStoreAsObj(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights, String... otherKys) {
        return this.zSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey, aggregate, weights);
    }

    /**
     * 取字符串交集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param otherKys  其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Long intersectAndStore(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, Double weight, String... otherKys) {
        return this.stringZSetOperations.intersectAndStore(key, Arrays.asList(otherKys), storeKey, aggregate);
    }

    /**
     * 取对象交集(带分数)
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Map<Double, Object> intersectByScoreAsObj(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.intersectAndStoreAsObj(key, tempKey, otherKys);
        Map<Double, Object> map = this.getAllByScoreAsObj(tempKey);
        this.redisTemplate.delete(tempKey);
        return map;
    }

    /**
     * 取字符串交集(带分数)
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Map<Double, String> intersectByScore(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.intersectAndStore(key, tempKey, otherKys);
        Map<Double, String> map = this.getAllByScore(tempKey);
        this.stringRedisTemplate.delete(tempKey);
        return map;
    }

    /**
     * 取对象交集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Set intersectAsObj(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.intersectAndStoreAsObj(key, tempKey, otherKys);
        Set set = this.getAllAsObj(tempKey);
        this.redisTemplate.delete(tempKey);
        return set;
    }

    /**
     * 取字符串交集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zinterstore">Redis Documentation: ZINTERSTORE</a>
     * @since redis 2.0.0
     */
    public Set<String> intersect(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.intersectAndStore(key, tempKey, otherKys);
        Set<String> set = this.getAll(tempKey);
        this.stringRedisTemplate.delete(tempKey);
        return set;
    }

    /**
     * 取对象并集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStoreAsObj(String key, String storeKey, String... otherKys) {
        return this.zSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取字符串并集并存储到新的集合
     *
     * @param key      键
     * @param storeKey 存储键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStore(String key, String storeKey, String... otherKys) {
        return this.stringZSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey);
    }

    /**
     * 取对象并集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param otherKys  其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStoreAsObj(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, String... otherKys) {
        return this.zSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey, aggregate);
    }

    /**
     * 取字符串并集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param weights   权重选项
     * @param otherKys  其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStore(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights, String... otherKys) {
        return this.stringZSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey, aggregate, weights);
    }

    /**
     * 取对象并集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param weights   权重选项
     * @param otherKys  其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStoreAsObj(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights, String... otherKys) {
        return this.zSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey, aggregate, weights);
    }

    /**
     * 取字符串并集并存储到新的集合
     *
     * @param key       键
     * @param storeKey  存储键
     * @param aggregate 聚合选项
     * @param otherKys  其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Long unionAndStore(String key, String storeKey, RedisZSetCommands.Aggregate aggregate, Double weight, String... otherKys) {
        return this.stringZSetOperations.unionAndStore(key, Arrays.asList(otherKys), storeKey, aggregate);
    }

    /**
     * 取对象并集(带分数)
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Map<Double, Object> unionByScoreAsObj(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.unionAndStoreAsObj(key, tempKey, otherKys);
        Map<Double, Object> map = this.getAllByScoreAsObj(tempKey);
        this.redisTemplate.delete(tempKey);
        return map;
    }

    /**
     * 取字符串并集(带分数)
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Map<Double, String> unionByScore(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.unionAndStore(key, tempKey, otherKys);
        Map<Double, String> map = this.getAllByScore(tempKey);
        this.stringRedisTemplate.delete(tempKey);
        return map;
    }

    /**
     * 取对象并集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集对象个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Set unionAsObj(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.unionAndStore(key, tempKey, otherKys);
        Set set = this.getAllAsObj(tempKey);
        this.redisTemplate.delete(tempKey);
        return set;
    }

    /**
     * 取字符串并集
     *
     * @param key      键
     * @param otherKys 其他键
     * @return 返回交集字符串个数
     * @see <a href="http://redis.io/commands/zunionstore">Redis Documentation: ZUNIONSTORE</a>
     * @since redis 2.0.0
     */
    public Set<String> union(String key, String... otherKys) {
        String tempKey = UUID.randomUUID().toString();
        this.unionAndStore(key, tempKey, otherKys);
        Set<String> set = this.getAll(tempKey);
        this.stringRedisTemplate.delete(tempKey);
        return set;
    }

    /**
     * 反转当前对象排序索引
     *
     * @param key   键
     * @param value 对象
     * @return 返回对象排序索引
     * @see <a href="http://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
     * @since redis 2.0.0
     */
    public Long reverseSortIndexAsObj(String key, Object value) {
        return this.zSetOperations.reverseRank(key, value);
    }

    /**
     * 反转当前字符串排序索引
     *
     * @param key   键
     * @param value 字符串
     * @return 返回字符串排序索引
     * @see <a href="http://redis.io/commands/zrevrank">Redis Documentation: ZREVRANK</a>
     * @since redis 2.0.0
     */
    public Long reverseSortIndex(String key, String value) {
        return this.stringZSetOperations.reverseRank(key, value);
    }

    /**
     * 反转范围内的对象
     *
     * @param key        键
     * @param startIndex 起始排序索引
     * @param endIndex   结束排序索引
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Set reverseRangeAsObj(String key, Long startIndex, Long endIndex) {
        return this.zSetOperations.reverseRange(key, startIndex, endIndex);
    }

    /**
     * 反转范围内的字符串
     *
     * @param key        键
     * @param startIndex 起始排序索引
     * @param endIndex   结束排序索引
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Set<String> reverseRange(String key, Long startIndex, Long endIndex) {
        return this.stringZSetOperations.reverseRange(key, startIndex, endIndex);
    }

    /**
     * 反转范围内的对象
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Set reverseRangeByScoreAsObj(String key, Double min, Double max) {
        return this.zSetOperations.reverseRangeByScore(key, min, max);
    }

    /**
     * 反转范围内的字符串
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Set<String> reverseRangeByScore(String key, Double min, Double max) {
        return this.stringZSetOperations.reverseRangeByScore(key, min, max);
    }

    /**
     * 反转范围内的对象(带分数)
     *
     * @param key        键
     * @param startIndex 起始排序索引
     * @param endIndex   结束排序索引
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, Object> reverseRangeByScoreAsObj(String key, Long startIndex, Long endIndex) {
        return ConvertUtil.toMap(this.zSetOperations.reverseRangeWithScores(key, startIndex, endIndex));
    }

    /**
     * 反转范围内的字符串(带分数)
     *
     * @param key        键
     * @param startIndex 起始排序索引
     * @param endIndex   结束排序索引
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrevrange">Redis Documentation: ZREVRANGE</a>
     * @since redis 1.2.0
     */
    public Map<Double, String> reverseRangeByScore(String key, Long startIndex, Long endIndex) {
        return ConvertUtil.toMap(this.stringZSetOperations.reverseRangeWithScores(key, startIndex, endIndex));
    }

    /**
     * 反转范围内的对象(带分数)
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     * @since redis 2.2.0
     */
    public Map<Double, Object> reverseRangeByScoreWithScoresAsObj(String key, Double min, Double max) {
        return ConvertUtil.toMap(this.zSetOperations.reverseRangeByScoreWithScores(key, min, max));
    }

    /**
     * 反转范围内的字符串(带分数)
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     * @since redis 2.2.0
     */
    public Map<Double, String> reverseRangeByScoreWithScores(String key, Double min, Double max) {
        return ConvertUtil.toMap(this.stringZSetOperations.reverseRangeByScoreWithScores(key, min, max));
    }

    /**
     * 反转范围内的对象(带分数)
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回对象集合
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     * @since redis 2.2.0
     */
    public Map<Double, Object> reverseRangeByScoreWithScoresAsObj(String key, Double min, Double max, Long count, Long offset) {
        return ConvertUtil.toMap(this.zSetOperations.reverseRangeByScoreWithScores(key, min, max, offset, count));
    }

    /**
     * 反转范围内的字符串(带分数)
     *
     * @param key    键
     * @param min    最小分数
     * @param max    最大分数
     * @param count  返回数量
     * @param offset 偏移量
     * @return 返回字符串集合
     * @see <a href="http://redis.io/commands/zrevrangebyscore">Redis Documentation: ZREVRANGEBYSCORE</a>
     * @since redis 2.2.0
     */
    public Map<Double, String> reverseRangeByScoreWithScores(String key, Double min, Double max, Long count, Long offset) {
        return ConvertUtil.toMap(this.stringZSetOperations.reverseRangeByScoreWithScores(key, min, max, offset, count));
    }

    /**
     * 匹配对象
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配对象
     * @see <a href="http://redis.io/commands/zscan">Redis Documentation: ZSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor<ZSetOperations.TypedTuple<Object>> scanAsObj(String key, Long count, String pattern) {
        return this.zSetOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
    }

    /**
     * 匹配字符串
     *
     * @param key     键
     * @param count   数量
     * @param pattern 规则
     * @return 返回匹配字符串
     * @see <a href="http://redis.io/commands/zscan">Redis Documentation: ZSCAN</a>
     * @since redis 2.8.0
     */
    public Cursor<ZSetOperations.TypedTuple<String>> scan(String key, Long count, String pattern) {
        return this.stringZSetOperations.scan(key, ScanOptions.scanOptions().count(count).match(pattern).build());
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

    /**
     * 获取范围对象
     *
     * @param startSortIndex  起始排序索引
     * @param isContainsStart 是否包含起始
     * @param endSortIndex    结束排序索引
     * @param isContainsEnd   是否包含结束
     * @return 返回范围对象
     */
    private RedisZSetCommands.Range getRange(Long startSortIndex, boolean isContainsStart, Long endSortIndex, boolean isContainsEnd) {
        RedisZSetCommands.Range range = RedisZSetCommands.Range.range();
        if (isContainsStart) {
            range.gte(startSortIndex);
        } else {
            range.gt(startSortIndex);
        }
        if (isContainsEnd) {
            range.lte(endSortIndex);
        } else {
            range.lt(endSortIndex);
        }
        return range;
    }
}
