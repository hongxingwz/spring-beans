package org.springframework.beans.factory;


import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * bean工厂中方便的操作方法，特别是在{@link ListableBeanFactory}接口中。
 *
 * <p>
 *     返回bean的数量，bean的名字，或bean的实例，
 *     考虑到bean工厂继承了多少层(在ListableBeanFacotry接口中定义的方法作不到，
 *     与在BeanFactory接口中定义的方法相比)
 * </p>
 *
 * @author JiangLei
 * @since 2003-07-04
 */
public abstract class BeanFactoryUtils {

    /**
     * 生成bean名字的分割符。如果class name或parent name不是唯一的，
     * "#1", "#2"将会被追回，直到名字变成唯一的。
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    /**
     * 返回指定名是否是工厂的引用(以工厂引用的前缀开始)
     * @param name 指定bean的名字
     * @return 是否指定的名字是工厂的引用
     * @see BeanFactory#FACTORY_BEAN_PREFIX
     */
    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    /**
     * 返回真实的bean名字，删除掉工厂引用的前缀(如果可能，也会删除他找到的重复的工厂前缀)
     * @param name bean工厂的名字
     * @return 转换后的名字
     * @see BeanFactory#FACTORY_BEAN_PREFIX
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }

    /**
     * 返回指定的名字是否是用默认的命名策略(包含"#..."部分)生成的
     * @param name 指定的bean名字
     * @return 指定的名字是否是生成的
     * @see #GENERATED_BEAN_NAME_SEPARATOR
     * @see org.springframework.beans.factory.support.BeanDefinitionReaderUtils#generateBeanName
     * @see org.springframework.beans.factory.support.DefaultBeanNameGenerator
     */
    public static boolean isGeneratedBeanName(String name) {
        return (name != null && name.contains(GENERATED_BEAN_NAME_SEPARATOR));
    }

    /**
     * 提取"原始"的bean名字从指定的bean(可能是生成的)名字中，包括任何的"#..."
     * 后缀添加它可能是为了唯一性的目的
     * @param name 原始的bean名字
     * @return #GENERATED_BEAN_NAME_SEPARATOR
     */
    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int seperatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (seperatorIndex != -1 ? name.substring(0, seperatorIndex) : name);
    }


    public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf).length;
    }

    public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf, Object.class);
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                List<String> resultList = new ArrayList<>();
                resultList.addAll(Arrays.asList(result));
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }
                result = StringUtils.toStringArray(resultList);
            }
        }

        return result;
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                List<String> resultList = new ArrayList<>();
                resultList.addAll(Arrays.asList(result));
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }

                result = StringUtils.toStringArray(resultList);
            }
        }

        return result;
    }

    public static String[] beanNamesForTypeIncludingAnces(
            ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAnces(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                List<String> resultList = new ArrayList<>();
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }
                result = StringUtils.toStringArray(resultList);
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
        throws BeansException{
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException{
        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
        return uniqueBean(type, beansOfType);
    }

    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type);
        return uniqueBean(type, beansOfType);
    }

    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
        throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        return uniqueBean(type, beansOfType);
    }

    private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
        int nrFound = matchingBeans.size();
        if (nrFound == 1) {
            return matchingBeans.values().iterator().next();
        } else if (nrFound > 1) {
            throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
        } else {
            throw new NoSuchBeanDefinitionException(type);
        }
    }

}
