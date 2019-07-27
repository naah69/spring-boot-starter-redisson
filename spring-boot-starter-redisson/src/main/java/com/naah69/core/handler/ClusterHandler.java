package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterCommands;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集群助手
 *
 * @author xsx
 * @date 2019/5/31
 * @since 1.8
 */
public final class ClusterHandler implements RedisHandler {
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
    ClusterHandler(Integer dbIndex) {
        this(HandlerManager.createRedisTemplate(dbIndex));
    }

    /**
     * 数据库助手构造
     *
     * @param redisTemplate 对象模板
     */
    @SuppressWarnings("unchecked")
    ClusterHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.connectionFactory = this.redisTemplate.getRequiredConnectionFactory();
    }

    /**
     * 集群信息
     *
     * @return 返回集群信息
     * @see <a href="http://redis.io/commands/cluster-info">Redis Documentation: CLUSTER INFO</a>
     * @since redis 3.0.0
     */
    public ClusterInfo info() {
        return this.connectionFactory.getClusterConnection().clusterGetClusterInfo();
    }

    /**
     * 集群节点
     *
     * @return 返回集群节点
     * @see <a href="http://redis.io/commands/cluster-nodes">Redis Documentation: CLUSTER NODES</a>
     * @since redis 3.0.0
     */
    public Iterable<RedisClusterNode> nodes() {
        return this.connectionFactory.getClusterConnection().clusterGetNodes();
    }

    /**
     * 从节点集合
     *
     * @param masterId 主节点id
     * @return 返回从节点集合
     * @see <a href="http://redis.io/commands/cluster-slaves">Redis Documentation: CLUSTER SLAVES</a>
     * @since redis 3.0.0
     */
    public Collection<RedisClusterNode> slaves(String masterId) {
        return this.connectionFactory
                .getClusterConnection()
                .clusterGetSlaves(RedisClusterNode.newRedisClusterNode().withId(masterId).build());
    }

    /**
     * 主从节点字典
     *
     * @return 返回主从节点字典
     * @see <a href="http://redis.io/commands/cluster-slaves">Redis Documentation: CLUSTER SLAVES</a>
     * @since redis 3.0.0
     */
    public Map<RedisClusterNode, Collection<RedisClusterNode>> masterSlaves() {
        return this.connectionFactory.getClusterConnection().clusterGetMasterSlaveMap();
    }

    /**
     * 根据键获取节点
     *
     * @param key 键
     * @return 返回节点
     */
    public RedisClusterNode nodeForKey(String key) {
        return this.connectionFactory
                .getClusterConnection()
                .clusterGetNodeForKey(ConvertUtil.toBytes(this.redisTemplate.getKeySerializer(), key));
    }

    /**
     * 根据键获取节点
     *
     * @param slotIndex 槽索引
     * @return 返回节点
     */
    public RedisClusterNode nodeForSlot(int slotIndex) {
        return this.connectionFactory.getClusterConnection().clusterGetNodeForSlot(slotIndex);
    }

    /**
     * 连接集群节点
     *
     * @param clusterNode 集群节点
     * @see <a href="http://redis.io/commands/cluster-meet">Redis Documentation: CLUSTER MEET</a>
     * @since redis 3.0.0
     */
    public void meet(RedisClusterNode clusterNode) {
        this.connectionFactory.getClusterConnection().clusterMeet(clusterNode);
    }

    /**
     * 连接集群节点
     *
     * @param ip   服务器IP
     * @param port 服务器端口
     * @see <a href="http://redis.io/commands/cluster-meet">Redis Documentation: CLUSTER MEET</a>
     * @since redis 3.0.0
     */
    public void meet(String ip, String port) {
        this.connectionFactory
                .getClusterConnection()
                .clusterMeet(
                        RedisClusterNode.newRedisClusterNode().listeningAt(ip, Integer.valueOf(port)).build()
                );
    }

    /**
     * 移除节点
     *
     * @param clusterNode 节点
     * @see <a href="http://redis.io/commands/cluster-forget">Redis Documentation: CLUSTER FORGET</a>
     * @since redis 3.0.0
     */
    public void forget(RedisClusterNode clusterNode) {
        this.connectionFactory.getClusterConnection().clusterForget(clusterNode);
    }

    /**
     * 移除节点
     *
     * @param nodeId 节点id
     * @see <a href="http://redis.io/commands/cluster-forget">Redis Documentation: CLUSTER FORGET</a>
     * @since redis 3.0.0
     */
    public void forget(String nodeId) {
        this.connectionFactory
                .getClusterConnection()
                .clusterForget(
                        RedisClusterNode.newRedisClusterNode().withId(nodeId).build()
                );
    }

    /**
     * 设置从节点
     *
     * @param master  主节点
     * @param replica 从节点
     * @see <a href="http://redis.io/commands/cluster-replicate">Redis Documentation: CLUSTER REPLICATE</a>
     * @since redis 3.0.0
     */
    public void replicate(RedisClusterNode master, RedisClusterNode replica) {
        this.connectionFactory.getClusterConnection().clusterReplicate(master, replica);
    }

    /**
     * 添加槽
     *
     * @param clusterNode 节点
     * @param slotIndex   槽索引
     * @see <a href="http://redis.io/commands/cluster-addslots">Redis Documentation: CLUSTER ADDSLOTS</a>
     * @since redis 3.0.0
     */
    public void addSlot(RedisClusterNode clusterNode, int... slotIndex) {
        this.connectionFactory.getClusterConnection().clusterAddSlots(clusterNode, slotIndex);
    }

    /**
     * 添加槽
     *
     * @param clusterNode    节点
     * @param beginSlotIndex 槽开始索引
     * @param endSlotIndex   槽结束索引
     * @see <a href="http://redis.io/commands/cluster-addslots">Redis Documentation: CLUSTER ADDSLOTS</a>
     * @since redis 3.0.0
     */
    public void addSlotInRange(RedisClusterNode clusterNode, int beginSlotIndex, int endSlotIndex) {
        this.connectionFactory
                .getClusterConnection()
                .clusterAddSlots(clusterNode, new RedisClusterNode.SlotRange(beginSlotIndex, endSlotIndex));
    }

    /**
     * 移除槽
     *
     * @param clusterNode 节点
     * @param slotIndex   槽索引
     * @see <a href="http://redis.io/commands/cluster-delslots">Redis Documentation: CLUSTER DELSLOTS</a>
     * @since redis 3.0.0
     */
    public void removeSlot(RedisClusterNode clusterNode, int... slotIndex) {
        this.connectionFactory.getClusterConnection().clusterDeleteSlots(clusterNode, slotIndex);
    }

    /**
     * 移除槽
     *
     * @param clusterNode    节点
     * @param beginSlotIndex 槽开始索引
     * @param endSlotIndex   槽结束索引
     * @see <a href="http://redis.io/commands/cluster-delslots">Redis Documentation: CLUSTER DELSLOTS</a>
     * @since redis 3.0.0
     */
    public void removeSlotInRange(RedisClusterNode clusterNode, int beginSlotIndex, int endSlotIndex) {
        this.connectionFactory
                .getClusterConnection()
                .clusterDeleteSlotsInRange(clusterNode, new RedisClusterNode.SlotRange(beginSlotIndex, endSlotIndex));
    }

    /**
     * 设置槽
     *
     * @param node      节点
     * @param slotIndex 槽索引
     * @param mode      模式
     * @see <a href="http://redis.io/commands/cluster-setslot">Redis Documentation: CLUSTER SETSLOT</a>
     * @since redis 3.0.0
     */
    public void setSlot(RedisClusterNode node, int slotIndex, RedisClusterCommands.AddSlots mode) {
        this.connectionFactory.getClusterConnection().clusterSetSlot(node, slotIndex, mode);
    }

    /**
     * 键对应的槽
     *
     * @param key 键
     * @return 返回槽索引
     * @see <a href="http://redis.io/commands/cluster-keyslot">Redis Documentation: CLUSTER KEYSLOT</a>
     * @since redis 3.0.0
     */
    public Integer keySlot(String key) {
        return this.connectionFactory
                .getClusterConnection()
                .clusterGetSlotForKey(ConvertUtil.toBytes(this.redisTemplate.getKeySerializer(), key));
    }

    /**
     * 在指定槽中键的数量
     *
     * @param slotIndex 槽索引
     * @return 返回键的数量
     * @see <a href="http://redis.io/commands/cluster-countkeysinslot">Redis Documentation: CLUSTER COUNTKEYSINSLOT</a>
     * @since redis 3.0.0
     */
    public Long countKeyInSlot(int slotIndex) {
        return this.connectionFactory.getClusterConnection().clusterCountKeysInSlot(slotIndex);
    }

    /**
     * 在指定槽中键的列表
     *
     * @param slotIndex 槽索引
     * @param count     返回数量
     * @return 返回键的列表
     * @see <a href="http://redis.io/commands/cluster-getkeysinslot">Redis Documentation: CLUSTER GETKEYSINSLOT</a>
     * @since redis 3.0.0
     */
    public List<String> keysInSlot(int slotIndex, Integer count) {
        List<byte[]> byteList = this.connectionFactory.getClusterConnection().clusterGetKeysInSlot(slotIndex, count);
        if (byteList != null && byteList.size() > 0) {
            List<String> keys = new ArrayList<>(byteList.size());
            for (byte[] bytes : byteList) {
                keys.add(ConvertUtil.toStr(this.redisTemplate.getKeySerializer(), bytes));
            }
            return keys;
        }
        return new ArrayList<>(0);
    }
}
