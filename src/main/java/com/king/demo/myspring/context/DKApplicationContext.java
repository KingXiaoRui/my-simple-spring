package com.king.demo.myspring.context;

import com.king.demo.myspring.annotation.DKAutowired;
import com.king.demo.myspring.annotation.DKController;
import com.king.demo.myspring.annotation.DKService;
import com.king.demo.myspring.beans.DKBeanFactory;
import com.king.demo.myspring.beans.config.DKBeanDefinition;
import com.king.demo.myspring.beans.support.DKBeanDefinitionReader;
import com.king.demo.myspring.beans.support.DKBeanWrapper;
import com.king.demo.myspring.beans.support.DKDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
public class DKApplicationContext extends DKDefaultListableBeanFactory implements DKBeanFactory {

    private final String[] configLocations;

    //作为单例的IOC容器缓存
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    //通用的IOC容器
    private final Map<String, DKBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    private DKBeanDefinitionReader reader;


    public DKApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }


    @Override
    protected void refresh() {
        //1、定位配置文件
        reader = new DKBeanDefinitionReader(this.configLocations);

        //2、加载配置文件，扫描相关类，封装成BeanDefinition
        List<DKBeanDefinition> beanDefinitionList = reader.loadBeanDefinitions();

        //3、注册，把配置信息放到容器中（伪IOC容器）
        doRegisterBeanDefinition(beanDefinitionList);

        //4、将不是延时加载的类提前初始化
        doAutowireted();

    }


    /**
     * 只处理不是延时加载的情况
     */
    private void doAutowireted() {
        for (Map.Entry<String, DKBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }


    private void doRegisterBeanDefinition(List<DKBeanDefinition> beanDefinitionList) {

        for (DKBeanDefinition beanDefinition : beanDefinitionList) {
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }


    @Override
    public Object getBean(String beanName) {

        //1、初始化
        DKBeanWrapper beanWrapper = instantiateBean(beanName, super.beanDefinitionMap.get(beanName));

        //4、拿到beanWrapper之后，将其保存到IOC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        //3、注入
        populateBean(beanName, new DKBeanDefinition(), beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }


    private void populateBean(String beanName, DKBeanDefinition dkBeanDefinition, DKBeanWrapper dkBeanWrapper) {
        Object instance = dkBeanWrapper.getWrappedInstance();

        Class<?> clazz = dkBeanWrapper.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(DKController.class) || clazz.isAnnotationPresent(DKService.class))) {
            return;
        }
        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DKAutowired.class)) {
                continue;
            }

            DKAutowired autowired = field.getAnnotation(DKAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);
            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    private DKBeanWrapper instantiateBean(String beanName, DKBeanDefinition dkBeanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = dkBeanDefinition.getBeanClassName();

        //2、反射实例化，得到一个对象
        Object instance = null;
        try {
            if (this.singletonObjects.containsKey(className)) {
                instance = this.singletonObjects.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonObjects.put(className, instance);
                this.singletonObjects.put(dkBeanDefinition.getFactoryBeanName(), instance);
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //3、把对象封装到BeanWrapper中
        //singletonObjects
        //factoryBeanInstanceCache
        DKBeanWrapper beanWrapper = new DKBeanWrapper(instance);

        //4、把BeanWrapper存到IOC容器中

        return beanWrapper;
    }

}
