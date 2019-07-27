package com.naah69.core.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.naah69.core.config.redisson.RedissonAutoConfiguration;
import com.naah69.core.util.ApplicationContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis自动配置
 *
 * @author xsx
 * @date 2019/4/18
 * @since 1.8
 */
@Configuration
@ConditionalOnClass({RedisTemplate.class})
@Import({
        ApplicationContextUtil.class,
        RedissonAutoConfiguration.class
})
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({RedisTemplate.class})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean({StringRedisTemplate.class})
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
