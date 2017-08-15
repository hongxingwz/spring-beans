package org.springframework.beans.factory.config;

/**
 * 定义注册共享bean实例的接口。可以由{@link org.springframework.beans.factory.BeanFactory}
 * 实现此接口确保以统一的管理形式暴露他们单例管理的设施
 *
 * <p>{@link ConfigurableBeanFactory}接口继承了此接口
 *
 * @see ConfigurableBeanFactory
 * @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 * @see org.springframework.beans.factory.support.AbstractBeanFactory
 */
public interface SingletonBeanRegistry {
    // TODO: 2017/8/14
    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

    int getSingletonCount();

    Object getSingletonMutex();
}
