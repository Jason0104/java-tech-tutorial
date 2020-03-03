package com.java.tech.aop;


import java.lang.annotation.*;

/**
 * created by Jason on 2020/3/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {

    String value() default "";
}
