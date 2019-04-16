package com.learn.spring.demo.webmvc.context;

import com.learn.spring.demo.webmvc.beans.MyBeanDefinition;
import com.learn.spring.demo.webmvc.beans.MyBeanDefinitionReader;
import com.learn.spring.demo.webmvc.core.MyBeanFactory;

import java.util.List;

/**
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {


    private String[] configLocations;

    private MyBeanDefinitionReader reader;

    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        //1.定位,定位配置文件
         reader = new MyBeanDefinitionReader(configLocations);
        //2.加载，扫描相关的类，把他们封装成BeanDefinition
        List<MyBeanDefinition> beanDefinitions= reader.loadBeanDefinitions();
        //3.注册，把配置信息放到容器里(伪IOC容器)
        doRegisterBeanDefinition(beanDefinitions);
        //4.把不是延时加载的类，提前初始化
        doAutowired();
    }

    private void doAutowired() {

    }

    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) {

    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
