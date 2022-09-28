package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.quarkus.devconsole.runtime.spi.FlashScopeUtil;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.impl.verticle.CustomJavaFileObject;
import io.vertx.core.impl.verticle.PackageHelper;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Recorder
public class CodegenRecorder {

    private static final Logger log = Logger.getLogger(CodegenRecorder.class);

    static final List<CodegenContainer> CODEGEN_CONTAINERS = new ArrayList<>(2);

    public void resetContainers() {
        CODEGEN_CONTAINERS.clear();
    }

    public void addContainer(String id, CodegenConfig config) {
        CODEGEN_CONTAINERS.add(new CodegenContainer(id, config));
    }

    public Handler<RoutingContext> handler() {
        return new DevConsolePostHandler() {
            @Override
            protected void handlePost(RoutingContext event, MultiMap form) throws Exception {
                String id = form.get("id");
                String operation = form.get("operation");
                log.infof("id: %s, operation: %s", id, operation);

                Collection<CodegenContainer> containers = new CodegenContainersSupplier().get();
                for (CodegenContainer container: containers) {
                    if (container.getId().equals(id)) {
                        if ("resource".equals(operation)) {
                            // TODO: 生成Resource
                            CodegenRecorder.this.genSource(container);
                            flashMessage(event, "Resources generated");
                        }
                        else if ("repository".equals(operation)) {
                            // TODO: 生成Repository
                            flashMessage(event, "Repositories generated");
                        }
                        else {
                            flashMessage(event, String.format("Invalid operation: %s", operation), FlashScopeUtil.FlashMessageStatus.ERROR);
                        }
                        return;
                    }
                }
                flashMessage(event, String.format("Generate Id not found: %s", id), FlashScopeUtil.FlashMessageStatus.ERROR);
            }
        };
    }

    public void genSource(CodegenContainer container) {
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
            }
        }
        catch (Exception e) {
            log.error("遍历失败", e);
        }
    }
}
