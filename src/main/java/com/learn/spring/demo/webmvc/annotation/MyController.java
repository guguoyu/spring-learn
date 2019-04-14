package com.learn.spring.demo.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @author guguoyu
 * @date 2019/4/14
 * @since JDK 1.8
 */


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    String value() default "";
}
