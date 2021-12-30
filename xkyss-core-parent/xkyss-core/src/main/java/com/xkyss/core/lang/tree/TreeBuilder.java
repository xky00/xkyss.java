package com.xkyss.core.lang.tree;

import com.xkyii.xktool.core.lang.Assert;

import java.util.*;
import java.util.function.Function;

public class TreeBuilder<TId, TValue> {

    private Function<TValue, TId> idGetter;
    private Function<TValue, TId> pidGetter;

    private final Map<TId, Tree<TId, TValue>> temporaries = new HashMap<>();

    public TreeBuilder<TId, TValue> add(List<TValue> values) {
        for (TValue v: values) {
            add(v);
        }
        return this;
    }

    public TreeBuilder<TId, TValue> add(TValue value) {
        Assert.notNull(idGetter, "id获取函数为null");
        Assert.notNull(pidGetter, "pid获取函数为null");
        Assert.notNull(value, "节点不能为null");

        TId id = idGetter.apply(value);
        Assert.notNull(value, "节点id不能为null");

        Tree<TId, TValue> t = new Tree<>();
        t.setId(id);
        t.setParentId(pidGetter.apply(value));
        t.setValue(value);

        // TODO: 去重校验
        temporaries.put(t.getId(), t);

        return this;
    }

    public Tree<TId, TValue> build() {
        Set<TId> idSet = new HashSet<>(temporaries.keySet());

        temporaries.forEach((id, tree) -> {
            Tree<TId, TValue> parent = temporaries.get(tree.getParentId());
            if (parent != null) {
                parent.getChildren().add(tree);
                idSet.remove(id);
            }
        });

        // 树列表,顶层节点为空
        if (idSet.size() > 1) {
            Tree<TId, TValue> root = new Tree<>();
            for (TId id: idSet) {
                root.getChildren().add(temporaries.get(id));
            }

            return root;
        }

        // 单根树
        if (idSet.size() == 1) {
            return temporaries.get(idSet.iterator().next());
        }

        // 空树
        return new Tree<>();
    }


    public Function<TValue, TId> getIdGetter() {
        return idGetter;
    }

    public void setIdGetter(Function<TValue, TId> idGetter) {
        this.idGetter = idGetter;
    }

    public Function<TValue, TId> getPidGetter() {
        return pidGetter;
    }

    public void setPidGetter(Function<TValue, TId> pidGetter) {
        this.pidGetter = pidGetter;
    }
}
