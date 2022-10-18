package com.xkyss.quarkus.codegen.runtime;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class CodegenContainerSupplier implements Supplier<Collection<CodegenContainer>> {

    @Override
    public Collection<CodegenContainer> get() {
        return List.of(new CodegenContainer("c1"), new CodegenContainer("c2"));
    }
}
