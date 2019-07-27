package com.naah69.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.naah69.core.util.ConvertUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 自定义命令助手
 *
 * @author xsx
 * @date 2019/6/6
 * @since 1.8
 */
public final class CustomCommandHandler implements RedisHandler {
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 数据库助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    CustomCommandHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
    }

    /**
     * 执行redis对象命令
     *
     * @param command 命令
     * @param keys    键列表
     * @param args    参数列表
     * @return 返回信息
     */
    public Object executeCommandAsObj(String command, List<String> keys, Object... args) {
        Object result = this.redisTemplate.getRequiredConnectionFactory().getConnection().execute(
                command,
                ConvertUtil.toByteArray(
                        this.redisTemplate.getKeySerializer(),
                        this.redisTemplate.getValueSerializer(),
                        keys,
                        args
                )
        );
        return result instanceof byte[]
                ? this.redisTemplate.getValueSerializer().deserialize((byte[]) result)
                : result;
    }

    /**
     * 执行redis字符串命令
     *
     * @param command 命令
     * @param keys    键列表
     * @param args    参数列表
     * @return 返回信息
     */
    public Object executeCommand(String command, List<String> keys, Object... args) {
        Object result = this.stringRedisTemplate.getRequiredConnectionFactory().getConnection().execute(
                command,
                ConvertUtil.toByteArray(
                        this.stringRedisTemplate.getKeySerializer(),
                        this.stringRedisTemplate.getValueSerializer(),
                        keys,
                        args
                )
        );
        return result instanceof byte[]
                ? this.stringRedisTemplate.getValueSerializer().deserialize((byte[]) result)
                : result;
    }

    /**
     * 反序列化对象
     *
     * @param bytes 字节数组
     * @param <T>   返回类型
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    public <T> T deserializeAsObj(byte[] bytes) {
        return (T) this.redisTemplate.getValueSerializer().deserialize(bytes);
    }

    /**
     * 反序列化对象
     *
     * @param type  返回值类型
     * @param bytes 字节数组
     * @param <T>   返回类型
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    public <T> T deserializeAsObj(Class<T> type, byte[] bytes) {
        Object value = this.redisTemplate.getValueSerializer().deserialize(bytes);
        return value instanceof JSON ? JSONObject.toJavaObject((JSON) value, type) : (T) value;
    }

    /**
     * 反序列化字符串
     *
     * @param bytes 字节数组
     * @param <T>   返回类型
     * @return 返回对象
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes) {
        return (T) this.stringRedisTemplate.getValueSerializer().deserialize(bytes);
    }
}
