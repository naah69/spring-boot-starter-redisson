package com.naah69.core.util;

import com.naah69.core.handler.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis工具
 *
 * @author xsx
 * @date 2019/4/8
 * @since 1.8
 */
public class RedisUtil {

    /**
     * 助手管理代理实例
     */
    private static final HandlerManagerProxy MANAGER = new HandlerManagerProxy();

    /**
     * 获取数据库助手
     *
     * @return 返回数据库助手
     */
    public static DBHandler getDBHandler() {
        return MANAGER.getHandler(HandlerType.DB);
    }

    /**
     * 获取数据库助手
     *
     * @param dbIndex 数据库索引
     * @return 返回数据库助手
     */
    public static DBHandler getDBHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.DB);
    }

    /**
     * 获取键助手
     *
     * @return 返回键助手
     */
    public static KeyHandler getKeyHandler() {
        return MANAGER.getHandler(HandlerType.KEY);
    }

    /**
     * 获取键助手
     *
     * @param dbIndex 数据库索引
     * @return 返回键助手
     */
    public static KeyHandler getKeyHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.KEY);
    }

    /**
     * 获取数字助手
     *
     * @return 返回数字助手
     */
    public static NumberHandler getNumberHandler() {
        return MANAGER.getHandler(HandlerType.NUMBER);
    }

    /**
     * 获取数字助手
     *
     * @param dbIndex 数据库索引
     * @return 返回数字助手
     */
    public static NumberHandler getNumberHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.NUMBER);
    }

    /**
     * 获取字符串助手
     *
     * @return 返回字符串助手
     */
    public static StringHandler getStringHandler() {
        return MANAGER.getHandler(HandlerType.STRING);
    }

    /**
     * 获取字符串助手
     *
     * @param dbIndex 数据库索引
     * @return 返回字符串助手
     */
    public static StringHandler getStringHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.STRING);
    }

    /**
     * 获取哈希助手
     *
     * @return 返回哈希助手
     */
    public static HashHandler getHashHandler() {
        return MANAGER.getHandler(HandlerType.HASH);
    }

    /**
     * 获取哈希助手
     *
     * @param dbIndex 数据库索引
     * @return 返回哈希助手
     */
    public static HashHandler getHashHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.HASH);
    }

    /**
     * 获取列表助手
     *
     * @return 返回列表助手
     */
    public static ListHandler getListHandler() {
        return MANAGER.getHandler(HandlerType.LIST);
    }

    /**
     * 获取列表助手
     *
     * @param dbIndex 数据库索引
     * @return 返回列表助手
     */
    public static ListHandler getListHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.LIST);
    }

    /**
     * 获取无序集合助手
     *
     * @return 返回无序集合助手
     */
    public static SetHandler getSetHandler() {
        return MANAGER.getHandler(HandlerType.SET);
    }

    /**
     * 获取无序集合助手
     *
     * @param dbIndex 数据库索引
     * @return 返回无序集合助手
     */
    public static SetHandler getSetHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.SET);
    }

    /**
     * 获取有序集合助手
     *
     * @return 返回有序集合助手
     */
    public static ZsetHandler getZsetHandler() {
        return MANAGER.getHandler(HandlerType.ZSET);
    }

    /**
     * 获取有序集合助手
     *
     * @param dbIndex 数据库索引
     * @return 返回有序集合助手
     */
    public static ZsetHandler getZsetHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.ZSET);
    }

    /**
     * 获取基数助手
     *
     * @return 返回基数助手
     */
    public static HyperLogLogHandler getHyperLogLogHandler() {
        return MANAGER.getHandler(HandlerType.HYPERLOGLOG);
    }

    /**
     * 获取基数助手
     *
     * @param dbIndex 数据库索引
     * @return 返回基数助手
     */
    public static HyperLogLogHandler getHyperLogLogHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.HYPERLOGLOG);
    }

    /**
     * 获取位图助手
     *
     * @return 返回位图助手
     */
    public static BitmapHandler getBitmapHandler() {
        return MANAGER.getHandler(HandlerType.BITMAP);
    }

    /**
     * 获取位图助手
     *
     * @param dbIndex 数据库索引
     * @return 返回位图助手
     */
    public static BitmapHandler getBitmapHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.BITMAP);
    }

    /**
     * 获取地理位置助手
     *
     * @return 返回地理位置助手
     */
    public static GeoHandler getGeoHandler() {
        return MANAGER.getHandler(HandlerType.GEO);
    }

    /**
     * 获取地理位置助手
     *
     * @param dbIndex 数据库索引
     * @return 返回地理位置助手
     */
    public static GeoHandler getGeoHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.GEO);
    }

    /**
     * 获取lua脚本助手
     *
     * @return 返回lua脚本助手
     */
    public static ScriptHandler getScriptHandler() {
        return MANAGER.getHandler(HandlerType.SCRIPT);
    }

    /**
     * 获取lua脚本助手
     *
     * @param dbIndex 数据库索引
     * @return 返回lua脚本助手
     */
    public static ScriptHandler getScriptHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.SCRIPT);
    }

    /**
     * 获取发布订阅助手
     *
     * @return 返回发布订阅助手
     */
    public static PubSubHandler getPubSubHandler() {
        return MANAGER.getHandler(HandlerType.PUBSUB);
    }

    /**
     * 获取发布订阅助手
     *
     * @param dbIndex 数据库索引
     * @return 返回发布订阅助手
     */
    public static PubSubHandler getPubSubHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.PUBSUB);
    }

    /**
     * 获取分布式锁助手(需添加redisson依赖)
     *
     * @return 返回分布式锁助手
     */
    public static RedisLockHandler getRedisLockHandler() {
        return MANAGER.getHandler(HandlerType.REDISLOCK);
    }

    /**
     * 获取分布式锁助手(需添加redisson依赖)
     *
     * @param dbIndex 数据库索引
     * @return 返回分布式锁助手
     */
    public static RedisLockHandler getRedisLockHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.REDISLOCK);
    }

    /**
     * 获取哨兵助手
     *
     * @return 返回哨兵助手
     */
    public static SentinelHandler getSentinelHandler() {
        return MANAGER.getHandler(HandlerType.SENTINEL);
    }

    /**
     * 获取哨兵助手
     *
     * @param dbIndex 数据库索引
     * @return 返回哨兵助手
     */
    public static SentinelHandler getSentinelHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.SENTINEL);
    }

    /**
     * 获取集群助手
     *
     * @return 返回集群助手
     */
    public static ClusterHandler getClusterHandler() {
        return MANAGER.getHandler(HandlerType.CLUSTER);
    }

    /**
     * 获取自定义命令助手
     *
     * @return 返回自定义命令助手
     */
    public static CustomCommandHandler getCustomCommandHandler() {
        return MANAGER.getHandler(HandlerType.CUSTOMCOMMAND);
    }

    /**
     * 获取自定义命令助手
     *
     * @param dbIndex 数据库索引
     * @return 返回自定义命令助手
     */
    public static CustomCommandHandler getCustomCommandHandler(int dbIndex) {
        return MANAGER.getHandler(String.valueOf(dbIndex), HandlerType.CUSTOMCOMMAND);
    }

    /**
     * 获取默认的对象模板
     *
     * @return 返回对象模板
     */
    public static RedisTemplate<String, Object> getDefaultRedisTemplate() {
        return MANAGER.getDefaultRedisTemplate();
    }

    /**
     * 获取默认的字符串模板
     *
     * @return 返回字符串模板
     */
    public static StringRedisTemplate getDefaultStringRedisTemplate() {
        return MANAGER.getDefaultStringRedisTemplate();
    }
}
