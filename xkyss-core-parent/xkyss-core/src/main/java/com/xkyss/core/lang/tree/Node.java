package com.xkyss.core.lang.tree;

public interface Node<TId, TValue> {
    TId getId();

    void setId(TId id);

    TId getParentId();

    void setParentId(TId parentId);

    TValue getValue();

    void setValue(TValue value);
}
