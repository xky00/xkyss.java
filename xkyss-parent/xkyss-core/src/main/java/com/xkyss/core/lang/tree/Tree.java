package com.xkyss.core.lang.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Tree<TId, TValue> {

    /**
     * id
     */
    private TId id;

    /**
     * 父级id
     */
    private TId parentId;

    /**
     * 节点对象
     */
    private TValue value;


    /**
     * 子树
     */
    private List<Tree<TId, TValue>> children = new ArrayList<>();

    public TId getId() {
        return id;
    }

    public void setId(TId id) {
        this.id = id;
    }

    public TId getParentId() {
        return parentId;
    }

    public void setParentId(TId parentId) {
        this.parentId = parentId;
    }

    public TValue getValue() {
        return value;
    }

    public void setValue(TValue value) {
        this.value = value;
    }

    public List<Tree<TId, TValue>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<TId, TValue>> children) {
        this.children = children;
    }
}
