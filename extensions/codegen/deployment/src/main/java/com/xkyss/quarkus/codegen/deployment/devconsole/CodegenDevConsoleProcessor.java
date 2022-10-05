package com.xkyss.quarkus.codegen.deployment.devconsole;

import com.xkyss.quarkus.codegen.runtime.*;
import com.xkyss.quarkus.codegen.runtime.config.*;
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

import javax.xml.transform.Source;
import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

public class CodegenDevConsoleProcessor {

    /**
     * 给Qute模板传入参数
     * info:codegens
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    public DevConsoleRuntimeTemplateInfoBuildItem codegenUnits(CurateOutcomeBuildItem curateOutcomeBuildItem, SourcesConfig sourcesConfig) {
        return new DevConsoleRuntimeTemplateInfoBuildItem(
                "sources",
                new ConfigContainerSupplier(sourcesConfig),
                this.getClass(),
                curateOutcomeBuildItem
        );
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
