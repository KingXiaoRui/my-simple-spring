package com.king.demo.myspring.beans.support;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
public class DKBeanWrapper {

    private final Object wrappedInstance;

    private Class<?> wrappedClass;


    public DKBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }


    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }


    public Class<?> getWrappedClass() {

        return this.wrappedInstance.getClass();
    }

}
