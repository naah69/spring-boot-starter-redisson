# spring-boot-starter-redisson
[![](https://img.shields.io/badge/Java-v1.8-green)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![](https://img.shields.io/maven-central/v/com.naah69/spring-boot-starter-redisson)](https://search.maven.org/artifact/com.naah69/spring-boot-starter-redisson/1.0.0/jar)
[![](https://img.shields.io/github/license/naah69/spring-boot-starter-redisson)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/github/release/naah69/spring-boot-starter-redisson)](https://github.com/naah69/spring-boot-starter-redisson/releases)

[\[中文\]](https://github.com/naah69/spring-boot-starter-redisson/blob/master/README.md) [\[English\]](https://github.com/naah69/spring-boot-starter-redisson/blob/master/README.en.md)


## 1 介绍
`RedisTemplate`与`StringRedisTemplate`整合`Redisson`，开箱即用，提供更友好更完善的API，更方便的调用!

最大的优点是省略`redisson.yaml`配置文件

本项目是基于[xsxgit/redis-spring-boot-starter](https://gitee.com/xsxgit/redis-spring-boot-starter)改写的（站在巨人的肩膀上）

## 2 软件依赖
[![](https://img.shields.io/badge/spring--boot--starter--parent-v2.1.5.RELEASE-blue)](https://github.com/spring-projects/spring-boot/blob/v2.1.5.RELEASE/spring-boot-project/spring-boot-parent/pom.xml)

[![](https://img.shields.io/badge/spring--boot--starter--data--redis-v2.1.5.RELEASE-blue)](https://github.com/spring-projects/spring-boot/blob/v2.1.5.RELEASE/spring-boot-project/spring-boot-starters/spring-boot-starter-data-redis/pom.xml)

[![](https://img.shields.io/badge/redisson--spring--data--21-v3.11.0-blue)](https://github.com/redisson/redisson/tree/master/redisson-spring-data/redisson-spring-data-21)

## 3 当前版本

[![](https://img.shields.io/maven-central/v/com.naah69/spring-boot-starter-redisson)](https://search.maven.org/artifact/com.naah69/spring-boot-starter-redisson/1.0.0/jar)

```xml
<dependency>
    <groupId>com.naah69</groupId>
    <artifactId>spring-boot-starter-redisson</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 4 快速开始

### 4.1 准备工作
当前demo源码在本项目的`spring-boot-starter-redisson-demo`中

1. 添加依赖：

```xml
<dependency>
    <groupId>com.naah69</groupId>
    <artifactId>spring-boot-starter-redisson</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. redis配置：

yml方式：
```yaml
server:
  port: 6969
spring:
  profiles:
    active: local
---
spring:
  profiles: local
  redis:
    redisson:
      enable: true
      singleServerConfig:
        idleConnectionTimeout: 10000
        pingTimeout: 1000
        connectTimeout: 10000
        timeout: 3000
        retryAttempts: 3
        retryInterval: 1500
        failedSlaveReconnectionInterval: 3000
        failedSlaveCheckInterval: 3
        #        password: null
        subscriptionsPerConnection: 5
        clientName: "redisson-demo"
        address: "redis://127.0.0.1:6379"
        subscriptionConnectionMinimumIdleSize: 1
        subscriptionConnectionPoolSize: 50
        connectionMinimumIdleSize: 32
        connectionPoolSize: 64
        database: 0
      threads: 0
      nettyThreads: 0
      codec:
        class: "org.redisson.codec.JsonJacksonCodec"
      transportMode: "NIO"
---
spring:
  profiles: cluster
  redis:
    redisson:
      enable: true
      clusterServersConfig:
        idleConnectionTimeout: 10000
        pingTimeout: 1000
        connectTimeout: 10000
        timeout: 3000
        retryAttempts: 3
        retryInterval: 1500
        failedSlaveReconnectionInterval: 3000
        failedSlaveCheckInterval: 3
        password: 'password'
        subscriptionsPerConnection: 5
        clientName: "redisson-demo"
        loadBalancer:
          class: "org.redisson.connection.balancer.RoundRobinLoadBalancer"
        slaveSubscriptionConnectionMinimumIdleSize: 1
        slaveSubscriptionConnectionPoolSize: 50
        slaveConnectionMinimumIdleSize: 32
        slaveConnectionPoolSize: 64
        masterConnectionMinimumIdleSize: 32
        masterConnectionPoolSize: 64
        readMode: "SLAVE"
        nodeAddresses:
        - "redis://127.0.0.1:7110"
        - "redis://127.0.0.1:7111"
        - "redis://127.0.0.1:7112"
        - "redis://127.0.0.1:7113"
        scanInterval: 1000
      threads: 0
      nettyThreads: 0
      codec:
        class: "org.redisson.codec.JsonJacksonCodec"
      transportMode: "NIO"
```

### 4.2 开始使用
下面的顺序为推荐使用的顺序

 1. RedisTemplate

```java
@Autowired
private StringRedisTemplate redisTemplate;

@Test
public void test() {
    //获取客户端
    ValueOperations<String, String> redisClient = redisTemplate.opsForValue();

    //key
    String key = "RedisTemplateTest";

    //删除
    redisTemplate.delete(key);

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(key, value);

    //校验
    Assert.assertEquals(redisClient.get(key), value);
}
```

2. RedisUtils

```java
@Test
public void test() {
    //获取客户端
    StringHandler redisClient = RedisUtil.getStringHandler();

    //key
    String key = "RedisUtilsTest";

    //删除
    redisClient.remove(key);

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(key, value);

    //校验
    Assert.assertEquals(redisClient.get(key), value);
}
```

3. RedissonClient

```java
@Autowired
private RedissonClient redissonClient;

@Test
public void test() {
    //key
    String key = "RedissonClientTest";

    //获取客户端
    RBucket<String> redisClient = redissonClient.getBucket(key);

    //删除
    redisClient.delete();

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(value);

    //校验
    Assert.assertEquals(redisClient.get(), value);
}
```



### 4.3 自定义组件RedisUtils说明

获取操作实例：
```java
// 获取默认数据库实例(DB)
DBHandler dbHandler = RedisUtil.getDBHandler();
...

// 获取数据库索引为2的数据库实例(DB)
DBHandler dbHandler = RedisUtil.getDBHandler(2);
...
```

| 实例 | 数据类型 | 获取方式 |
| :------: | :------: | :------: |
| NumberHandler | 数字(Number) | RedisUtil.getNumberHandler()<br>RedisUtil.getNumberHandler(dbIndex) |
| StringHandler | 字符串(String) | RedisUtil.getStringHandler()<br>RedisUtil.getStringHandler(dbIndex) |
| HashHandler | 哈希(Hash) | RedisUtil.getHashHandler()<br>RedisUtil.getHashHandler(dbIndex) |
| SetHandler | 无序集合(Set) | RedisUtil.getSetHandler()<br>RedisUtil.getSetHandler(dbIndex) |
| ZsetHandler | 有序集合(Zset) | RedisUtil.getZsetHandler()<br>RedisUtil.getZsetHandler(dbIndex) |
| HyperLogLogHandler | 基数(HyperLogLog) | RedisUtil.getHyperLogLogHandler()<br>RedisUtil.getHyperLogLogHandler(dbIndex) |
| BitmapHandler | 位图(Bitmap) | RedisUtil.getBitmapHandler()<br>RedisUtil.getBitmapHandler(dbIndex) |
| GeoHandler | 地理位置(Geo) | RedisUtil.getGeoHandler()<br>RedisUtil.getGeoHandler(dbIndex) |
| KeyHandler | 键(Key) | RedisUtil.getKeyHandler()<br>RedisUtil.getKeyHandler(dbIndex) |
| ScriptHandler | 脚本(Lua Script) | RedisUtil.getScriptHandler()<br>RedisUtil.getScriptHandler(dbIndex) |
| PubSubHandler | 发布订阅(Pubsub) | RedisUtil.getPubSubHandler()<br>RedisUtil.getPubSubHandler(dbIndex) |
| StreamHandler | 流(Stream) | RedisUtil.getStreamHandler()<br>RedisUtil.getStreamHandler(dbIndex)<br>RedisUtil.getStreamHandler(dbIndex, mapper) |
| DBHandler | 数据库(DB) | RedisUtil.getDBHandler()<br>RedisUtil.getDBHandler(dbIndex) |
| SentinelHandler | 哨兵(Sentinel) | RedisUtil.getSentinelHandler()<br>RedisUtil.getSentinelHandler(dbIndex) |
| ClusterHandler | 集群(Cluster) | RedisUtil.getClusterHandler() |
| CustomCommandHandler | 自定义命令(CustomCommand) | RedisUtil.getCustomCommandHandler()<br>RedisUtil.getCustomCommandHandler(dbIndex) |
| RedisLockHandler | 分布式锁(Lock) | RedisUtil.getRedisLockHandler()<br>RedisUtil.getRedisLockHandler(dbIndex) |
| TransactionHandler | 事务(Transaction) | RedisUtil.getTransactionHandler()<br>RedisUtil.getTransactionHandler(dbIndex) |

### 4.4 事务使用示例
```java
List execute = RedisUtil.getTransactionHandler(2).execute(handler -> {
    // 开启监控
    handler.watch("xx", "test");
    // 开启事务
    handler.beginTransaction();
    // 获取对应事务字符串助手
    StringHandler stringHandler = handler.getStringHandler();
    // 执行操作
    stringHandler.set("xx", "hello");
    stringHandler.append("xx", "world");
    stringHandler.append("xx", "!");
    // 获取对应事务数字助手
    NumberHandler numberHandler = handler.getNumberHandler();
    numberHandler.addLong("test", 100);
    numberHandler.incrementLong("test");
    numberHandler.incrementLong("test");
    numberHandler.incrementLong("test");
    // 提交事务返回结果
    return handler.commit();
});
```
