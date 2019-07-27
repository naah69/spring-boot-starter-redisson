package com.naah69.core.util;

import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 转换工具
 *
 * @author xsx
 * @date 2019/4/28
 * @since 1.8
 */
public class ConvertUtil {

    /**
     * 转为二维字节数组
     *
     * @param serializer 序列化器
     * @param keys       键
     * @return 返回二位字节数组
     */
    public static byte[][] toByteArray(RedisSerializer serializer, String... keys) {
        byte[][] bytes = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            bytes[i] = toBytes(serializer, keys[i]);
        }
        return bytes;
    }

    /**
     * 转为二维字节数组
     *
     * @param keySerializer  键序列化器
     * @param argsSerializer 参数序列化器
     * @param keys           键列表
     * @param args           参数列表
     * @param <K>            键类型
     * @return 返回键和参数的二维数组
     */
    @SuppressWarnings("unchecked")
    public static <K> byte[][] toByteArray(RedisSerializer keySerializer, RedisSerializer argsSerializer, List<K> keys, Object[] args) {
        Assert.notNull(keySerializer, "keySerializer must not be null");
        Assert.notNull(argsSerializer, "argsSerializer must not be null");

        final int keySize = keys != null ? keys.size() : 0;
        byte[][] keysAndArgs = new byte[args.length + keySize][];
        int i = 0;
        if (keys != null) {
            for (K key : keys) {
                if (key instanceof byte[]) {
                    keysAndArgs[i++] = (byte[]) key;
                } else {
                    keysAndArgs[i++] = keySerializer.serialize(key);
                }
            }
        }
        for (Object arg : args) {
            if (arg instanceof byte[]) {
                keysAndArgs[i++] = (byte[]) arg;
            } else {
                keysAndArgs[i++] = argsSerializer.serialize(arg);
            }
        }
        return keysAndArgs;
    }

    /**
     * 将字典转为TypedTuple类型的集合
     *
     * @param map 字典
     * @param <T> 对象类型
     * @return 返回TypedTuple类型的集合
     */
    public static <T> Set<ZSetOperations.TypedTuple<T>> toTypedTupleSet(Map<Double, T> map) {
        if (map == null) {
            return null;
        }
        Set<ZSetOperations.TypedTuple<T>> set = new HashSet<>(map.size());
        for (Map.Entry<Double, T> entry : map.entrySet()) {
            set.add(new DefaultTypedTuple<>(entry.getValue(), entry.getKey()));
        }
        return set;
    }

    /**
     * 转换为字典
     *
     * @param results 结果集
     * @param <T>     类型
     * @return 返回字典
     */
    public static <T> Map<T, Point> toMap(GeoResults<RedisGeoCommands.GeoLocation<T>> results) {
        Map<T, Point> map;
        if (results != null) {
            map = new HashMap<>(results.getContent().size());
            for (GeoResult<RedisGeoCommands.GeoLocation<T>> result : results) {
                map.put(result.getContent().getName(), result.getContent().getPoint());
            }
        } else {
            map = new HashMap<>(0);
        }
        return map;
    }

    /**
     * 将TypedTuple类型的集合转为字典
     *
     * @param set TypedTuple类型的集合
     * @param <T> 对象类型
     * @return 返回TypedTuple类型的集合
     */
    public static <T> Map<Double, T> toMap(Set<ZSetOperations.TypedTuple<T>> set) {
        if (set == null) {
            return null;
        }
        Map<Double, T> map = new LinkedHashMap<>(set.size());
        for (ZSetOperations.TypedTuple<T> typedTuple : set) {
            map.put(typedTuple.getScore(), typedTuple.getValue());
        }
        return map;
    }

    /**
     * 将对象转为字典
     *
     * @param values 对象
     * @param <T>    对象类型
     * @return 返回对象字典
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<Double, T> toMap(T... values) {
        if (values == null) {
            return null;
        }
        Map<Double, T> map = new HashMap<>(values.length);
        for (int i = 0; i < values.length; i++) {
            map.put((double) i, values[i]);
        }
        return map;
    }

    /**
     * 转为字节数组
     *
     * @param serializer 序列化器
     * @param value      值
     * @returns 返回字节数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] toBytes(RedisSerializer serializer, Object value) {
        Assert.notNull(serializer, "serializer must not be null");

        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return serializer.serialize(value);
    }

    /**
     * 转为字符串
     *
     * @param serializer 序列化器
     * @param bytes      字节数组
     * @return 返回字符串
     */
    public static String toStr(RedisSerializer serializer, byte[] bytes) {
        Assert.notNull(serializer, "serializer must not be null");

        Object o = serializer.deserialize(bytes);
        return o == null ? null : o.toString();
    }
}
