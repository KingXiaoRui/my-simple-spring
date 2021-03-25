package com.king.demo.myspring.context;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
public interface DKApplicationContextAware {

    /**
     * 通过解耦方式获得IOC容器的顶层设计 后面通过一个监听器扫描所有的类，只要实现了此接口 将自动调用setApplicationContext()方法，从而将IOC容器注入到目标中
     *
     * @param applicationContext
     *
     * @throws Exception
     */
    void setApplicationContext(DKApplicationContext applicationContext) throws Exception;

}
