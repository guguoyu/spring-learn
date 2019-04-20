package com.learn.spring.demo.webmvc.context;

/**
 *
 * @author guguoyu
 * @date 2019/4/17
 * @since JDK 1.8
 */
public interface MyApplicationContextAware {
    void setApplicationContext(MyApplicationContext applicationContext);
}
