package org.springframework.beans.factory.config;

import org.springframework.beans.factory.BeanFactory;

/**
 * 实现bean factories的子接口作为继承体系的一部分
 *
 * <p>
 *     在configurable的形式中相应的@{code setParentBeanFactory}方法
 *     允许设置父BeanFactory，可以在ConfigurableBeanFactory接口中找到
 * </p>
 *
 * @author JiangLei
 * @sincd 2017-08-19
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#setParentBeanFactory
 */
public interface HierarchicalBeanFactory extends BeanFactory {

    /**
     * 返回此bean工厂的父工厂，如果没有则返回{@code null}
     * @return
     */
    BeanFactory getParentBeanFactory();

    /**
     * 返回此工厂是否包含指定名字的bean，忽略在祖先上下文中
     * 定义的bean
     *
     * <p>
     *     这是{@code containsBean}的可供代替的方法，
     *     忽略在祖先bean工厂中定义的指定名字的bean
     * </p>
     * @param name 要查询的bean的名字
     * @return 在本工厂中是否具有指定名字的bean
     * @see BeanFactory#containsBean
     */
    boolean containsLocalBean(String name);
}
