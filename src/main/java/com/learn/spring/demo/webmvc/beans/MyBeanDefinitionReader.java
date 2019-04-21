package com.learn.spring.demo.webmvc.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author guguoyu
 * @date 2019/4/16
 * @since JDK 1.8
 */

//用于对配置文件查找，读取，解析
public class MyBeanDefinitionReader {
    //里面存储的元素为className,例如：com.learn.spring.demo.webmvc.annotation.MyAutowired
    private List<String> registBeanClasses = new ArrayList<String>();

    private Properties config = new Properties();

    //固定配置文件中的key,相当于xml的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public MyBeanDefinitionReader(String... configLocations) {
        InputStream is = this.getClass().getClassLoader().
                getResourceAsStream(configLocations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    public Properties getConfig() {
        return this.config;
    }

    //扫描目录下文件，并将className(例如：com.example.demo.Person)封装到registBeanClasses-List<String>中
    private void doScanner(String scanPackage) {

        //转换为文件路径，实际上就是把.替换为/

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        System.out.println(url);
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registBeanClasses.add(className);
            }
        }

    }


    //把配置文件
    public List<MyBeanDefinition> loadBeanDefinitions() {
        List<MyBeanDefinition> result = new ArrayList<MyBeanDefinition>();
        for (String className : registBeanClasses) {
            try {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                //如果不是接口，则先创建MyBeanDefinition，再封装到result中，然后返回
                MyBeanDefinition myBeanDefinition = doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName());
                result.add(myBeanDefinition);
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    result.add(doCreateBeanDefinition(anInterface.getName(), beanClass.getName()));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //把每一个配置信息解析成一个MyBeanDefinition
    private MyBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        MyBeanDefinition myBeanDefinition = new MyBeanDefinition();
        myBeanDefinition.setBeanClassName(beanClassName);
        myBeanDefinition.setFactoryBeanName(factoryBeanName);
        return myBeanDefinition;
    }

    //将第一个首字母小写
    private String toLowerFirstCase(String simpleName) {
        //因为大写字母的ASCII码小于 小写字母的ASCII码
        //而且大小写字母相差正好32
        //在Java中，对char做数学运算，就是对ASCII码做运算
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
