package com.learn.spring.demo.webmvc.core;

public interface MyBeanFactory {
    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
