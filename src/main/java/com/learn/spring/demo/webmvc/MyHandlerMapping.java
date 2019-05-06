package com.learn.spring.demo.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author guguoyu
 * @date 2019/4/20
 * @since JDK 1.8
 */
public class MyHandlerMapping {
    //controller对象
    private Object controller;
    //一个url对应一个方法
    private Method method;

    //url的封装
    private Pattern pattern;

    public MyHandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
