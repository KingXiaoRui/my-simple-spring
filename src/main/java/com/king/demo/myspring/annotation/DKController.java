package com.king.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DKController {

    String value() default "";

}
