package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.datasource.common.runtime.DataSourceUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class CodegenRecorderTest {

    @Test
    public void test_01() throws IOException {
        // 1.获取当前文件所在的路径
        String p1 = this.getClass().getResource("").getPath();
        // 2.获取再 target 下 classpath 路径
        String p2 = this.getClass().getResource("/").getPath();
        // 3.也是获取 classpath 的绝对路径
        String p3 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        // 4.也是获取 classpath 的绝对路径
        String p4 = this.getClass().getClassLoader().getResource("").getPath();
        // 5.也是获取 classpath 的绝对路径
        String p5 = ClassLoader.getSystemResource("").getPath();
        // 6.获取当前项目路径（此方法与 7 效果相同，但是可以将路径转为标准形式，会处理“.”和“..”）
        String p6 = new File("").getCanonicalPath();
        // 7.获取项目绝对路径（不会处理“.”和“..”）
        String p7 = new File("").getAbsolutePath();

        CodegenRecorder recorder = new CodegenRecorder();
        CodegenConfig config = new CodegenConfig();
        config.packageName = this.getClass().getPackageName();
        config.entityDir = "entity";
        CodegenContainer container = new CodegenContainer(DataSourceUtil.DEFAULT_DATASOURCE_NAME, config);

        recorder.genSource(container);
    }
}
