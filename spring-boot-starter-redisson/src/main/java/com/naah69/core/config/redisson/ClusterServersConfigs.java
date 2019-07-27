package com.naah69.core.config.redisson;

import org.redisson.config.ClusterServersConfig;
import org.redisson.connection.balancer.LoadBalancer;

import java.util.Map;

/**
 * ClusterServersConfigs
 *
 * @author naah
 */
public class ClusterServersConfigs extends ClusterServersConfig {

    private Map<String, String> loadBalancer;

    @Override
    public LoadBalancer getLoadBalancer() {
        if (this.loadBalancer != null) {

            try {
                String classStr = this.loadBalancer.get("class");
                if (classStr != null && !classStr.equals("")) {
                    Class<?> clazz = Class.forName(classStr);
                    Object loadBalancer = clazz.newInstance();
                    if (loadBalancer instanceof LoadBalancer) {
                        return (LoadBalancer) loadBalancer;
                    }
                }

            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public void setLoadBalancer(Map<String, String> loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}
