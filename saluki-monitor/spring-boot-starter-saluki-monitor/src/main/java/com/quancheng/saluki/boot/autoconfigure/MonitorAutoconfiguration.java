/*
 * Copyright (c) 2016, Quancheng-ec.com All right reserved. This software is the
 * confidential and proprietary information of Quancheng-ec.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Quancheng-ec.com.
 */
package com.quancheng.saluki.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author shimingliu 2016年12月20日 下午3:44:35
 * @version MonitorAutoconfiguration.java, v 0.0.1 2016年12月20日 下午3:44:35 shimingliu
 */
@Configuration
@ConditionalOnExpression("${thrall.monitor.enabled:true}")
public class MonitorAutoconfiguration {

    private static final Logger log = LoggerFactory.getLogger(MonitorAutoconfiguration.class);

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor(ApplicationContext applicationContext) {
        return new BeanFactoryPostProcessor() {

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                if (beanFactory instanceof BeanDefinitionRegistry) {
                    try {
                        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
                        scanner.setResourceLoader(applicationContext);
                        scanner.scan("com.quancheng.saluki.boot.monitor");
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                    }
                }

            }

        };
    }

    @Configuration
    @AutoConfigureAfter(WebMvcAutoConfiguration.class)
    public static class WebMvcAutoconfig extends WebMvcConfigurerAdapter {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/service/dist/**")//
                    .addResourceLocations("classpath:/META-INF/static/dist/");
            registry.addResourceHandler("service.html").//
                    addResourceLocations("classpath:/META-INF/static/");
            registry.addResourceHandler("/webjars/**")//
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }

    };

}