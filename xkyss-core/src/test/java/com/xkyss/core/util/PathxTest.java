package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

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

    @Test
    public void test_03() {
        String packageName = "com.aj.frame.yfty.platform.entity.User";
        String filepath=System.getProperty("user.dir");

        Path p = Paths.get(System.getProperty("user.dir"))
                .resolve("src/main/java")
                .resolve(packageName.replace('.', File.separatorChar))
                ;

        Assertions.assertTrue(p.endsWith("src/main/java/com/aj/frame/yfty/platform/entity/User"));
    }

    @Test
    public void test_04() {
        String packageName = "com.aj.frame.yfty.platform.entity.User";
        Integer relativeDirDepth = 1;
        String p = getRelativePackage(packageName, relativeDirDepth);
        Assertions.assertEquals("com.aj.frame.yfty.platform.entity", p);
    }

    @Test
    public void test_05() {
        String o = "E:/xk/Code/zyaj/jwt_v3/ydjwv3/trunk/working/gits/Yfty/quarks/yfty-common/target/classes/com/aj/frame/yfty/platform/entity/User.class";
        Path p = Paths.get(o);
    }

    public String getRelativePackage(String packageName, Integer relativeDirDepth) {
        if (packageName == null || packageName.length() == 0) {
            return packageName;
        }
        if (relativeDirDepth == null || relativeDirDepth == 0) {
            return packageName;
        }

        String ret = packageName;
        for (int i = 0; i<relativeDirDepth; i++) {
            int dotIndex = ret.lastIndexOf('.');
            if (dotIndex <= 0) {
                break;
            }
            ret = ret.substring(0, dotIndex);
        }

        return ret;
    }

    @Test
    public void test_get() {
        Assertions.assertEquals(Paths.get("a/b/c/d"), Pathx.get("a.b.c.d", '.'));
        Assertions.assertEquals(
                Paths.get("a/b/c/d", "e/f", "g"),
                Pathx.get("a.b.c.d", '.', "e.f", "g"));
    }

    @Test
    public void test_extension()
    {
        Assertions.assertEquals(Paths.get("a/b/c/d.txt"), Pathx.extension(Paths.get("a/b/c/d"), "txt"));
    }
}
