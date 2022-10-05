package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;

import java.util.*;
import java.util.function.Supplier;

public class SourceContainerSupplier implements Supplier<Collection<SourceContainer>> {
    @Override
    public Collection<SourceContainer> get() {
        if (CodegenRecorder.SOURCE_CONTAINERS.isEmpty()) {
            return Collections.emptySet();
        }

        Set<SourceContainer> containers = new TreeSet<>(CodegenContainerComparator.INSTANCE);
        containers.addAll(CodegenRecorder.SOURCE_CONTAINERS);
        return containers;
    }

    private static class CodegenContainerComparator implements Comparator<SourceContainer> {

        private static final CodegenContainerComparator INSTANCE = new CodegenContainerComparator();

        @Override
        public int compare(SourceContainer o1, SourceContainer o2) {

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
