package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

/**
 * 访问Spring bean容器的根接口。
 * 这是bean容器最基础的客户端视图;
 * 进一步的接口例如 {@link ListableBeanFactory} 和 {@link org.springframework.beans.factory.config..ConfigurableBeanFactory}
 * 用来更特殊的目地
 *
 * <p>此接口由持有一定数量的定义好的bean的对象来实现，每个bean是唯一的由一个字符名字来辨别。依赖于bean的定义，
 * 工厂将会返回一个independent instance of a contained object(the Prototype design pattern) 或者
 * 一个共享的单例(比单例设置模式更好的选择，返回的bean在工厂范围内是单例的)。返回哪种类型的bean依赖于bean工厂的配置：
 * API是相同的。从自Spring 2.0之后更多的域可以用依赖于具体的应用上下文(e.g. 在web环境下的"request" and "session"域)
 *
 * <p>此bean工厂的要点是作为一个应用组件的中央注册，和应用组件的集中配置（例如，不需要单独的对象来读取属性文件）。
 * 参阅 "Expert One-on-One J2EE Design and Development"对此方法益处的讨论。
 *
 * <p>注意通常依赖于依赖注入是较好的选择("push"配置)来配置对应对象通过setters或构造器，而不是使用任何"pull"配置的形式（像在
 * bean工厂里查找）。Spring的依赖注入功能使用此bean工厂接口和其子接口实现
 *
 * <P>通常一个bean工厂将会加载存储在配置源(例如像XML)的bean定义信息，使用{@code org.springframework.beans}包来配置beans。然而，
 * 实例可以仅返回在Java代码里创建的Java对象如果必要的话。这里没有对定义信息怎样存储作限制：LDAP, RDBMS, XML, 属性文件。
 * 实现鼓励bean之间的相互引用(Dependency Injection).
 *
 * <p>与{@link ListableBeanFactory}的方法相比，此接口的所有操作也将会检查父工厂如果他有一个{@link HierarchicalBeanFactory}。如果
 * bean没有在此工厂实例中找到，最亲近的父工厂将会被寻问。此工厂实例中的bean应该覆盖任何其父工厂中相同名字的bean
 *
 * <p>bean工厂的实现应该尽可能支持标准的bean生命周期接口。全部的初始化方法和他们标准的顺序如下：
 * <ol>
 *     <li>BeanNameAware's {@code setBeanName}</li>
 *     <li>BeanClassLoaderAware's {@code setBeanClassLoader}</li>
 *     <li>BeanFactoryAware's {@code setBeanFactory}</li>
 *     <li>EnvironmentAware's {@code setEnvironment}</li>
 *     <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}</li>
 *     <li>ResourceLoaderAware's {@code setResourceLoader}
 *     (仅当运行在application context下才可应用)</li>
 *     <li>MessageSourceAware's {@code setMessageSource}
 *     (仅当运行在application context下才可应用)</li>
 *     <li>ApplicationContextAware's {@code setApplicationContext}
 *     (仅当运行在application context下才可应用)</li>
 *     <li>ServletContextAware's {@code setApplicationContext}
 *     (仅当运行在web application context下才可应用)</li>
 * </ol>
 *
 * 当关闭一个bean工厂时，下面的生命周期方法将会调用：
 * <ol>
 *     <li>DestructionAwareBeanPostProcessors的{@code postProcessBeforeDestruction}方法
 *     <li>DisposableBean's的 destroy方法
 *     <li>一个自定义的销毁方法
 * </ol>
 */
public interface BeanFactory {

    /**
     * 用来解析一个工厂实例并把他与bean工厂创建的beans区分开来。例如，如果名字为{@code myJndiObject}的bean是一个工厂bean。获取{@code myJndiObject}
     * 将会返回工厂，而不是工厂创建的实例
     */
    String FACTORY_BEAN_PREFIX = "&";

    /**
     * 返回一个实例，可能指定bean共享或独立的。
     * @param name
     * @return
     * @throws BeansException
     */
    Object getBean(String name) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    boolean containsBean(String name);

    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    boolean isTypeMach(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    String[] getAliases(String name);


}
