package com.king.demo.myspring.beans.support;

import com.king.demo.myspring.beans.config.DKBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author DKing
 * @description
 * @date 2019/4/25
 */
public class DKBeanDefinitionReader {

    //固定配置文件中的Bean，相当于XML的规范
    private final String SCAN_PACKAGE = "scanPackage";

    private Properties config = new Properties();
    private List<String> registryBeanClasses = new ArrayList<>();

    public DKBeanDefinitionReader(String... configLocations) {

        //通过URL定位找到所对应的文件，然后转换为文件流
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(configLocations[0].replace("classpath:", ""));

        try {
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(this.config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String property) {
        URL url = this.getClass().getClassLoader().getResource("/" + property.replaceAll("\\.", "/"));
        File classpath = new File(url.getFile());
        for (File file : classpath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(property + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = property + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return this.config;
    }

    /**
     * 将配置文件中扫描到的所有配置信息转换成一个DKBeanDefinition对象，便于使用。
     *
     * @return
     */
    public List<DKBeanDefinition> loadBeanDefinitions() {
        List<DKBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {

                Class<?> beanClass = Class.forName(className);
                if (!beanClass.isInterface()) {
                    continue;
                }


                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把每一个配置信息解析成BeanDefinition
     *
     * @param simpleName
     * @param className
     * @return
     */
    private DKBeanDefinition doCreateBeanDefinition(String simpleName, String className) {


        DKBeanDefinition beanDefinition = new DKBeanDefinition();
        beanDefinition.setBeanClassName(className);
        beanDefinition.setFactoryBeanName(simpleName);

        return beanDefinition;

    }

    private String toLowerFirstCase(String beanName) {
        char[] chars = beanName.toCharArray();

        chars[0] += 32;
        return String.valueOf(chars);
    }
}
