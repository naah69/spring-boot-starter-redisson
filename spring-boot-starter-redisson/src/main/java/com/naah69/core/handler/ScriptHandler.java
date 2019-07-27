package com.naah69.core.handler;

import com.naah69.core.util.ConvertUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * lua脚本助手
 *
 * @author xsx
 * @date 2019/5/9
 * @since 1.8
 */
public final class ScriptHandler implements RedisHandler {

    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 对象脚本命令
     */
    private RedisScriptingCommands commands;
    /**
     * 字符串脚本命令
     */
    private RedisScriptingCommands stringCommands;

    /**
     * 键助手构造
     *
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    ScriptHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.commands = this.redisTemplate.getRequiredConnectionFactory().getConnection().scriptingCommands();
        this.stringCommands = this.stringRedisTemplate.getRequiredConnectionFactory().getConnection().scriptingCommands();
    }

    /**
     * 执行对象脚本
     *
     * @param scriptText lua脚本
     * @param resultType 返回类型
     * @param keys       键列表
     * @param args       参数列表
     * @param <T>        返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/eval">Redis Documentation: EVAL</a>
     * @since redis 2.6.0
     */
    public <T> T excuteAsObj(String scriptText, Class<T> resultType, List<String> keys, Object... args) {
        return this.redisTemplate.execute(this.buildScriptWithText(scriptText, resultType), keys, args);
    }

    /**
     * 执行字符串脚本
     *
     * @param scriptText lua脚本
     * @param resultType 返回类型
     * @param keys       键列表
     * @param args       参数列表
     * @param <T>        返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/eval">Redis Documentation: EVAL</a>
     * @since redis 2.6.0
     */
    public <T> T excute(String scriptText, Class<T> resultType, List<String> keys, Object... args) {
        return this.stringRedisTemplate.execute(this.buildScriptWithText(scriptText, resultType), keys, args);
    }

    /**
     * 执行对象脚本
     *
     * @param scriptLocation   lua脚本路径
     * @param resultType       返回类型
     * @param argsSerializer   参数序列化类型
     * @param resultSerializer 结果序列化类型
     * @param keys             键列表
     * @param args             参数列表
     * @param <T>              返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/eval">Redis Documentation: EVAL</a>
     * @since redis 2.6.0
     */
    public <T> T excuteAsObj(
            String scriptLocation,
            Class<T> resultType,
            RedisSerializer<?> argsSerializer,
            RedisSerializer<T> resultSerializer,
            List<String> keys,
            Object... args
    ) {
        return this.redisTemplate.execute(
                this.buildScriptWithLocation(scriptLocation, resultType),
                argsSerializer,
                resultSerializer,
                keys,
                args
        );
    }

    /**
     * 执行字符串脚本
     *
     * @param scriptLocation   lua脚本路径
     * @param resultType       返回类型
     * @param argsSerializer   参数序列化类型
     * @param resultSerializer 结果序列化类型
     * @param keys             键列表
     * @param args             参数列表
     * @param <T>              返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/eval">Redis Documentation: EVAL</a>
     * @since redis 2.6.0
     */
    public <T> T excute(
            String scriptLocation,
            Class<T> resultType,
            RedisSerializer<?> argsSerializer,
            RedisSerializer<T> resultSerializer,
            List<String> keys,
            Object... args
    ) {
        return this.stringRedisTemplate.execute(
                this.buildScriptWithLocation(scriptLocation, resultType),
                argsSerializer,
                resultSerializer,
                keys,
                args
        );
    }

    /**
     * 执行对象缓存脚本
     *
     * @param scriptSHA  脚本缓存SHA码
     * @param resultType 返回类型
     * @param keys       键列表
     * @param args       参数列表
     * @param <T>        返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/evalsha">Redis Documentation: EVALSHA</a>
     * @since redis 2.6.0
     */
    public <T> T excuteWithSHAAsObj(String scriptSHA, Class<T> resultType, List<String> keys, Object... args) {
        return this.commands.evalSha(
                scriptSHA,
                ReturnType.fromJavaType(resultType),
                keys.size(),
                ConvertUtil.toByteArray(
                        this.redisTemplate.getKeySerializer(),
                        this.redisTemplate.getValueSerializer(),
                        keys,
                        args
                )
        );
    }

    /**
     * 执行字符串缓存脚本
     *
     * @param scriptSHA  脚本缓存SHA码
     * @param resultType 返回类型
     * @param keys       键列表
     * @param args       参数列表
     * @param <T>        返回类型
     * @return 返回脚本类型对象
     * @see <a href="http://redis.io/commands/evalsha">Redis Documentation: EVALSHA</a>
     * @since redis 2.6.0
     */
    public <T> T excuteWithSHA(String scriptSHA, Class<T> resultType, List<String> keys, Object... args) {
        return this.stringCommands.evalSha(
                scriptSHA,
                ReturnType.fromJavaType(resultType),
                keys.size(),
                ConvertUtil.toByteArray(
                        this.stringRedisTemplate.getKeySerializer(),
                        this.stringRedisTemplate.getValueSerializer(),
                        keys,
                        args
                )
        );
    }

    /**
     * 加载对象脚本到缓存
     *
     * @param scriptText lua脚本
     * @return 返回SHA1校验码
     * @see <a href="http://redis.io/commands/script-load">Redis Documentation: SCRIPT LOAD</a>
     * @since redis 2.6.0
     */
    public String loadAsObj(String scriptText) {
        return this.commands.scriptLoad(RedisSerializer.string().serialize(scriptText));
    }

    /**
     * 加载字符串脚本到缓存
     *
     * @param scriptText lua脚本
     * @return 返回SHA1校验码
     * @see <a href="http://redis.io/commands/script-load">Redis Documentation: SCRIPT LOAD</a>
     * @since redis 2.6.0
     */
    public String load(String scriptText) {
        return this.stringCommands.scriptLoad(RedisSerializer.string().serialize(scriptText));
    }

    /**
     * 加载对象脚本到缓存
     *
     * @param scriptLocation lua脚本路径
     * @return 返回SHA1校验码
     * @see <a href="http://redis.io/commands/script-load">Redis Documentation: SCRIPT LOAD</a>
     * @since redis 2.6.0
     */
    public String loadByLocationAsObj(String scriptLocation) {
        return this.commands.scriptLoad(
                this.buildScriptWithLocation(scriptLocation, null)
                        .getScriptAsString()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 加载字符串脚本到缓存
     *
     * @param scriptLocation lua脚本路径
     * @return 返回SHA1校验码
     * @see <a href="http://redis.io/commands/script-load">Redis Documentation: SCRIPT LOAD</a>
     * @since redis 2.6.0
     */
    public String loadByLocation(String scriptLocation) {
        return this.stringCommands.scriptLoad(
                this.buildScriptWithLocation(scriptLocation, null)
                        .getScriptAsString()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 是否存在对象脚本
     *
     * @param scriptSHAs 脚本sha1验证码
     * @return 返回布尔值列表
     * @see <a href="http://redis.io/commands/script-exists">Redis Documentation: SCRIPT EXISTS</a>
     * @since redis 2.6.0
     */
    public List<Boolean> existsAsObj(String... scriptSHAs) {
        return this.commands.scriptExists(scriptSHAs);
    }

    /**
     * 是否存在字符串脚本
     *
     * @param scriptSHAs 脚本sha1验证码
     * @return 返回布尔值列表
     * @see <a href="http://redis.io/commands/script-exists">Redis Documentation: SCRIPT EXISTS</a>
     * @since redis 2.6.0
     */
    public List<Boolean> exists(String... scriptSHAs) {
        return this.stringCommands.scriptExists(scriptSHAs);
    }

    /**
     * 清除对象脚本缓存
     *
     * @see <a href="http://redis.io/commands/script-flush">Redis Documentation: SCRIPT FLUSH</a>
     * @since redis 2.6.0
     */
    public void clearAsObj() {
        this.commands.scriptFlush();
    }

    /**
     * 清除字符串脚本缓存
     *
     * @see <a href="http://redis.io/commands/script-flush">Redis Documentation: SCRIPT FLUSH</a>
     * @since redis 2.6.0
     */
    public void clear() {
        this.stringCommands.scriptFlush();
    }

    /**
     * 停止对象脚本
     *
     * @see <a href="http://redis.io/commands/script-kill">Redis Documentation: SCRIPT KILL</a>
     * @since redis 2.6.0
     */
    public void stopAsObj() {
        this.commands.scriptKill();
    }

    /**
     * 停止字符串脚本
     *
     * @see <a href="http://redis.io/commands/script-kill">Redis Documentation: SCRIPT KILL</a>
     * @since redis 2.6.0
     */
    public void stop() {
        this.stringCommands.scriptKill();
    }

    /**
     * 获取spring redis模板
     *
     * @return 返回对象模板
     */
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取spring string redis模板
     *
     * @return 返回字符串模板
     */
    public StringRedisTemplate getStringRedisTemplate() {
        return this.stringRedisTemplate;
    }

    /**
     * 通过路径创建脚本
     *
     * @param scriptLocation 脚本路径
     * @param resultType     返回类型
     * @param <T>            类型
     * @return 返回脚本
     */
    private <T> RedisScript<T> buildScriptWithLocation(String scriptLocation, Class<T> resultType) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource(scriptLocation));
        redisScript.setResultType(resultType);
        return redisScript;
    }

    /**
     * 通过字符串创建脚本
     *
     * @param scriptText 脚本字符串
     * @param resultType 返回类型
     * @param <T>        类型
     * @return 返回脚本
     */
    @SuppressWarnings("unchecked")
    private <T> RedisScript<T> buildScriptWithText(String scriptText, Class<T> resultType) {
        return RedisScript.of(scriptText, resultType);
    }
}