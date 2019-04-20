package com.learn.spring.demo.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author guguoyu
 * @date 2019/4/20
 * @since JDK 1.8
 */
public class MyHandlerMapping {

    private Object controller;

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
