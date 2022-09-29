package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.function.Supplier;

public class CodegenContainersSupplier implements Supplier<Collection<CodegenContainer>> {
    private static final Logger log = Logger.getLogger(CodegenContainersSupplier.class);

    @Override
    public Collection<CodegenContainer> get() {
        log.infof("CodegenRecorder.CODEGEN_CONTAINERS.size(): %d", CodegenRecorder.CODEGEN_CONTAINERS.size());

        if (CodegenRecorder.CODEGEN_CONTAINERS.isEmpty()) {
            return Collections.emptySet();
        }

        Set<CodegenContainer> containers = new TreeSet<>(CodegenContainerComparator.INSTANCE);
        containers.addAll(CodegenRecorder.CODEGEN_CONTAINERS);
        return containers;
    }

    private static class CodegenContainerComparator implements Comparator<CodegenContainer> {

        private static final CodegenContainerComparator INSTANCE = new CodegenContainerComparator();

        @Override
        public int compare(CodegenContainer o1, CodegenContainer o2) {

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
