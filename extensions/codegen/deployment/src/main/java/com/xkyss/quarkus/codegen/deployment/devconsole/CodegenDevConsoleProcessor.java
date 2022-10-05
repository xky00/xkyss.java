package com.xkyss.quarkus.codegen.deployment.devconsole;

import com.xkyss.quarkus.codegen.runtime.*;
import io.quarkus.arc.deployment.SyntheticBeansRuntimeInitBuildItem;
import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Consume;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.logging.LoggingSetupBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRouteBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;
import io.quarkus.qute.runtime.devmode.QuteDevConsoleRecorder;

import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

public class CodegenDevConsoleProcessor {

    /**
     * 给Qute模板传入参数
     * info:sources
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem sourceUnits(
            CurateOutcomeBuildItem curateOutcomeBuildItem) {
        return new DevConsoleRuntimeTemplateInfoBuildItem(
                "sources",
                new SourceContainerSupplier(),
                this.getClass(),
                curateOutcomeBuildItem
        );
    }
    /**
     * 给Qute模板传入参数
     * info:targets
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem targetUnits(
            CurateOutcomeBuildItem curateOutcomeBuildItem) {
        return new DevConsoleRuntimeTemplateInfoBuildItem(
                "targets",
                new TargetContainerSupplier(),
                this.getClass(),
                curateOutcomeBuildItem
        );
    }

    /**
     * 初始化Recorder
     */
    @BuildStep
    @Produce(SyntheticBeansRuntimeInitBuildItem.class)
    @Consume(LoggingSetupBuildItem.class)
    @Record(RUNTIME_INIT)
    void init(CodegenRecorder recorder, SourcesConfig sources, TargetsConfig targets) {
        recorder.resetContainers();

        // source configs
        recorder.addSource(DataSourceUtil.DEFAULT_DATASOURCE_NAME, sources.defaultConfig);
        if (!sources.namedConfigs.isEmpty()) {
            for (Map.Entry<String, SourceConfig> entry: sources.namedConfigs.entrySet()) {
                recorder.addSource(entry.getKey(), entry.getValue());
            }
        }

        // target configs
        recorder.addTarget(DataSourceUtil.DEFAULT_DATASOURCE_NAME, targets.defaultConfig);
        if (!targets.namedConfigs.isEmpty()) {
            for (Map.Entry<String, TargetConfig> entry: targets.namedConfigs.entrySet()) {
                recorder.addTarget(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 调用Record中的接口
     */
    @BuildStep
    @Record(value = RUNTIME_INIT, optional = true)
    DevConsoleRouteBuildItem invokeEndpoint(CodegenRecorder recorder, QuteDevConsoleRecorder quteRecorder) {
        quteRecorder.setupRenderer();
        // codegen需与resources/dev-templates/codegen.html中的codegen一致
        return new DevConsoleRouteBuildItem("source", "POST", recorder.handler());
    }

}
