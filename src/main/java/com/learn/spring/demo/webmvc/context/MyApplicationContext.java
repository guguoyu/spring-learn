package com.learn.spring.demo.webmvc.context;

import com.learn.spring.demo.webmvc.annotation.MyAutowired;
import com.learn.spring.demo.webmvc.annotation.MyController;
import com.learn.spring.demo.webmvc.annotation.MyService;
import com.learn.spring.demo.webmvc.beans.MyBeanDefinition;
import com.learn.spring.demo.webmvc.beans.MyBeanDefinitionReader;
import com.learn.spring.demo.webmvc.beans.MyBeanWrapper;
import com.learn.spring.demo.webmvc.core.GetBeanPostProcessor;
import com.learn.spring.demo.webmvc.core.MyBeanFactory;

import java.lang.reflect.Field;
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
    //单例的IOC容器缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();

    //通用IOC容器
    private Map<String, MyBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, MyBeanWrapper>();

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

    /**
     * 依赖注入，从这里开始，通过读取BeanDefinition中的信息
     * 然后，通过反射机制创建要给实例返回
     * Spring的做法，不会把原始对象放出去，会用一个BeanWrapper来进行一次包装
     * 装饰模式：
     * 1.保留原来的OOP关系
     * 2.我需要对它进行扩展，增强（为后面的AOP打基础）
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        MyBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        GetBeanPostProcessor beanPostProcessor = new GetBeanPostProcessor();

        Object instance = instantiateBean(beanDefinition);
        if (null == instance) {
            return null;
        }
        try {
            //在实例初始化以前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            MyBeanWrapper beanWrapper = new MyBeanWrapper(instance);
            this.beanWrapperMap.put(beanName, beanWrapper);
            //在实例初始化以后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            populateBean(beanName, instance);
            Object wrappedInstance = this.beanWrapperMap.get(beanName).getWrappedInstance();
            return wrappedInstance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //填充bean，也就是DI注入，给类下面的字段注入bean
    private void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();
        //如果该类没有被MyController和MyService,则不需要注入
        if (!clazz.isAnnotationPresent(MyController.class) || clazz.isAnnotationPresent(MyService.class)) {
            return;
        }
        //否则就需要给字段注入bean
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //首先判断字段是否被@MyAutuwired修饰
            if (!field.isAnnotationPresent(MyAutowired.class)) {
                continue;
            }
            //获取字段被修饰的注解对象
            MyAutowired autowired = field.getAnnotation(MyAutowired.class);
            //获取到注解对象的值
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                //给字段设置属性，就相当于person.setName("张三"); 这里的person相当于这里的instance,"张三"就是value
                field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    //传一个BeanDefinition,就返回一个实例Bean
    private Object instantiateBean(MyBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        if (this.singletonObjects.containsKey(className)) {
            instance = singletonObjects.get(className);
        } else {
            try {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonObjects.put(beanDefinition.getFactoryBeanName(), instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance;
    }
}
