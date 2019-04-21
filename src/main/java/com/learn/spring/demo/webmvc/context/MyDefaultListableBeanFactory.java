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

    //存储注册信息的BeanDefinition，key为 myBeanDefinitionReader，value为MyBeanDefinition实体对象
    protected final Map<String, MyBeanDefinition> beanDefinitionMap= new ConcurrentHashMap<String,MyBeanDefinition>();
}
