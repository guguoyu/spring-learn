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

    private List<String> registBeanClasses = new ArrayList<>();

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

    //扫描目录下文件，并将className(例如：com.example.demo.Person)封装到registBeanClasses中
    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replace("\\.", "/"));
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


    //
    public List<MyBeanDefinition> loadBeanDefinitions() {
        List<MyBeanDefinition> result = new ArrayList<>();
        for (String className : registBeanClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                if(clazz.isInterface()){
                    continue;
                }
                //如果不是接口，则先创建MyBeanDefinition，再封装到result中，然后返回
                MyBeanDefinition myBeanDefinition = doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()), clazz.getName());
                result.add(myBeanDefinition);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private MyBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        MyBeanDefinition myBeanDefinition = new MyBeanDefinition();
        myBeanDefinition.setBeanClassName(beanClassName);
        myBeanDefinition.setFactoryBeanName(factoryBeanName);

        return myBeanDefinition;
    }
    //将第一个首字母小写
    private String toLowerFirstCase(String simpleName){
        char[] chars = simpleName.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
