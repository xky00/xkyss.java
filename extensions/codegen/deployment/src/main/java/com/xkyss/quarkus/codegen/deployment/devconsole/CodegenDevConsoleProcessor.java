package com.xkyss.quarkus.codegen.deployment.devconsole;

import com.xkyss.quarkus.codegen.runtime.CodegenContainerSupplier;
import com.xkyss.quarkus.codegen.runtime.CodegenRecorder;
import com.xkyss.quarkus.codegen.runtime.config.CodegenConfig;
import io.quarkus.arc.runtime.BeanLookupSupplier;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRouteBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;
import io.quarkus.qute.runtime.devmode.QuteDevConsoleRecorder;
import org.jboss.logging.Logger;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

public class CodegenDevConsoleProcessor {
    private static final Logger log = Logger.getLogger(CodegenDevConsoleProcessor.class);

    /**
     * 给Qute模板传入参数
     * info:sources
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem sourceUnits(CurateOutcomeBuildItem curateOutcomeBuildItem, CodegenConfig config) {
        return new DevConsoleRuntimeTemplateInfoBuildItem(
            "container",
            new CodegenContainerSupplier(),
            this.getClass(),
            curateOutcomeBuildItem
        );
    }

    /**
     * 调用Record中的接口
     */
    @BuildStep
    @Record(value = RUNTIME_INIT, optional = true)
    public void invokeEndpoint(CodegenRecorder recorder,
                               QuteDevConsoleRecorder quteRecorder,
                               BuildProducer<DevConsoleRouteBuildItem> devConsoleRouteProducer) {
        quteRecorder.setupRenderer();

        // 与resources/dev-templates/generator.html一致
        devConsoleRouteProducer.produce(
            new DevConsoleRouteBuildItem("generator", "POST", recorder.generateHandler()));
    }
}
