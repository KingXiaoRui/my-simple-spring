package com.king.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DKAutowired {

    /**
     * Declares whether the annotated dependency is required.
     * <p>Defaults to {@code true}.
     */
    String value() default "";
}
