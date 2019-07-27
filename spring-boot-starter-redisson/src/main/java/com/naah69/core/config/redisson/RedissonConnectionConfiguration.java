package com.naah69.core.config.redisson;

import org.redisson.cluster.ClusterConnectionManager;
import org.redisson.config.*;
import org.redisson.connection.*;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.UUID;

/**
 * redisson配置类
 * 重写org.redisson.config.RedissonConnectionConfiguration
 *
 * @author xsx
 * @since 1.8
 */
public class RedissonConnectionConfiguration extends Config implements RedisConfiguration {
    /**
     * 哨兵配置
     */
    private SentinelServersConfig sentinelServersConfig;
    /**
     * 主从配置
     */
    private MasterSlaveServersConfig masterSlaveServersConfig;
    /**
     * 单机配置
     */
    private SingleServerConfig singleServerConfig;
    /**
     * 集群配置
     */
    private ClusterServersConfig clusterServersConfig;
    /**
     * 云托管配置
     */
    private ReplicatedServersConfig replicatedServersConfig;
    /**
     * 连接管理器
     */
    private ConnectionManager connectionManager;
    /**
     * 数据库索引
     */
    private int database = 0;

    /**
     * 无参构造
     */
    public RedissonConnectionConfiguration() {
    }

    /**
     * 获取哨兵配置
     *
     * @return 返回哨兵配置
     */
    public SentinelServersConfig getSentinelServersConfig() {
        return sentinelServersConfig;
    }

    /**
     * 设置哨兵配置
     *
     * @param sentinelServersConfig 哨兵配置
     */
    public void setSentinelServersConfig(SentinelServersConfig sentinelServersConfig) {
        this.sentinelServersConfig = sentinelServersConfig;
    }

    /**
     * 获取主从配置
     *
     * @return 返回主从配置
     */
    public MasterSlaveServersConfig getMasterSlaveServersConfig() {
        return masterSlaveServersConfig;
    }

    /**
     * 设置主从配置
     *
     * @param masterSlaveServersConfig 主从配置
     */
    public void setMasterSlaveServersConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
        this.masterSlaveServersConfig = masterSlaveServersConfig;
    }

    /**
     * 获取单机配置
     *
     * @return 返回单机配置
     */
    public SingleServerConfig getSingleServerConfig() {
        return singleServerConfig;
    }

    /**
     * 设置单机配置
     *
     * @param singleServerConfig 单机配置
     */
    public void setSingleServerConfig(SingleServerConfig singleServerConfig) {
        this.singleServerConfig = singleServerConfig;
    }

    /**
     * 获取集群配置
     *
     * @return 返回集群配置
     */
    public ClusterServersConfig getClusterServersConfig() {
        return clusterServersConfig;
    }

    /**
     * 设置集群配置
     *
     * @param clusterServersConfig 集群配置
     */
    public void setClusterServersConfig(ClusterServersConfig clusterServersConfig) {
        this.clusterServersConfig = clusterServersConfig;
    }

    /**
     * 获取云托管配置
     *
     * @return 返回云托管配置
     */
    public ReplicatedServersConfig getReplicatedServersConfig() {
        return replicatedServersConfig;
    }

    /**
     * 设置云托管配置
     *
     * @param replicatedServersConfig 云托管配置
     */
    public void setReplicatedServersConfig(ReplicatedServersConfig replicatedServersConfig) {
        this.replicatedServersConfig = replicatedServersConfig;
    }

    /**
     * 获取连接管理器
     *
     * @return 返回连接管理器
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * 设置连接管理器
     *
     * @param connectionManager 连接管理器
     */
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * 使用自定义模式
     *
     * @param connectionManager 连接管理器
     */
    @Override
    public void useCustomServers(ConnectionManager connectionManager) {
        super.useCustomServers(connectionManager);
        this.connectionManager = connectionManager;
        this.initDatabase();
    }

    /**
     * 获取配置(json格式)
     *
     * @param content 配置内容
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(String content) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromJSON(content, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(json格式)
     *
     * @param inputStream 配置内容
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(InputStream inputStream) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromJSON(inputStream, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(json格式)
     *
     * @param file        配置文件
     * @param classLoader 类加载器
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(File file, ClassLoader classLoader) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromJSON(file, RedissonConnectionConfiguration.class, classLoader);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(json格式)
     *
     * @param file 配置文件
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(File file) throws IOException {
        return fromJSON(file, null);
    }

    /**
     * 获取配置(json格式)
     *
     * @param url 配置文件路径
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(URL url) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromJSON(url, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(json格式)
     *
     * @param reader 配置文件读取器
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromJSON(Reader reader) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromJSON(reader, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 配置转为字符串
     *
     * @return 返回字符串
     * @throws IOException IOException
     */
    @Override
    public String toJSON() throws IOException {
        return new ConfigSupport().toJSON(this);
    }

    /**
     * 获取配置(yml格式)
     *
     * @param content 配置内容
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(String content) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromYAML(content, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(yml格式)
     *
     * @param inputStream 配置内容
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(InputStream inputStream) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromYAML(inputStream, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(yml格式)
     *
     * @param file 配置文件
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(File file) throws IOException {
        return fromYAML(file, null);
    }

    /**
     * 获取配置(yml格式)
     *
     * @param file        配置文件
     * @param classLoader 类加载器
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(File file, ClassLoader classLoader) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromYAML(file, RedissonConnectionConfiguration.class, classLoader);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(yml格式)
     *
     * @param url 配置文件路径
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(URL url) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromYAML(url, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 获取配置(yml格式)
     *
     * @param reader 配置文件读取器
     * @return 返回配置
     * @throws IOException IOException
     */
    public static RedissonConnectionConfiguration fromYAML(Reader reader) throws IOException {
        RedissonConnectionConfiguration configuration = new ConfigSupport().fromYAML(reader, RedissonConnectionConfiguration.class);
        configuration.useCustomServers(createConnectionManager(configuration));
        return configuration;
    }

    /**
     * 配置转为字符串
     *
     * @return 返回字符串
     * @throws IOException IOException
     */
    @Override
    public String toYAML() throws IOException {
        return new ConfigSupport().toYAML(this);
    }

    /**
     * 创建连接管理器
     *
     * @param configCopy 配置
     * @return 返回连接管理器
     */
    public static ConnectionManager createConnectionManager(RedissonConnectionConfiguration configCopy) {
        UUID id = UUID.randomUUID();

        if (configCopy.getMasterSlaveServersConfig() != null) {
            validate(configCopy.getMasterSlaveServersConfig());
            return new MasterSlaveConnectionManager(configCopy.getMasterSlaveServersConfig(), configCopy, id);
        } else if (configCopy.getSingleServerConfig() != null) {
            validate(configCopy.getSingleServerConfig());
            return new SingleConnectionManager(configCopy.getSingleServerConfig(), configCopy, id);
        } else if (configCopy.getSentinelServersConfig() != null) {
            validate(configCopy.getSentinelServersConfig());
            return new SentinelConnectionManager(configCopy.getSentinelServersConfig(), configCopy, id);
        } else if (configCopy.getClusterServersConfig() != null) {
            validate(configCopy.getClusterServersConfig());
            return new ClusterConnectionManager(configCopy.getClusterServersConfig(), configCopy, id);
        } else if (configCopy.getReplicatedServersConfig() != null) {
            validate(configCopy.getReplicatedServersConfig());
            return new ReplicatedConnectionManager(configCopy.getReplicatedServersConfig(), configCopy, id);
        } else if (configCopy.getConnectionManager() != null) {
            return configCopy.getConnectionManager();
        } else {
            throw new IllegalArgumentException("server(s) address(es) not defined!");
        }
    }

    /**
     * 验证配置
     *
     * @param config 配置
     */
    private static void validate(SingleServerConfig config) {
        if (config.getConnectionPoolSize() < config.getConnectionMinimumIdleSize()) {
            throw new IllegalArgumentException("connectionPoolSize can't be lower than connectionMinimumIdleSize");
        }
    }

    /**
     * 验证配置
     *
     * @param config 配置
     */
    private static void validate(BaseMasterSlaveServersConfig<?> config) {
        if (config.getSlaveConnectionPoolSize() < config.getSlaveConnectionMinimumIdleSize()) {
            throw new IllegalArgumentException("slaveConnectionPoolSize can't be lower than slaveConnectionMinimumIdleSize");
        }
        if (config.getMasterConnectionPoolSize() < config.getMasterConnectionMinimumIdleSize()) {
            throw new IllegalArgumentException("masterConnectionPoolSize can't be lower than masterConnectionMinimumIdleSize");
        }
        if (config.getSubscriptionConnectionPoolSize() < config.getSubscriptionConnectionMinimumIdleSize()) {
            throw new IllegalArgumentException("slaveSubscriptionConnectionMinimumIdleSize can't be lower than slaveSubscriptionConnectionPoolSize");
        }
    }

    /**
     * 设置数据库索引
     *
     * @param index 数据库索引
     */
    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.database = index;
        if (this.isSentinelConfig()) {
            this.getSentinelServersConfig().setDatabase(this.database);
        } else if (this.isClusterConfig()) {
            this.database = 0;
        } else if (this.getReplicatedServersConfig() != null) {
            this.getReplicatedServersConfig().setDatabase(this.database);
        } else {
            if (this.getMasterSlaveServersConfig() != null) {
                this.getMasterSlaveServersConfig().setDatabase(this.database);
            } else {
                this.getSingleServerConfig().setDatabase(this.database);
            }
        }
    }

    /**
     * 获取数据库索引
     *
     * @return 返回数据库索引
     */
    public int getDatabase() {
        if (this.getSentinelServersConfig() != null) {
            this.database = this.getSentinelServersConfig().getDatabase();
        } else if (this.getClusterServersConfig() != null) {
            this.database = 0;
        } else if (this.getReplicatedServersConfig() != null) {
            this.database = this.getReplicatedServersConfig().getDatabase();
        } else {
            if (this.getMasterSlaveServersConfig() != null) {
                this.database = this.getMasterSlaveServersConfig().getDatabase();
            } else {
                this.database = this.getSingleServerConfig().getDatabase();
            }
        }
        return this.database;
    }

    /**
     * 初始化数据库索引
     */
    private void initDatabase() {
        this.getDatabase();
    }

}
