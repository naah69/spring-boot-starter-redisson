package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * 位图助手
 *
 * @author xsx
 * @since 1.8
 */
public final class BitmapHandler implements RedisHandler {

    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 字符串命令
     */
    private RedisStringCommands commands;

    /**
     * 位图助手构造
     *
     * @param dbIndex 数据库索引
     */
    BitmapHandler(Integer dbIndex) {
        this.stringRedisTemplate = HandlerManager.createStringRedisTemplate(dbIndex);
        this.commands = this.stringRedisTemplate.getRequiredConnectionFactory().getConnection().stringCommands();
    }

    /**
     * 设置位
     *
     * @param key    键
     * @param offset 偏移量
     * @param bit    位值,true=1,false=0
     * @return 返回布尔值
     * @see <a href="http://redis.io/commands/setbit">Redis Documentation: SETBIT</a>
     * @since redis 2.2.0
     */
    public Boolean set(String key, Long offset, boolean bit) {
        return this.stringRedisTemplate.opsForValue().setBit(key, offset, bit);
    }

    /**
     * 获取位
     *
     * @param key    键
     * @param offset 偏移量
     * @return 返回布尔值
     * @see <a href="http://redis.io/commands/getbit">Redis Documentation: GETBIT</a>
     * @since redis 2.2.0
     */
    public Boolean get(String key, Long offset) {
        return this.stringRedisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 位长度
     *
     * @param key 键
     * @return 返回位的总长度
     * @see <a href="http://redis.io/commands/get">Redis Documentation: GET</a>
     * @since redis 2.2.0
     */
    public Long bitLength(String key) {
        String s = this.stringRedisTemplate.opsForValue().get(key);
        if (s == null) {
            return 0L;
        }
        return (long) (s.length() << 3);
    }

    /**
     * 二进制
     *
     * @param key 键
     * @return 返回二进制字符串
     * @see <a href="http://redis.io/commands/get">Redis Documentation: GET</a>
     * @since redis 2.2.0
     */
    public String binary(String key) {
        String value = this.stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        char[] chars = value.toCharArray();
        int count = chars.length;
        int length = count << 3;
        int bit = 8;
        StringBuilder builder = new StringBuilder(length);
        for (char aChar : chars) {
            for (int j = bit - 1; j >= 0; j--) {
                builder.append(aChar >>> j & 1);
            }
            builder.append(' ');
        }
        return builder.substring(0, length + count - 1);
    }

    /**
     * 统计
     *
     * @param key 键
     * @return 返回统计总数
     * @see <a href="http://redis.io/commands/bitcount">Redis Documentation: BITCOUNT</a>
     * @since redis 2.6.0
     */
    public Long count(String key) {
        return this.commands.bitCount(
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), key)
        );
    }

    /**
     * 统计
     *
     * @param key        键
     * @param startIndex 开始字节索引
     * @param endIndex   结束字节索引
     * @return 返回统计总数
     * @see <a href="http://redis.io/commands/bitcount">Redis Documentation: BITCOUNT</a>
     * @since redis 2.6.0
     */
    public Long count(String key, Long startIndex, Long endIndex) {
        final long startByteIndex = startIndex << 3;
        final long endByteIndex = endIndex << 3;
        return this.commands.bitCount(
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), key),
                startByteIndex,
                endByteIndex
        );
    }

    /**
     * 定位
     *
     * @param key 键
     * @param bit 位值,true=1,false=0
     * @return 返回二进制中首次出现的索引
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.8.7
     */
    public Long position(String key, boolean bit) {
        return this.commands.bitPos(
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), key),
                bit
        );
    }

    /**
     * 定位
     *
     * @param key        键
     * @param bit        位值,true=1,false=0
     * @param startIndex 开始字节索引
     * @param endIndex   结束字节索引
     * @return 返回二进制中首次出现的索引
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.8.7
     */
    public Long position(String key, boolean bit, Long startIndex, Long endIndex) {
        final Range<Long> range = Range.of(Range.Bound.inclusive(startIndex), Range.Bound.inclusive(endIndex));
        return this.commands.bitPos(
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), key),
                bit,
                range
        );
    }

    /**
     * 逻辑与
     *
     * @param storeKey 存储键
     * @param keys     键
     * @return 返回最长字符串长度
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.6.0
     */
    public Long bitOpWithAnd(String storeKey, String... keys) {
        final byte[][] keyBytes = ConvertUtil.toByteArray(RedisSerializer.string(), keys);
        return this.commands.bitOp(
                RedisStringCommands.BitOperation.AND,
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), storeKey),
                keyBytes
        );
    }

    /**
     * 逻辑或
     *
     * @param storeKey 存储键
     * @param keys     键
     * @return 返回最长字符串长度
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.6.0
     */
    public Long bitOpWithOr(String storeKey, String... keys) {
        final byte[][] keyBytes = ConvertUtil.toByteArray(RedisSerializer.string(), keys);
        return this.commands.bitOp(
                RedisStringCommands.BitOperation.OR,
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), storeKey),
                keyBytes
        );
    }

    /**
     * 逻辑异或
     *
     * @param storeKey 存储键
     * @param keys     键
     * @return 返回最长字符串长度
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.6.0
     */
    public Long bitOpWithXor(String storeKey, String... keys) {
        final byte[][] keyBytes = ConvertUtil.toByteArray(RedisSerializer.string(), keys);
        return this.commands.bitOp(
                RedisStringCommands.BitOperation.XOR,
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), storeKey),
                keyBytes
        );
    }

    /**
     * 逻辑非
     *
     * @param storeKey 存储键
     * @param keys     键
     * @return 返回最长字符串长度
     * @see <a href="http://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since redis 2.6.0
     */
    public Long bitOpWithNot(String storeKey, String... keys) {
        final byte[][] keyBytes = ConvertUtil.toByteArray(RedisSerializer.string(), keys);
        return this.commands.bitOp(
                RedisStringCommands.BitOperation.NOT,
                ConvertUtil.toBytes(this.stringRedisTemplate.getKeySerializer(), storeKey),
                keyBytes
        );
    }

    /**
     * 多位操作
     *
     * @param key      键
     * @param commands 指令
     * @return 返回指令结果列表
     * @see <a href="http://redis.io/commands/bitfield">Redis Documentation: BITFIELD</a>
     */
    public List<Long> bitField(String key, BitFieldSubCommands commands) {
        return this.stringRedisTemplate.opsForValue().bitField(key, commands);
    }

    /**
     * 移除字符串
     *
     * @param keys 键
     * @return 返回移除数量
     * @see <a href="http://redis.io/commands/del">Redis Documentation: DEL</a>
     * @since redis 1.0.0
     */
    public Long remove(String... keys) {
        return this.stringRedisTemplate.opsForValue().getOperations().delete(Arrays.asList(keys));
    }

    /**
     * 获取spring string redis模板
     *
     * @return 返回字符串模板
     */
    public StringRedisTemplate getStringRedisTemplate() {
        return this.stringRedisTemplate;
    }
}
