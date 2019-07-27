package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地理位置助手
 *
 * @author xsx
 * @date 2019/4/25
 * @since 1.8
 */
public final class GeoHandler implements RedisHandler {
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
    private GeoOperations<String, Object> geoOperations;
    /**
     * 字符串模板
     */
    private GeoOperations<String, String> stringGeoOperations;

    /**
     * 地理位置助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    GeoHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.geoOperations = redisTemplate.opsForGeo();
        this.stringGeoOperations = stringRedisTemplate.opsForGeo();
    }

    /**
     * 添加对象
     *
     * @param key   键
     * @param point 坐标
     * @param value 对象
     * @return 返回总数
     * @see <a href="http://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @since redis 3.2.0
     */
    public Long addAsObj(String key, Point point, Object value) {
        return this.geoOperations.add(key, point, value);
    }

    /**
     * 添加字符串
     *
     * @param key   键
     * @param point 坐标
     * @param value 字符串
     * @return 返回总数
     * @see <a href="http://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @since redis 3.2.0
     */
    public Long add(String key, Point point, String value) {
        return this.stringGeoOperations.add(key, point, value);
    }

    /**
     * 添加对象
     *
     * @param key    键
     * @param params 参数，键为待添加对象，值为待添加坐标
     * @return 返回总数
     * @see <a href="http://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @since redis 3.2.0
     */
    public Long addAsObj(String key, Map<Object, Point> params) {
        return this.geoOperations.add(key, params);
    }

    /**
     * 添加字符串
     *
     * @param key    键
     * @param params 参数，键为待添加字符串，值为待添加坐标
     * @return 返回总数
     * @see <a href="http://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
     * @since redis 3.2.0
     */
    public Long add(String key, Map<String, Point> params) {
        return this.stringGeoOperations.add(key, params);
    }

    /**
     * 定位对象
     *
     * @param key    键
     * @param values 对象
     * @return 返回坐标列表
     * @see <a href="http://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
     * @since redis 3.2.0
     */
    public List<Point> positionAsObj(String key, Object... values) {
        return this.geoOperations.position(key, values);
    }

    /**
     * 定位字符串
     *
     * @param key    键
     * @param values 字符串
     * @return 返回坐标列表
     * @see <a href="http://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
     * @since redis 3.2.0
     */
    public List<Point> position(String key, String... values) {
        return this.stringGeoOperations.position(key, values);
    }

    /**
     * 对象地理位置哈希码
     *
     * @param key    键
     * @param values 对象
     * @return 返回对象地理位置哈希码
     * @see <a href="http://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
     * @since redis 3.2.0
     */
    public List<String> hashAsObj(String key, Object... values) {
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        return this.geoOperations.hash(key, values);
    }

    /**
     * 字符串地理位置哈希码
     *
     * @param key    键
     * @param values 对象
     * @return 返回字符串地理位置哈希码
     * @see <a href="http://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
     * @since redis 3.2.0
     */
    public List<String> hash(String key, String... values) {
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        return this.stringGeoOperations.hash(key, values);
    }

    /**
     * 对象距离
     *
     * @param key     键
     * @param member1 对象1
     * @param member2 对象2
     * @return 返回对象间的距离
     * @see <a href="http://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @since redis 3.2.0
     */
    public Distance distanceAsObj(String key, Object member1, Object member2) {
        return this.geoOperations.distance(key, member1, member2);
    }

    /**
     * 字符串距离
     *
     * @param key     键
     * @param member1 字符串1
     * @param member2 字符串2
     * @return 返回字符串间的距离
     * @see <a href="http://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @since redis 3.2.0
     */
    public Distance distance(String key, String member1, String member2) {
        return this.stringGeoOperations.distance(key, member1, member2);
    }

    /**
     * 对象距离
     *
     * @param key     键
     * @param member1 对象1
     * @param member2 对象2
     * @param metric  单位
     * @return 返回对象间的距离
     * @see <a href="http://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @since redis 3.2.0
     */
    public Distance distanceAsObj(String key, Object member1, Object member2, Metric metric) {
        return this.geoOperations.distance(key, member1, member2, metric);
    }

    /**
     * 字符串距离
     *
     * @param key     键
     * @param member1 字符串1
     * @param member2 字符串2
     * @param metric  单位
     * @return 返回字符串间的距离
     * @see <a href="http://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
     * @since redis 3.2.0
     */
    public Distance distance(String key, String member1, String member2, Metric metric) {
        return this.stringGeoOperations.distance(key, member1, member2, metric);
    }

    /**
     * 中心范围内的对象
     *
     * @param key      键
     * @param value    对象
     * @param distance 距离
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<Object, Point> radiusByMemberAsObj(String key, Object value, Distance distance) {
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = this.geoOperations.radius(key, value, distance);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的字符串
     *
     * @param key      键
     * @param value    字符串
     * @param distance 距离
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<String, Point> radiusByMember(String key, String value, Distance distance) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.stringGeoOperations.radius(key, value, distance);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的对象
     *
     * @param key    键
     * @param value  对象
     * @param radius 半径
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<Object, Point> radiusByMemberAsObj(String key, Object value, Double radius) {
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = this.geoOperations.radius(key, value, radius);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的字符串
     *
     * @param key    键
     * @param value  字符串
     * @param radius 半径
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<String, Point> radiusByMember(String key, String value, Double radius) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.stringGeoOperations.radius(key, value, radius);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的对象
     *
     * @param key      键
     * @param value    对象
     * @param distance 距离
     * @param args     命令参数
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<Object, Point> radiusByMemberAsObj(String key, Object value, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = this.geoOperations.radius(key, value, distance, args);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的字符串
     *
     * @param key      键
     * @param value    字符串
     * @param distance 距离
     * @param args     命令参数
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public Map<String, Point> radiusByMember(String key, String value, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.stringGeoOperations.radius(key, value, distance, args);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的对象
     *
     * @param key    键
     * @param within 圆
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public Map<Object, Point> radiusAsObj(String key, Circle within) {
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = this.geoOperations.radius(key, within);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的字符串
     *
     * @param key    键
     * @param within 圆
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public Map<String, Point> radius(String key, Circle within) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.stringGeoOperations.radius(key, within);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的对象
     *
     * @param key    键
     * @param within 圆
     * @param args   命令参数
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public Map<Object, Point> radiusAsObj(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = this.geoOperations.radius(key, within, args);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的字符串
     *
     * @param key    键
     * @param within 圆
     * @param args   命令参数
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public Map<String, Point> radius(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = this.stringGeoOperations.radius(key, within, args);
        return ConvertUtil.toMap(results);
    }

    /**
     * 中心范围内的对象(带详细信息)
     *
     * @param key      键
     * @param value    对象
     * @param distance 距离
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusByMemberWithResultAsObj(String key, Object value, Distance distance) {
        return this.geoOperations.radius(key, value, distance);
    }

    /**
     * 中心范围内的字符串(带详细信息)
     *
     * @param key      键
     * @param value    字符串
     * @param distance 距离
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMemberWithResult(String key, String value, Distance distance) {
        return this.stringGeoOperations.radius(key, value, distance);
    }

    /**
     * 中心范围内的对象(带详细信息)
     *
     * @param key    键
     * @param value  对象
     * @param radius 半径
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusByMemberAsObjWithResult(String key, Object value, Double radius) {
        return this.geoOperations.radius(key, value, radius);
    }

    /**
     * 中心范围内的字符串(带详细信息)
     *
     * @param key    键
     * @param value  字符串
     * @param radius 半径
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMemberWithResult(String key, String value, Double radius) {
        return this.stringGeoOperations.radius(key, value, radius);
    }

    /**
     * 中心范围内的对象(带详细信息)
     *
     * @param key      键
     * @param value    对象
     * @param distance 距离
     * @param args     命令参数
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusByMemberAsObjWithResult(String key, Object value, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return this.geoOperations.radius(key, value, distance, args);
    }

    /**
     * 中心范围内的字符串(带详细信息)
     *
     * @param key      键
     * @param value    字符串
     * @param distance 距离
     * @param args     命令参数
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMemberWithResult(String key, String value, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return this.stringGeoOperations.radius(key, value, distance, args);
    }

    /**
     * 中心范围内的对象(带详细信息)
     *
     * @param key    键
     * @param within 圆
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusAsObjWithResult(String key, Circle within) {
        return this.geoOperations.radius(key, within);
    }

    /**
     * 中心范围内的字符串(带详细信息)
     *
     * @param key    键
     * @param within 圆
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusWithResult(String key, Circle within) {
        return this.stringGeoOperations.radius(key, within);
    }

    /**
     * 中心范围内的对象(带详细信息)
     *
     * @param key    键
     * @param within 圆
     * @param args   命令参数
     * @return 返回包含的对象
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusAsObjWithResult(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return this.geoOperations.radius(key, within, args);
    }

    /**
     * 中心范围内的字符串(带详细信息)
     *
     * @param key    键
     * @param within 圆
     * @param args   命令参数
     * @return 返回包含的字符串
     * @see <a href="http://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
     * @since redis 3.2.0
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusWithResult(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return this.stringGeoOperations.radius(key, within, args);
    }

    /**
     * 移除对象
     *
     * @param key     键
     * @param members 对象
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     * @since redis 2.4.0
     */
    public Long removeAsObj(String key, String... members) {
        return this.geoOperations.remove(key, (Object[]) members);
    }

    /**
     * 移除字符串
     *
     * @param key     键
     * @param members 字符串
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/zrem">Redis Documentation: ZREM</a>
     * @since redis 2.4.0
     */
    public Long remove(String key, String... members) {
        return this.stringGeoOperations.remove(key, members);
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
