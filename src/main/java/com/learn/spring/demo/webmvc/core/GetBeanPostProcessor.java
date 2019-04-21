package com.learn.spring.demo.webmvc.core;

/**
 * @author guguoyu
 * @date 2019/4/21
 * @since JDK 1.8
 */
public class GetBeanPostProcessor {

    //为在Bean的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }
    //为在Bean的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }


}
