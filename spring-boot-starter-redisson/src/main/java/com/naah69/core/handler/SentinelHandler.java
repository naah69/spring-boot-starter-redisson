package com.naah69.core.handler;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Properties;

/**
 * 哨兵助手
 *
 * @author xsx
 * @date 2019/5/23
 * @since 1.8
 */
public final class SentinelHandler implements RedisHandler {
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 连接工厂
     */
    private RedisConnectionFactory connectionFactory;

    /**
     * 数据库助手构造
     *
     * @param dbIndex 数据库索引
     */
    SentinelHandler(Integer dbIndex) {
        this.redisTemplate = HandlerManager.createRedisTemplate(dbIndex);
        this.connectionFactory = this.redisTemplate.getRequiredConnectionFactory();
    }

    /**
     * 是否连接
     */
    public void isOpen() {
        this.connectionFactory.getSentinelConnection().isOpen();
    }

    /**
     * 故障转移
     *
     * @param masterName 主服务名称
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public void failover(String masterName) {
        this.connectionFactory.getSentinelConnection().failover(
                RedisNode.newRedisNode()
                        .withName(masterName)
                        .promotedAs(RedisNode.NodeType.MASTER)
                        .build()
        );
    }

    /**
     * 主服务器列表
     *
     * @return 返回主服务器列表
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public Collection<RedisServer> masters() {
        return this.connectionFactory.getSentinelConnection().masters();
    }

    /**
     * 从服务器列表
     *
     * @param masterName 主服务器名称
     * @return 返回从服务器列表
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public Collection<RedisServer> slaves(String masterName) {
        return this.connectionFactory.getSentinelConnection().slaves(
                RedisNode.newRedisNode()
                        .withName(masterName)
                        .promotedAs(RedisNode.NodeType.MASTER)
                        .build()
        );
    }

    /**
     * 监控主服务器
     *
     * @param master 主服务器对象
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public void monitor(RedisServer master) {
        this.connectionFactory.getSentinelConnection().monitor(master);
    }

    /**
     * 监控主服务器
     *
     * @param name   主服务器名称
     * @param ip     主服务器IP
     * @param port   主服务器端口
     * @param quorum 确认故障的最少哨兵数量
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public void monitor(String name, String ip, String port, String quorum) {
        Properties properties = new Properties();
        properties.setProperty("name", name);
        properties.setProperty("ip", ip);
        properties.setProperty("port", port);
        properties.setProperty("quorum", quorum);
        this.monitor(RedisServer.newServerFrom(properties));
    }

    /**
     * 移除主服务器
     *
     * @param masterName 主服务器名称
     * @see <a href="http://redis.io/topics/sentinel">Redis Sentinel Documentation</a>
     * @since redis 2.8
     */
    public void remove(String masterName) {
        this.connectionFactory.getSentinelConnection().remove(
                RedisNode.newRedisNode()
                        .withName(masterName)
                        .promotedAs(RedisNode.NodeType.MASTER)
                        .build()
        );
    }

    /**
     * 获取spring redis模板
     *
     * @return 返回对象模板
     */
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }
}
