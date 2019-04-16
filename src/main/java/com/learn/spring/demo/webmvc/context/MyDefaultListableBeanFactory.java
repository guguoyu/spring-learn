package com.learn.spring.demo.webmvc.context;

import com.learn.spring.demo.webmvc.beans.MyBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */
public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext {

    //
    protected final Map<String, MyBeanDefinition> beanDefinitionMap= new ConcurrentHashMap<>();
}
