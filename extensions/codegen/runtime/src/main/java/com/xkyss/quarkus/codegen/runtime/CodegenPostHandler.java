package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.dev.console.DevConsoleManager;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.quarkus.devconsole.runtime.spi.FlashScopeUtil;
import io.quarkus.qute.runtime.devmode.QuteDevConsoleRecorder;
import io.vertx.core.MultiMap;
import io.vertx.core.impl.verticle.CustomJavaFileObject;
import io.vertx.core.impl.verticle.PackageHelper;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import javax.tools.JavaFileObject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class CodegenPostHandler extends DevConsolePostHandler {

    private static final Logger log = Logger.getLogger(CodegenPostHandler.class);

    @Override
    protected void handlePost(RoutingContext context, MultiMap form) throws Exception {
        String id = form.get("id");
        String operation = form.get("operation");
        log.infof("id: %s, operation: %s", id, operation);

        Collection<CodegenContainer> containers = new CodegenContainersSupplier().get();
        log.infof("containers.size(): %d", containers.size());

        for (CodegenContainer container: containers) {
            log.infof("container.getId(): %s", container.getId());
            if (container.getId().equals(id)) {
                genSource(context, container, operation);
                flashMessage(context, String.format("%s generated", operation));
                return;
            }
        }
        flashMessage(context, String.format("Generate Id not found: %s", id), FlashScopeUtil.FlashMessageStatus.ERROR);
    }

    public void genSource(RoutingContext context, CodegenContainer container, String operation) {
        PackageHelper ph = new PackageHelper(Thread.currentThread().getContextClassLoader());
        String entityPackageName = String.format("%s.%s", container.getConfig().packageName, container.getConfig().entityDir);
        log.infof("遍历包: %s", entityPackageName);
        try {
            List<JavaFileObject> javaFileObjects = ph.find(entityPackageName);
            Objects.requireNonNull(javaFileObjects);
            if (javaFileObjects.size() == 0) {
                log.info("没有找到包下面的类.");
            }
            for (JavaFileObject o: javaFileObjects) {
                log.info(o.getName());
                if (o.getKind() != JavaFileObject.Kind.CLASS && o.getKind() != JavaFileObject.Kind.SOURCE) {
                    continue;
                }
                if (o.getName().contains("$")) {
                    continue;
                }

                String binaryName = ((CustomJavaFileObject) o).binaryName();
                log.infof("当前Entity为: %s", binaryName);

                String filepath=System.getProperty("user.dir");
                log.infof("当前目录为: %s", filepath);

                log.info("TODO: 生成文件");

                try {
                    BiFunction<String, Object, String> renderer = DevConsoleManager.getGlobal(QuteDevConsoleRecorder.RENDER_HANDLER);
                    // TODO: 实体名
                    String s = renderer.apply(operation, Map.of("entity", "Hello"));
                    log.info(s);
                } catch (Throwable e) {
                    //flashMessage(context, String.format("Render %s failed", operation), FlashScopeUtil.FlashMessageStatus.ERROR);
                    context.fail(e);
                }
            }
        }
        catch (Exception e) {
            log.error("遍历失败", e);
        }
    }
}
