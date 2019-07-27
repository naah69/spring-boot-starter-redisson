package com.naah69.core.config.redisson;

import com.alibaba.fastjson.JSONObject;
import org.redisson.config.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * redisson配置类
 * 重写org.redisson.spring.starter.RedissonProperties
 *
 * @author xsx
 * @author nayan
 * @date 2019-07-27 4:32 AM
 * @since 1.8
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

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
    private ClusterServersConfigs clusterServersConfig;
    /**
     * 云托管配置
     */
    private ReplicatedServersConfig replicatedServersConfig;

    private int threads;
    private int nettyThreads;
    private Map<String, String> codec;
    private TransportMode transportMode;


    public String toJSON() {
        return JSONObject.toJSONString(this);
    }

    public boolean judgeNull() {
        boolean isNull = true;
        if (sentinelServersConfig != null) {
            isNull = false;
        } else if (masterSlaveServersConfig != null) {
            isNull = false;
        } else if (singleServerConfig != null) {
            isNull = false;
        } else if (clusterServersConfig != null) {
            isNull = false;
        } else if (replicatedServersConfig != null) {
            isNull = false;
        }
        return isNull;
    }


    public SentinelServersConfig getSentinelServersConfig() {
        return sentinelServersConfig;
    }

    public void setSentinelServersConfig(SentinelServersConfig sentinelServersConfig) {
        this.sentinelServersConfig = sentinelServersConfig;
    }

    public MasterSlaveServersConfig getMasterSlaveServersConfig() {
        return masterSlaveServersConfig;
    }

    public void setMasterSlaveServersConfig(MasterSlaveServersConfig masterSlaveServersConfig) {
        this.masterSlaveServersConfig = masterSlaveServersConfig;
    }

    public SingleServerConfig getSingleServerConfig() {
        return singleServerConfig;
    }

    public void setSingleServerConfig(SingleServerConfig singleServerConfig) {
        this.singleServerConfig = singleServerConfig;
    }

    public ClusterServersConfigs getClusterServersConfig() {
        return clusterServersConfig;
    }

    public void setClusterServersConfig(ClusterServersConfigs clusterServersConfig) {
        this.clusterServersConfig = clusterServersConfig;
    }

    public ReplicatedServersConfig getReplicatedServersConfig() {
        return replicatedServersConfig;
    }

    public void setReplicatedServersConfig(ReplicatedServersConfig replicatedServersConfig) {
        this.replicatedServersConfig = replicatedServersConfig;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getNettyThreads() {
        return nettyThreads;
    }

    public void setNettyThreads(int nettyThreads) {
        this.nettyThreads = nettyThreads;
    }

    public Map<String, String> getCodec() {
        return codec;
    }

    public void setCodec(Map<String, String> codec) {
        this.codec = codec;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }
}
