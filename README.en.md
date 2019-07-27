# spring-boot-starter-redisson
[![](https://img.shields.io/badge/Java-v1.8-green)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![](https://img.shields.io/maven-central/v/com.naah69/spring-boot-starter-redisson)](https://search.maven.org/artifact/com.naah69/spring-boot-starter-redisson/1.0.0/jar)
[![](https://img.shields.io/github/license/naah69/spring-boot-starter-redisson)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/github/release-pre/naah69/spring-boot-starter-redisson)](https://github.com/naah69/spring-boot-starter-redisson/releases)

[\[中文\]](https://github.com/naah69/spring-boot-starter-redisson/blob/master/README.md) [\[English\]](https://github.com/naah69/spring-boot-starter-redisson/blob/master/README.en.md)

## 1 Introduce
`RedisTemplate` and `StringRedisTemplate` cooperate with `Redisson`,use it easily,provide the more friendly and perfect API for you!

The biggest advantage is the omission of the `redisson.yaml` config file

This project is devolped based on [xsxgit/redis-spring-boot-starter](https://gitee.com/xsxgit/redis-spring-boot-starter)（Standing on the shoulders of giants）

## 2 Dependencies
[![](https://img.shields.io/badge/spring--boot--starter--parent-v2.1.5.RELEASE-blue)](https://github.com/spring-projects/spring-boot/blob/v2.1.5.RELEASE/spring-boot-project/spring-boot-parent/pom.xml)

[![](https://img.shields.io/badge/spring--boot--starter--data--redis-v2.1.5.RELEASE-blue)](https://github.com/spring-projects/spring-boot/blob/v2.1.5.RELEASE/spring-boot-project/spring-boot-starters/spring-boot-starter-data-redis/pom.xml)

[![](https://img.shields.io/badge/redisson--spring--data--21-v3.11.0-blue)](https://github.com/redisson/redisson/tree/master/redisson-spring-data/redisson-spring-data-21)

## 3 Current Version

[![](https://img.shields.io/maven-central/v/com.naah69/spring-boot-starter-redisson)](https://search.maven.org/artifact/com.naah69/spring-boot-starter-redisson/1.0.0/jar)

```xml
<dependency>
    <groupId>com.naah69</groupId>
    <artifactId>spring-boot-starter-redisson</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 4 Getting Started

### 4.1 Preparation
The code of this demo is in  `spring-boot-starter-redisson-demo`

1. Maven Dependency：

```xml
<dependency>
    <groupId>com.naah69</groupId>
    <artifactId>spring-boot-starter-redisson</artifactId>
    <version>1.0.0</version>
</dependency>
```
2. Redis Configuration：

yml：
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

### 4.2 Started
The following order is the order that is recommended

 1. RedisTemplate

```java
@Autowired
private StringRedisTemplate redisTemplate;

@Test
public void test() {
    //get client
    ValueOperations<String, String> redisClient = redisTemplate.opsForValue();

    //key
    String key = "RedisTemplateTest";

    //delete
    redisTemplate.delete(key);

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(key, value);

    //verify
    Assert.assertEquals(redisClient.get(key), value);
}
```

2. RedisUtils

```java
@Test
public void test() {
    //get client
    StringHandler redisClient = RedisUtil.getStringHandler();

    //key
    String key = "RedisUtilsTest";

    //delete
    redisClient.remove(key);

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(key, value);

    //verify
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

    //get client
    RBucket<String> redisClient = redissonClient.getBucket(key);

    //delete
    redisClient.delete();

    //value
    long current = System.currentTimeMillis();
    String value = current + "";

    //set
    redisClient.set(value);

    //verify
    Assert.assertEquals(redisClient.get(), value);
}
```



### 4.3 Custom Compont Named RedisUtils Specification

get database instance：
```java
// get default database instance(DB)
DBHandler dbHandler = RedisUtil.getDBHandler();
...

// get the database instance that is second index(DB)
DBHandler dbHandler = RedisUtil.getDBHandler(2);
...
```

| Instance | Data Type | Get Way |
| :------: | :------: | :------: |
| NumberHandler | Number | RedisUtil.getNumberHandler()<br>RedisUtil.getNumberHandler(dbIndex) |
| StringHandler | String | RedisUtil.getStringHandler()<br>RedisUtil.getStringHandler(dbIndex) |
| HashHandler | Hash | RedisUtil.getHashHandler()<br>RedisUtil.getHashHandler(dbIndex) |
| SetHandler | Set | RedisUtil.getSetHandler()<br>RedisUtil.getSetHandler(dbIndex) |
| ZsetHandler | Zset | RedisUtil.getZsetHandler()<br>RedisUtil.getZsetHandler(dbIndex) |
| HyperLogLogHandler | HyperLogLog | RedisUtil.getHyperLogLogHandler()<br>RedisUtil.getHyperLogLogHandler(dbIndex) |
| BitmapHandler | Bitmap | RedisUtil.getBitmapHandler()<br>RedisUtil.getBitmapHandler(dbIndex) |
| GeoHandler | Geo | RedisUtil.getGeoHandler()<br>RedisUtil.getGeoHandler(dbIndex) |
| KeyHandler | Key | RedisUtil.getKeyHandler()<br>RedisUtil.getKeyHandler(dbIndex) |
| ScriptHandler | Lua Script | RedisUtil.getScriptHandler()<br>RedisUtil.getScriptHandler(dbIndex) |
| PubSubHandler | Pubsub | RedisUtil.getPubSubHandler()<br>RedisUtil.getPubSubHandler(dbIndex) |
| StreamHandler | Stream | RedisUtil.getStreamHandler()<br>RedisUtil.getStreamHandler(dbIndex)<br>RedisUtil.getStreamHandler(dbIndex, mapper) |
| DBHandler | DB | RedisUtil.getDBHandler()<br>RedisUtil.getDBHandler(dbIndex) |
| SentinelHandler | Sentinel | RedisUtil.getSentinelHandler()<br>RedisUtil.getSentinelHandler(dbIndex) |
| ClusterHandler | Cluster | RedisUtil.getClusterHandler() |
| CustomCommandHandler | CustomCommand | RedisUtil.getCustomCommandHandler()<br>RedisUtil.getCustomCommandHandler(dbIndex) |
| RedisLockHandler | Lock | RedisUtil.getRedisLockHandler()<br>RedisUtil.getRedisLockHandler(dbIndex) |
| TransactionHandler | Transaction | RedisUtil.getTransactionHandler()<br>RedisUtil.getTransactionHandler(dbIndex) |

### 4.4 Transaction Sample
```java
List execute = RedisUtil.getTransactionHandler(2).execute(handler -> {
    // open monitoring
    handler.watch("xx", "test");
    // open transaction
    handler.beginTransaction();
    // get the string handler
    StringHandler stringHandler = handler.getStringHandler();
    // operate
    stringHandler.set("xx", "hello");
    stringHandler.append("xx", "world");
    stringHandler.append("xx", "!");
    // get the number handler
    NumberHandler numberHandler = handler.getNumberHandler();
    numberHandler.addLong("test", 100);
    numberHandler.incrementLong("test");
    numberHandler.incrementLong("test");
    numberHandler.incrementLong("test");
    // commit
    return handler.commit();
});
```
