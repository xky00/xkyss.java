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
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String template = form.get("template");
        String pathname = form.get("pathname");
        String postfix = form.get("postfix");
        log.infof("id: %s, template: %s, pathname: %s, postfix: %s", id, template, pathname, postfix);

        Collection<CodegenContainer> containers = new CodegenContainersSupplier().get();

        for (CodegenContainer container: containers) {
            if (container.getId().equals(id)) {
                genSource(context, container, template, pathname, postfix);
                return;
            }
        }
        flashMessage(context, String.format("Generate Id not found: %s", id), FlashScopeUtil.FlashMessageStatus.ERROR);
    }

    public void genSource(RoutingContext context, CodegenContainer container, String template, String pathname, String postfix) {
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

                // 组合新路径
                String p0 = binaryName.replace('.', '\\');
                String p1 = String.format("%s\\src\\main\\java\\%s", filepath, p0);

                // 转换为Path
                Path path = Paths.get(p1);
                String entityName = path.getFileName().toString();
                path = path.getParent().getParent();

                // R目录
                Path rpath = path.resolve(pathname);
                Files.createDirectories(rpath);
                // R文件
                Path rfile = rpath.resolve(String.format("%s%s", entityName, postfix));
                log.infof("rfile: %s", rfile.toString());

                try {
                    BiFunction<String, Object, String> renderer = DevConsoleManager.getGlobal(QuteDevConsoleRecorder.RENDER_HANDLER);
                    String s = renderer.apply(template, Map.of("entity", entityName));
                    log.info(s);
                    BufferedWriter writer = Files.newBufferedWriter(rfile, StandardCharsets.UTF_8);
                    writer.write(s,0, s.length());
                    writer.flush();
                } catch (Throwable e) {
                    //flashMessage(context, String.format("Render %s failed", operation), FlashScopeUtil.FlashMessageStatus.ERROR);
                    context.fail(e);
                }

                flashMessage(context, String.format("%s generated", rfile.toString()));
            }
        }
        catch (Exception e) {
            log.error("遍历失败", e);
        }
    }
}
