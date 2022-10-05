package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;

import java.util.*;
import java.util.function.Supplier;

public class TargetContainerSupplier implements Supplier<Collection<TargetContainer>> {
    @Override
    public Collection<TargetContainer> get() {

        if (CodegenRecorder.TARGET_CONTAINERS.isEmpty()) {
            return Collections.emptySet();
        }

        Set<TargetContainer> containers = new TreeSet<>(CodegenContainerComparator.INSTANCE);
        containers.addAll(CodegenRecorder.TARGET_CONTAINERS);
        return containers;
    }

    private static class CodegenContainerComparator implements Comparator<TargetContainer> {

        private static final CodegenContainerComparator INSTANCE = new CodegenContainerComparator();

        @Override
        public int compare(TargetContainer o1, TargetContainer o2) {

            String id1 = o1.getId();
            String id2 = o2.getId();

            if (DataSourceUtil.isDefault(id1)) {
                return -1;
            }
            if (DataSourceUtil.isDefault(id2)) {
                return 1;
            }

            return id1.compareTo(id2);
        }
    }
}
