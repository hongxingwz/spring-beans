package org.springframework.beans.factory.config;

import org.springframework.beans.factory.BeanFactory;

public interface HierarchicalBeanFactory extends BeanFactory {

    BeanFactory getParentBeanFactory();

    boolean containsLocalBean(String name);
}
