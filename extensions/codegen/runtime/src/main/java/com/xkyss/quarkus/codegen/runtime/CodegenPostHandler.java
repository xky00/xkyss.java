package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.core.util.Pathx;
import io.quarkus.dev.console.DevConsoleManager;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.quarkus.devconsole.runtime.spi.FlashScopeUtil;
import io.quarkus.qute.runtime.devmode.QuteDevConsoleRecorder;
import io.vertx.core.MultiMap;
import io.vertx.core.impl.verticle.PackageHelper;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class CodegenPostHandler extends DevConsolePostHandler {

    private static final Logger log = Logger.getLogger(CodegenPostHandler.class);

    @Override
    protected void handlePost(RoutingContext context, MultiMap form) {
        String sourceId = Objects.requireNonNull(form.get("sourceId"));
        String targetId = Objects.requireNonNull(form.get("targetId"));
        log.infof("sourceId: %s, targetId: %s", sourceId, targetId);

        Optional<SourceContainer> sourceOptional = new SourceContainerSupplier().get().stream().filter(x -> x.getId().equals(sourceId)).findFirst();
        if (!sourceOptional.isPresent()) {
            flashMessage(context, String.format("Source config NOT found: %s", sourceId), FlashScopeUtil.FlashMessageStatus.ERROR);
            return;
        }

        Optional<TargetContainer> targetOptional = new TargetContainerSupplier().get().stream().filter(x -> x.getId().equals(targetId)).findFirst();
        if (!targetOptional.isPresent()) {
            flashMessage(context, String.format("Target config NOT found: %s", sourceId), FlashScopeUtil.FlashMessageStatus.ERROR);
            return;
        }

        gen(context, sourceOptional.get(), targetOptional.get());
    }

    private void gen(RoutingContext context, SourceContainer sourceContainer, TargetContainer targetContainer) {
        PackageHelper ph = new PackageHelper(Thread.currentThread().getContextClassLoader());

        try {
            SourceConfig source = Objects.requireNonNull(sourceContainer.getConfig());
            TargetConfig target = Objects.requireNonNull(targetContainer.getConfig());
            log.infof("遍历包: %s", source.packageName);

            List<JavaFileObject> javaFileObjects = Objects.requireNonNull(ph.find(source.packageName));
            if (javaFileObjects.isEmpty()) {
                log.info("没有找到包下面的类.");
                return;
            }

            for (JavaFileObject o: javaFileObjects) {
                if (o.getKind() != JavaFileObject.Kind.CLASS && o.getKind() != JavaFileObject.Kind.SOURCE) {
                    continue;
                }
                if (o.getName().contains("$")) {
                    continue;
                }

                Path basePath = Paths.get(System.getProperty("user.dir"));
                log.infof("基础目录为: %s", basePath.toString());

                // Source 文件
                Path sourceFile = Paths.get(o.toUri());
                log.infof("Source文件: %s", sourceFile.toString());
                String entityName = Pathx.getFileRawName(sourceFile);
                log.infof("Entity: %s", entityName);
                String relativePackage = source.getRelativePackage();

                // Target 目录
                Path targetPath = basePath
                        .resolve("src/main/java")
                        .resolve(relativePackage.replace('.', File.separatorChar))
                        .resolve(target.relative.replace('.', File.separatorChar))
                        ;
                Files.createDirectories(targetPath);
                log.infof("Target目录: %s", targetPath.toString());

                // Target 文件
                Path targetFile = targetPath.resolve(String.format("%s%s", entityName, target.postfix));
                log.infof("Target文件: %s", targetFile.toString());

                // 渲染模板
                BiFunction<String, Object, String> renderer = DevConsoleManager.getGlobal(QuteDevConsoleRecorder.RENDER_HANDLER);
                String s = renderer.apply(target.template, Map.ofEntries(
                        Map.entry("item", new TemplateItem(
                                source.packageName,
                                String.format("%s.%s", relativePackage, target.relative),
                                entityName
                        ))
                ));

                // 写入Target文件
                // log.info(s);
                try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
                    writer.write(s, 0, s.length());
                    writer.flush();
                }
            }

        }
        catch (Throwable t) {
            context.fail(t);
        }
    }

//    public void genSource(RoutingContext context, CodegenContainer container, String template, String pathname, String postfix) {
//        PackageHelper ph = new PackageHelper(Thread.currentThread().getContextClassLoader());
//        String entityPackageName = String.format("%s.%s", container.getConfig().packageName, container.getConfig().entityDir);
//        log.infof("遍历包: %s", entityPackageName);
//        try {
//            List<JavaFileObject> javaFileObjects = ph.find(entityPackageName);
//            Objects.requireNonNull(javaFileObjects);
//            if (javaFileObjects.size() == 0) {
//                log.info("没有找到包下面的类.");
//            }
//            for (JavaFileObject o: javaFileObjects) {
//                log.info(o.getName());
//                if (o.getKind() != JavaFileObject.Kind.CLASS && o.getKind() != JavaFileObject.Kind.SOURCE) {
//                    continue;
//                }
//                if (o.getName().contains("$")) {
//                    continue;
//                }
//
//                String binaryName = ((CustomJavaFileObject) o).binaryName();
//                log.infof("当前Entity为: %s", binaryName);
//                String filepath=System.getProperty("user.dir");
//                log.infof("当前目录为: %s", filepath);
//                String rpackage = binaryName.substring(0, binaryName.lastIndexOf("."));
//                rpackage = rpackage.substring(0, rpackage.lastIndexOf("."));
//                rpackage = String.format("%s.%s", rpackage, pathname);
//                log.infof("R包名为: %s", filepath);
//
//                // 组合新路径
//                String p0 = binaryName.replace('.', '\\');
//                String p1 = String.format("%s\\src\\main\\java\\%s", filepath, p0);
//
//                // 转换为Path
//                Path path = Paths.get(p1);
//                String entityName = path.getFileName().toString();
//                path = path.getParent().getParent();
//
//                // R目录
//                Path rpath = path.resolve(pathname);
//                Files.createDirectories(rpath);
//                log.infof("rpath: %s", rpath.toString());
//
//                // R文件
//                Path rfile = rpath.resolve(String.format("%s%s", entityName, postfix));
//                log.infof("rfile: %s", rfile.toString());
//
//                try {
//                    BiFunction<String, Object, String> renderer = DevConsoleManager.getGlobal(QuteDevConsoleRecorder.RENDER_HANDLER);
//                    String s = renderer.apply(template, Map.ofEntries(
//                            Map.entry("package", rpackage),
//                            Map.entry("entityPackage", entityPackageName),
//                            Map.entry("entity", entityName)
//                    ));
//                    // log.info(s);
//                    BufferedWriter writer = Files.newBufferedWriter(rfile, StandardCharsets.UTF_8);
//                    writer.write(s,0, s.length());
//                    writer.flush();
//                } catch (Throwable e) {
//                    // flashMessage(context, String.format("Render %s failed", operation), FlashScopeUtil.FlashMessageStatus.ERROR);
//                    context.fail(e);
//                }
//
//                flashMessage(context, String.format("%s generated", rfile.toString()));
//            }
//        }
//        catch (Exception e) {
//            log.error("遍历失败", e);
//        }
//    }

}
