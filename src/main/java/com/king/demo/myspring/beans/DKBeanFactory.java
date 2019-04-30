package com.king.demo.myspring.beans;

/**
 * @author DKing
 * @description 单例
 * @date 2019/4/25
 */
public interface DKBeanFactory {

    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
