package com.learn.spring.demo.webmvc.context;

import com.learn.spring.demo.webmvc.beans.MyBeanDefinition;
import com.learn.spring.demo.webmvc.beans.MyBeanDefinitionReader;
import com.learn.spring.demo.webmvc.core.MyBeanFactory;

import java.util.List;
import java.util.Set;

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
    //只处理非延迟加载的情况
    private void doAutowired() {
        Set<String> keySet = super.beanDefinitionMap.keySet();
        for (String beanName : keySet) {
            MyBeanDefinition myBeanDefinition = beanDefinitionMap.get(beanName);
            if(!myBeanDefinition.isLazyInit()){
                getBean(beanName);
            }
        }
    }

    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitions) {
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw  new Exception("The “"+beanDefinition.getFactoryBeanName()+"” is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
        //到这里，容器初始化完毕
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
