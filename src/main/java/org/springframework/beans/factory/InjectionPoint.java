package org.springframework.beans.factory;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;

/**
 * 对注入点简单的描述，指向一个方法/构造器参数或一个域。
 * 由{@link UnsatisfiedDependencyException}暴露。
 *
 * @author JiangLei
 * @since 4.3
 * @see UnsatisfiedDependencyException#getInjectionPoint()
 * @see org.springframework.beans.factory.config.DependencyDescriptor
 */
public class InjectionPoint {

    protected MethodParameter methodParameter;

    protected Field field;

    private volatile Annotation[] fieldAnnotations;

    /**
     * 创建一个方法或构造器参数的注入点描述符
     * @param methodParameter 要包裹的MethodParameter
     */
    public InjectionPoint(MethodParameter methodParameter) {
        Assert.notNull(methodParameter, "MethodParameter must not be null");
        this.methodParameter = methodParameter;
    }

    /**
     * 创建一个字段的注入点描述符
     * @param field
     */
    public InjectionPoint(Field field) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
    }

    /**
     * 复制构造器
     * @param original 要复制的原始描述符
     */
    protected InjectionPoint(InjectionPoint original) {
        this.methodParameter = (original.methodParameter != null ?
                new MethodParameter(original.methodParameter) : null);
        this.field = original.field;
        this.fieldAnnotations = original.fieldAnnotations;
    }

    /**
     * 仅用在子类序列化可用的目的上
     */
    protected InjectionPoint() {

    }

    /**
     * 返回包裹的MethodParameter,如果有的话。
     * <p>
     *     注意：无论MethodParameter或Field是否可用
     * </p>
     * @return MethodParameter, 如果没有返回{@code null}
     */
    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    /**
     * 返回包裹的字段，如果有的话
     * <p>
     *     注意：不管MethodParameter或Field可用
     * </p>
     * @return 字段，如果没有返回{@code null}
     */
    public Field getField() {
        return this.field;
    }

    /**
     * 获取包裹的字段或(方法/构造器参数)相关联的注解
     */
    public Annotation[] getAnnotations() {
        if (this.field != null) {
            if (this.fieldAnnotations == null) {
                this.fieldAnnotations = this.field.getAnnotations();
            }
            return this.fieldAnnotations;
        } else {
            return this.methodParameter.getParameterAnnotations();
        }
    }

    /**
     * 提取指定类型的field/parameter注解，如果有的话。
     * @param annotationType 要提取的注解类型
     * @return 注解实例，如果没有找到返回{@code null}
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (this.field != null ? this.field.getAnnotation(annotationType) :
                this.methodParameter.getParameterAnnotation(annotationType));
    }

    /**
     * 返回field或method/constructor参数声明的类型，
     * 表示注入点的类型
     * @return
     */
    public Class<?> getDeclaredType() {
        return (this.field != null ? this.field.getType() : this.methodParameter.getParameterType());
    }

    /**
     * 返回包裹的成员，包含注入点
     * @return Field / Method / Constructor 作为 Member
     */
    public Member getMember() {
        return (this.field != null ? this.field : this.methodParameter.getMember());
    }

    /**
     * 返回包裹的注解元素。
     * <p>
     *     注意：如果是method/constructor参数，此暴露了
     *     在方法或构造器上声明的注解(i.e. 在method/constructor级别上的，而不是在parameter级别上)。
     *     在这种情况下，使用{@link #getAnnotations()}来获取参数级别上的注解， transparently with
     *     corresponding field annotations
     * </p>
     * @return 以 AnnotatedElement在形式返回Field / Method / Constructor
     */
    public AnnotatedElement getAnnotatedElement() {
        return (this.field != null ? this.field : this.methodParameter.getAnnotatedElement());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        InjectionPoint otherPoint = (InjectionPoint) other;
        return (this.field != null ? this.field.equals(otherPoint.field) :
                this.methodParameter.equals(otherPoint.methodParameter));
    }

    @Override
    public int hashCode() {
        return (this.field != null ? this.field.hashCode() : this.methodParameter.hashCode());
    }

    @Override
    public String toString() {
        return (this.field != null ? "field '" + this.field.getName() + "'" : this.methodParameter.toString());
    }
}
