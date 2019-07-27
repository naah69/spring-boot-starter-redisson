package com.naah69.core.handler;

import com.naah69.core.config.redisson.RedissonClientHelper;
import org.redisson.api.*;

/**
 * 分布式锁助手(需添加redisson依赖)
 *
 * @author xsx
 * @date 2019/5/9
 * @since 1.8
 */
public final class RedisLockHandler implements RedisHandler {

    /**
     * redisson客户端
     */
    private RedissonClient redissonClient;

    /**
     * 分布式锁助手构造
     *
     * @param dbIndex 数据库索引
     */
    RedisLockHandler(Integer dbIndex) {
        this.redissonClient = RedissonClientHelper.createClient(dbIndex);
    }

    /**
     * 获取可重入锁
     *
     * @param name 名称
     * @return 返回可重入锁
     */
    public RLock getLock(String name) {
        return this.redissonClient.getLock(name);
    }

    /**
     * 获取公平锁
     *
     * @param name 名称
     * @return 返回公平锁
     */
    public RLock getFairLock(String name) {
        return this.redissonClient.getFairLock(name);
    }

    /**
     * 获取读写锁
     *
     * @param name 名称
     * @return 返回读写锁
     */
    public RReadWriteLock getReadWriteLock(String name) {
        return this.redissonClient.getReadWriteLock(name);
    }

    /**
     * 获取闭锁
     *
     * @param name 名称
     * @return 返回闭锁
     */
    public RCountDownLatch getCountDownLatch(String name) {
        return this.redissonClient.getCountDownLatch(name);
    }

    /**
     * 获取联锁
     *
     * @param locks 锁列表
     * @return 返回联锁
     */
    public RLock getMultiLock(RLock... locks) {
        return this.redissonClient.getMultiLock(locks);
    }

    /**
     * 获取红锁
     *
     * @param locks 锁列表
     * @return 返回红锁
     */
    public RLock getRedLock(RLock... locks) {
        return this.redissonClient.getRedLock(locks);
    }

    /**
     * 获取信号量
     *
     * @param name 名称
     * @return 返回信号量
     */
    public RSemaphore getSemaphore(String name) {
        return this.redissonClient.getSemaphore(name);
    }

    /**
     * 获取可过期信号量
     *
     * @param name 名称
     * @return 返回可过期信号量
     */
    public RPermitExpirableSemaphore getExpirableSemaphore(String name) {
        return this.redissonClient.getPermitExpirableSemaphore(name);
    }

    /**
     * 获取redisson客户端
     *
     * @return 返回redisson客户端
     */
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }
}
