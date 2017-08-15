package org.springframework.beans;

public interface BeanMetadataElement {

    /**
     * 对于指定的元数据元素返回配置源{@code Object}（可能为{@code null}）。
     * @return
     */
    Object getSource();
}
