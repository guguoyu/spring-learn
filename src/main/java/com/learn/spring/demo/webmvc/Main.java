package com.learn.spring.demo.webmvc;

import com.learn.spring.demo.webmvc.annotation.MyAutowired;

import java.lang.reflect.Field;

public class Main {

    @MyAutowired
    private String name;
    public static void main(String[] args) {

        try {
            Class<?> clazz = Class.forName("com.learn.spring.demo.webmvc.Main");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                System.out.println(field);
                System.out.println(field.getName());
                field.setAccessible(true);
                String name = field.getType().getName();
                System.out.println(name);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
