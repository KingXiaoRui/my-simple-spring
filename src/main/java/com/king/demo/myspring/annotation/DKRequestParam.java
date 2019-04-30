package com.king.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DKRequestParam {

    String value() default "";

    String name() default "";
}
