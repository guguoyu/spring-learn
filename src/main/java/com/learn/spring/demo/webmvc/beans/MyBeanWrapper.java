package com.learn.spring.demo.webmvc.beans;

/**
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */
public class MyBeanWrapper {
    //实例对象
    private Object wrappedInstance;
    //实例的Class
    private Class<?> wrappedClass;

    public MyBeanWrapper(Object wrappedInstance){
        this.wrappedInstance=wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    //返回代理以后的Class
    //可能会是$Proxy0
    public Class<?> getWrappedClass() {
        return wrappedInstance.getClass();
    }
}
