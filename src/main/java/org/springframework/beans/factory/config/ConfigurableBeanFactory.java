package org.springframework.beans.factory.config;

import org.springframework.beans.factory.BeanFactory;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry{
    // TODO: 2017/8/14

    /**
     * 标准的单例域"singleton"标识符
     * 自定义的可以通过registerScope添加
     *
     * @see #registerScope
     */
    String SCOPE_SINGLETON = "singleton";


    /**
     * 标准的原型域:"prototype"标识符
     * 自定义的标识符可以通过{@code registerScope}添加
     *
     * @see #registerScope
     */
    String SCOPE_PROTOTPYE = "prototype";

    void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getBeanClassLoader();

    void setTempClassLoader(ClassLoader tempClassLoader);

    ClassLoader getTempClassLoader();

    void setCacheBeanMetadata(boolean cacheBeanMetadata);

    boolean isCacheBeanMetadata();

}
