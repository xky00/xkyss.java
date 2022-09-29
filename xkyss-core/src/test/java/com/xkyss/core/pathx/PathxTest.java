package com.xkyss.core.pathx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathxTest {

    /**
     * https://zq99299.github.io/java-tutorial/essential/io/dirs.html
     */
    @Test
    public void path_test_01() throws IOException {
        String pathname = "repository";
        String postfix = "Repository.java";

        // 包名
        String binaryName = "com.aj.frame.yfty.platform.entity.User";
        // System.getProperty("user.dir")
        String filepath = "E:\\xk\\Code\\zyaj\\jwt_v3\\ydjwv3\\trunk\\working\\gits\\Yfty\\quarks\\yfty-migration";

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
        System.out.println(rfile.toString());

        // 写入文本
        String s = "Hello";
        try (BufferedWriter writer = Files.newBufferedWriter(rfile, StandardCharsets.UTF_8)){
            writer.write(s,0, s.length());
        } catch(IOException e){
            System.err.format("IOException：%s%n", e);
        }
    }

    @Test
    public void test_02() {
        // 包名
        String binaryName = "com.aj.frame.yfty.platform.entity.User";
        String rpackage = binaryName.substring(0, binaryName.lastIndexOf("."));
        rpackage = rpackage.substring(0, rpackage.lastIndexOf("."));
        rpackage = String.format("%s.%s", rpackage, "repository");

        Assertions.assertEquals("com.aj.frame.yfty.platform.repository", rpackage);
    }
}
