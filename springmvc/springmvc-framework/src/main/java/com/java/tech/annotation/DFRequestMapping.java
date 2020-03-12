package com.java.tech.annotation;

import java.lang.annotation.*;

/**
 * created by Jason on 2020/3/12
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DFRequestMapping {

    String value() default "";
}
