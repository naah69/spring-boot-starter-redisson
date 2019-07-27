package com.naah69.core.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * redis连接配置
 * 重写org.springframework.boot.autoconfigure.data.redis.AbstractRedisConnectionConfiguration
 *
 * @author xsx
 * @date 2019/5/7
 * @since 1.8
 */
public abstract class AbstractRedisConnectionConfiguration {
    private final RedisProperties properties;
    private final RedisSentinelConfiguration sentinelConfiguration;
    private final RedisClusterConfiguration clusterConfiguration;

    protected AbstractRedisConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider, ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
        this.properties = properties;
        this.sentinelConfiguration = sentinelConfigurationProvider.getIfAvailable();
        this.clusterConfiguration = clusterConfigurationProvider.getIfAvailable();
    }

    protected final RedisStandaloneConfiguration getStandaloneConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (StringUtils.hasText(this.properties.getUrl())) {
            AbstractRedisConnectionConfiguration.ConnectionInfo connectionInfo = this.parseUrl(this.properties.getUrl());
            config.setHostName(connectionInfo.getHostName());
            config.setPort(connectionInfo.getPort());
            config.setPassword(RedisPassword.of(connectionInfo.getPassword()));
        } else {
            config.setHostName(this.properties.getHost());
            config.setPort(this.properties.getPort());
            config.setPassword(RedisPassword.of(this.properties.getPassword()));
        }

        config.setDatabase(this.properties.getDatabase());
        return config;
    }

    protected final RedisSentinelConfiguration getSentinelConfig() {
        if (this.sentinelConfiguration != null) {
            return this.sentinelConfiguration;
        } else {
            RedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(this.createSentinels(sentinelProperties));
                if (this.properties.getPassword() != null) {
                    config.setPassword(RedisPassword.of(this.properties.getPassword()));
                }

                config.setDatabase(this.properties.getDatabase());
                return config;
            } else {
                return null;
            }
        }
    }

    protected final RedisClusterConfiguration getClusterConfiguration() {
        if (this.clusterConfiguration != null) {
            return this.clusterConfiguration;
        } else if (this.properties.getCluster() == null) {
            return null;
        } else {
            RedisProperties.Cluster clusterProperties = this.properties.getCluster();
            RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());
            if (clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }

            if (this.properties.getPassword() != null) {
                config.setPassword(RedisPassword.of(this.properties.getPassword()));
            }

            return config;
        }
    }

    private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<>();
        Iterator var3 = sentinel.getNodes().iterator();

        while (var3.hasNext()) {
            String node = (String) var3.next();

            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            } catch (RuntimeException var6) {
                throw new IllegalStateException("Invalid redis sentinel property '" + node + "'", var6);
            }
        }

        return nodes;
    }

    protected AbstractRedisConnectionConfiguration.ConnectionInfo parseUrl(String url) {
        try {
            URI uri = new URI(url);
            boolean useSsl = url.startsWith("rediss://");
            String password = null;
            if (uri.getUserInfo() != null) {
                password = uri.getUserInfo();
                int index = password.indexOf(58);
                if (index >= 0) {
                    password = password.substring(index + 1);
                }
            }

            return new AbstractRedisConnectionConfiguration.ConnectionInfo(uri, useSsl, password);
        } catch (URISyntaxException var6) {
            throw new IllegalArgumentException("Malformed url '" + url + "'", var6);
        }
    }

    protected static class ConnectionInfo {
        private final URI uri;
        private final boolean useSsl;
        private final String password;

        public ConnectionInfo(URI uri, boolean useSsl, String password) {
            this.uri = uri;
            this.useSsl = useSsl;
            this.password = password;
        }

        public boolean isUseSsl() {
            return this.useSsl;
        }

        public String getHostName() {
            return this.uri.getHost();
        }

        public int getPort() {
            return this.uri.getPort();
        }

        public String getPassword() {
            return this.password;
        }
    }
}