package com.naah69.core.handler;

import com.naah69.core.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 数据库助手
 *
 * @author xsx
 * @since 1.8
 */
public final class DBHandler implements RedisHandler {
    /**
     * 数据库信息选项枚举
     */
    public enum DBOption {
        /**
         * Redis服务器的一般信息
         */
        SERVER("server"),
        /**
         * 客户端的连接部分相关信息
         */
        CLIENTS("clients"),
        /**
         * 内存消耗相关信息
         */
        MEMORY("memory"),
        /**
         * RDB和AOF相关信息
         */
        PERSISTENCE("persistence"),
        /**
         * 一般统计信息
         */
        STATS("stats"),
        /**
         * 主/从复制信息
         */
        REPLICATION("replication"),
        /**
         * CPU的相关信息
         */
        CPU("cpu"),
        /**
         * Redis命令统计相关信息
         */
        COMMANDSTATS("commandstats"),
        /**
         * Redis集群信息
         */
        CLUSTER("cluster"),
        /**
         * 数据库的相关统计信息
         */
        KEYSPACE("keyspace"),
        /**
         * 所有信息
         */
        ALL("all"),
        /**
         * 默认设置的信息
         */
        DEFAULT("default");

        /**
         * 选项
         */
        private String option;

        /**
         * 选项枚举构造
         *
         * @param option 选项
         */
        DBOption(String option) {
            this.option = option;
        }
    }

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(DBHandler.class);
    /**
     * 时钟
     */
    private static final Clock CLOCK = Clock.systemDefaultZone();
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 数据库索引
     */
    private int dbIndex;

    /**
     * 数据库助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    DBHandler(Integer dbIndex) {
        this.dbIndex = dbIndex;
        this.redisTemplate = HandlerManager.createRedisTemplate(dbIndex);
    }

    /**
     * 获取当前数据库索引
     *
     * @return 返回当前数据库索引
     * @since redis 1.0.0
     */
    public int getDBIndex() {
        return this.dbIndex;
    }

    /**
     * 获取当前数据库信息
     *
     * @return 返回当前数据库信息
     * @see <a href="http://redis.io/commands/info">Redis Documentation: INFO</a>
     * @since redis 1.0.0
     */
    public Properties getDBInfo() {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands().info();
    }

    /**
     * 获取当前数据库信息
     *
     * @param dbOption 选项
     *                 <p>SERVER: Redis服务器的一般信息</p>
     *                 <p>CLIENTS: 客户端的连接部分相关信息</p>
     *                 <p>MEMORY: 内存消耗相关信息</p>
     *                 <p>PERSISTENCE: RDB和AOF相关信息</p>
     *                 <p>STATS: 一般统计信息</p>
     *                 <p>REPLICATION: 主/从复制信息</p>
     *                 <p>CPU: CPU的相关信息</p>
     *                 <p>COMMANDSTATS: Redis命令统计相关信息</p>
     *                 <p>CLUSTER: Redis集群信息</p>
     *                 <p>KEYSPACE: 数据库的相关统计信息</p>
     *                 <p>ALL: 所有信息</p>
     *                 <p>DEFAULT: 默认设置的信息</p>
     * @return 返回当前数据库信息
     * @see <a href="http://redis.io/commands/info">Redis Documentation: INFO</a>
     * @since redis 1.0.0
     */
    public Properties getDBInfo(DBOption dbOption) {
        RedisServerCommands serverCommands = this.redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands();
        if (dbOption != null) {
            return serverCommands.info(dbOption.option);
        }
        return serverCommands.info(DBOption.DEFAULT.option);
    }

    /**
     * 清理当前数据库
     *
     * @see <a href="http://redis.io/commands/flushdb">Redis Documentation: FLUSHDB</a>
     * @since redis 1.0.0
     */
    public void clearDB() {
        try {
            this.redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands().flushDb();
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 清理所有数据库
     *
     * @see <a href="http://redis.io/commands/flushall">Redis Documentation: FLUSHALL</a>
     * @since redis 1.0.0
     */
    public void clearDBAll() {
        try {
            this.redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands().flushAll();
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置数据库配置参数
     *
     * @param param 参数名
     * @param value 参数值
     * @see <a href="http://redis.io/commands/config-set">Redis Documentation: CONFIG SET</a>
     * @since redis 2.0.0
     */
    public void setConfig(String param, String value) {
        this.redisTemplate.getRequiredConnectionFactory().getConnection().setConfig(param, value);
    }

    /**
     * 获取数据库配置信息
     *
     * @param param 参数名
     * @return 返回数据库配置信息
     * @see <a href="http://redis.io/commands/config-get">Redis Documentation: CONFIG GET</a>
     * @since redis 2.0.0
     */
    public Properties getConfig(String param) {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().getConfig(param);
    }

    /**
     * 重置配置状态
     *
     * @see <a href="http://redis.io/commands/config-resetstat">Redis Documentation: CONFIG RESETSTAT</a>
     * @since redis 2.0.0
     */
    public void resetConfigStats() {
        this.redisTemplate.getRequiredConnectionFactory().getConnection().resetConfigStats();
    }

    /**
     * 设置客户端连接名称
     *
     * @param name 名称
     * @see <a href="http://redis.io/commands/client-setname">Redis Documentation: CLIENT SETNAME</a>
     * @since redis 2.6.9
     */
    public void setClientName(String name) {
        this.redisTemplate
                .getRequiredConnectionFactory()
                .getConnection()
                .setClientName(RedisSerializer.string().serialize(name));
    }

    /**
     * 获取客户端连接名称
     *
     * @return 返回客户端连接名称
     * @see <a href="http://redis.io/commands/client-getname">Redis Documentation: CLIENT GETNAME</a>
     * @since redis 2.6.9
     */
    public String getClientName() {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().getClientName();
    }

    /**
     * 获取客户端连接列表
     *
     * @return 返回客户端连接列表
     */
    public List<RedisClientInfo> getClientList() {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().getClientList();
    }

    /**
     * 关闭客户端
     *
     * @param ip   客户端IP
     * @param port 客户端端口
     */
    public void killClient(String ip, int port) {
        this.redisTemplate.killClient(ip, port);
    }

    /**
     * 角色的信息
     *
     * @return 返回当前是master，slave还是sentinel
     * @see <a href="http://redis.io/commands/role">Redis Documentation: ROLE</a>
     * @since redis 2.8.12
     */
    @SuppressWarnings("unchecked")
    public String getRole() {
        CustomCommandHandler commandHandler = RedisUtil.getCustomCommandHandler(this.dbIndex);
        List<Object> list = (List<Object>) commandHandler.executeCommand("ROLE", null);
        return commandHandler.deserialize((byte[]) list.get(0));
    }

    /**
     * 转为从服务器
     *
     * @param ip   主服务器IP
     * @param port 主服务器端口
     * @see <a href="http://redis.io/commands/slaveof">Redis Documentation: SLAVEOF</a>
     * @since redis 1.0.0
     */
    public void slaveOf(String ip, int port) {
        this.redisTemplate.slaveOf(ip, port);
    }

    /**
     * 转为主服务器
     *
     * @see <a href="http://redis.io/commands/slaveof">Redis Documentation: SLAVEOF NO ONE</a>
     * @since redis 1.0.0
     */
    public void slaveOfNoOne() {
        this.redisTemplate.slaveOfNoOne();
    }

    /**
     * 开始事务
     *
     * @see <a href="http://redis.io/commands/multi">Redis Documentation: MULTI</a>
     * @since redis 1.2.0
     */
    public void beginTransaction() {
        this.redisTemplate.multi();
    }

    /**
     * 提交事务
     *
     * @return 返回执行命令列表
     * @see <a href="http://redis.io/commands/exec">Redis Documentation: EXEC</a>
     * @since redis 1.2.0
     */
    public List commit() {
        return this.redisTemplate.exec();
    }

    /**
     * 取消事务
     *
     * @see <a href="http://redis.io/commands/discard">Redis Documentation: DISCARD</a>
     * @since redis 2.0.0
     */
    public void cancelTransaction() {
        this.redisTemplate.discard();
    }

    /**
     * 监控
     *
     * @param keys 键
     * @see <a href="http://redis.io/commands/watch">Redis Documentation: WATCH</a>
     * @since redis 2.2.0
     */
    public void watch(String... keys) {
        this.redisTemplate.watch(Arrays.asList(keys));
    }

    /**
     * 取消监控
     *
     * @see <a href="http://redis.io/commands/unwatch">Redis Documentation: UNWATCH</a>
     * @since redis 2.2.0
     */
    public void unwatch() {
        this.redisTemplate.unwatch();
    }

    /**
     * 异步AOF文件重写
     *
     * @see <a href="http://redis.io/commands/bgrewriteaof">Redis Documentation: BGREWRITEAOF</a>
     * @since redis 1.0.0
     */
    public void bgReWriteAof() {
        this.redisTemplate.getRequiredConnectionFactory().getConnection().bgReWriteAof();
    }

    /**
     * 非阻塞式同步
     *
     * @see <a href="http://redis.io/commands/bgsave">Redis Documentation: BGSAVE</a>
     * @since redis 1.0.0
     */
    public void bgSave() {
        this.redisTemplate.getRequiredConnectionFactory().getConnection().bgSave();
    }

    /**
     * 阻塞式同步
     *
     * @see <a href="http://redis.io/commands/save">Redis Documentation: SAVE</a>
     * @since redis 1.0.0
     */
    public void save() {
        this.redisTemplate.getRequiredConnectionFactory().getConnection().save();
    }

    /**
     * 最近一次保存的时间戳
     *
     * @return 返回时间戳
     * @see <a href="http://redis.io/commands/lastsave">Redis Documentation: LASTSAVE</a>
     * @since redis 1.0.0
     */
    public Long lastSave() {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().lastSave();
    }

    /**
     * 检查连接
     *
     * @return 返回延迟时间(ms)
     * @see <a href="http://redis.io/commands/ping">Redis Documentation: PING</a>
     * @since redis 1.0.0
     */
    public Long ping() {
        RedisConnection connection = this.redisTemplate.getRequiredConnectionFactory().getConnection();
        long begin = CLOCK.millis();
        connection.ping();
        long end = CLOCK.millis();
        return end - begin;
    }

    /**
     * 服务器时间
     *
     * @return 返回服务器时间(ms)
     * @see <a href="http://redis.io/commands/time">Redis Documentation: TIME</a>
     * @since redis 2.6.0
     */
    public Long time() {
        return this.redisTemplate.getRequiredConnectionFactory().getConnection().serverCommands().time();
    }

    /**
     * 测试打印
     *
     * @param message 信息
     * @return 返回打印信息
     * @see <a href="http://redis.io/commands/echo">Redis Documentation: ECHO</a>
     * @since redis 1.0.0
     */
    public String echo(String message) {
        RedisSerializer<String> serializer = RedisSerializer.string();
        return serializer.deserialize(
                this.redisTemplate
                        .getRequiredConnectionFactory()
                        .getConnection()
                        .echo(serializer.serialize(message))
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
