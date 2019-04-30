package com.king.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DKRequestMapping {

    String name() default "";

    String[] value() default {};

    String[] path() default {};

    DKRequestMethod[] method() default {};
}
