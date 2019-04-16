package com.learn.spring.demo.webmvc.beans;

/**
 * 用来存储配置文件中的信息
 * 相当于保存在内存中的位置
 *
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */
public class MyBeanDefinition {

    private String beanClassName;

    private boolean lazyInit=false;

    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
