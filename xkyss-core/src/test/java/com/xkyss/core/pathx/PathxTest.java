package com.xkyss.core.pathx;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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
        // 包名
        String binaryName = "com.aj.frame.yfty.platform.entity.User";
        // System.getProperty("user.dir")
        String filepath = "E:\\xk\\Code\\zyaj\\jwt_v3\\ydjwv3\\trunk\\working\\gits\\Yfty\\quarks\\yfty-migration";

        // 组合新路径
        String p0 = binaryName.replace('.', '\\');
        String p1 = String.format("%s\\src\\main\\java\\%s", filepath, p0);

        // 转换位Path
        Path path = Paths.get(p1);
        String entityName = path.getFileName().toString();
        path = path.getParent().getParent();

        // 确保Repository目录存在
        path = path.resolve("repository");
        Files.createDirectories(path);

        // Repository java文件
        Path repositoryFile = path.resolve(String.format("%sRepository.java", entityName));

        // 写入文本
        String s = "Hello";
        try (BufferedWriter writer = Files.newBufferedWriter(repositoryFile, StandardCharsets.UTF_8)){
            writer.write(s,0, s.length());
        } catch(IOException e){
            System.err.format("IOException：%s%n", e);
        }
    }
}
