package com.naah69.core.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * redis初始化类
 *
 * @author xsx
 * @date 2019/6/12
 * @since 1.8
 */
public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        context.addBeanFactoryPostProcessor(new RedisInitializerPostProcessor());
    }
}


