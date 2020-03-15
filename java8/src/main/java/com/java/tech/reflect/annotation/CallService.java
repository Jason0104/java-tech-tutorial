package com.java.tech.reflect.annotation;

import java.lang.annotation.*;

/**
 * created by Jason on 2020/3/15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CallService {

    String value() default "";
}
