package com.king.demo.myspring.beans.support;

import com.king.demo.myspring.beans.config.DKBeanDefinition;
import com.king.demo.myspring.context.support.DKAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
public class DKDefaultListableBeanFactory extends DKAbstractApplicationContext {

    /**
     * Map of bean definition objects, keyed by bean name
     */
    protected final Map<String, DKBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, DKBeanDefinition>(256);

}
