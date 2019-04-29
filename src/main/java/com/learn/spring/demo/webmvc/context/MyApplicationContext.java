package com.learn.spring.demo.webmvc.context;

import com.learn.spring.demo.webmvc.beans.MyBeanDefinition;
import com.learn.spring.demo.webmvc.beans.MyBeanDefinitionReader;
import com.learn.spring.demo.webmvc.beans.MyBeanWrapper;
import com.learn.spring.demo.webmvc.core.MyBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {


    private String[] configLocations;

    private MyBeanDefinitionReader reader;
    //用来保证注册式单例的容器
    private Map<String, Object> singletonBeanCacheMap = new ConcurrentHashMap<String, Object>();

    //通用IOC容器
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, MyBeanWrapper>();

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
        List<MyBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
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
            if (!myBeanDefinition.isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    /**
     * 将List<MyBeanDefinition> 都注册到父类的属性map中，即beanDefinitionMap
     * key为beanDefinition的factoryBeanName,例如：myBeanDefinitionReader
     * value为beanDefiniition对象
     *
     * @param beanDefinitions
     * @throws Exception
     */
    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
        //到这里，容器初始化完毕
    }

    /**
     * 返回的为beanFactoryName(例如)数组，myBeanDefinitionReader
     *
     * @return
     */
    public String[] getBeanDefinitionNames() {
        Set<String> keySet = this.beanDefinitionMap.keySet();
        String[] strings = keySet.toArray(new String[this.beanDefinitionMap.size()]);
        return strings;
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    @Override
    public Object getBean(String beanName) {
        MyBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object bean = instantiateBean(beanDefinition);
        return bean;
    }

    //传一个BeanDefinition,就返回一个实例Bean
    private Object instantiateBean(MyBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        if (this.singletonBeanCacheMap.containsKey(className)) {
            instance = singletonBeanCacheMap.get(className);
        } else {
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonBeanCacheMap.put(beanDefinition.getFactoryBeanName(),instance);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }
}
