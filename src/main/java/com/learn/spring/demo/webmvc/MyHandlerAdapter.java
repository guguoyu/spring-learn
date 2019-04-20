package com.learn.spring.demo.webmvc;

/**
 * @author guguoyu
 * @date 2019/4/20
 * @since JDK 1.8
 */
//专人干专事
public class MyHandlerAdapter {

    public boolean supports(Object handler){
        return (handler instanceof MyHandlerMapping);
    }


}
