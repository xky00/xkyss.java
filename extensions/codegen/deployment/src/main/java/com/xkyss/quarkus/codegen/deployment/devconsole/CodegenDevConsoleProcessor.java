package com.xkyss.quarkus.codegen.deployment.devconsole;

import com.xkyss.quarkus.codegen.runtime.CodegenConfig;
import com.xkyss.quarkus.codegen.runtime.CodegenContainersSupplier;
import com.xkyss.quarkus.codegen.runtime.CodegenRecorder;
import com.xkyss.quarkus.codegen.runtime.CodegensConfig;
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

import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;


public class CodegenDevConsoleProcessor {

    /**
     * 给Qute模板传入参数
     * info:codegens
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem codegenUnits(CurateOutcomeBuildItem curateOutcomeBuildItem) {
        return new DevConsoleRuntimeTemplateInfoBuildItem(
                "codegens",
                new CodegenContainersSupplier(),
                this.getClass(),
                curateOutcomeBuildItem
        );
    }

    @BuildStep
    @Record(value = RUNTIME_INIT, optional = true)
    DevConsoleRouteBuildItem invokeEndpoint(CodegenRecorder recorder) {
        // codegen需与resources/dev-templates/codegen.html中的codegen一致
        return new DevConsoleRouteBuildItem("codegen", "POST", recorder.handler());
    }

    /**
     * 初始化Recorder
     */
    @BuildStep
    @Produce(SyntheticBeansRuntimeInitBuildItem.class)
    @Consume(LoggingSetupBuildItem.class)
    @Record(RUNTIME_INIT)
    void init(CodegenRecorder recorder, CodegensConfig configs) {
        recorder.resetContainers();

        recorder.addContainer(DataSourceUtil.DEFAULT_DATASOURCE_NAME, configs.defaultConfig);
        if (!configs.namedConfigs.isEmpty()) {
            for (Map.Entry<String, CodegenConfig> entry: configs.namedConfigs.entrySet()) {
                recorder.addContainer(entry.getKey(), entry.getValue());
            }
        }
    }
}
