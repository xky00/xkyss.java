package com.xkyss.quarkus.codegen.deployment.devconsole;

import com.xkyss.quarkus.codegen.runtime.*;
import io.quarkus.arc.deployment.SyntheticBeansRuntimeInitBuildItem;
import io.quarkus.datasource.common.runtime.DataSourceUtil;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.*;
import io.quarkus.deployment.logging.LoggingSetupBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRouteBuildItem;
import io.quarkus.devconsole.spi.DevConsoleRuntimeTemplateInfoBuildItem;
import io.quarkus.qute.runtime.devmode.QuteDevConsoleRecorder;

import javax.enterprise.inject.spi.Producer;
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
    public void init(CodegenRecorder recorder, SourcesConfig sources, TargetsConfig targets) {
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
    public void invokeEndpoint(CodegenRecorder recorder,
                               QuteDevConsoleRecorder quteRecorder,
                               BuildProducer<DevConsoleRouteBuildItem> devConsoleRouteProducer) {
        quteRecorder.setupRenderer();

        // 与resources/dev-templates/from-entity.html一致
        devConsoleRouteProducer.produce(new DevConsoleRouteBuildItem("from-entity", "POST", recorder.fromEntity()));

        // 与resources/dev-templates/from-database.html一致
        devConsoleRouteProducer.produce(new DevConsoleRouteBuildItem("from-database", "POST", recorder.fromDatabase()));
    }

}
