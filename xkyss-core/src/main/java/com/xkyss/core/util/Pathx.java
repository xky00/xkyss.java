package com.xkyss.core.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Pathx {
    public static Path get(String first, char separator, String... more) {
        if (more.length == 0) {
            return Paths.get(replaceSeparator(first, separator));
        }
        else {
            return Paths.get(
                    replaceSeparator(first, separator),
                    Arrays.stream(more)
                            .map(x -> replaceSeparator(x, separator))
                            .toArray(String[]::new)
            );
        }
    }

    public static Path extension(Path p, String ext) {
        if (p == null) {
            return null;
        }

        return Paths.get(String.format("%s.%s", p.toString(), ext));
    }

    public static String getFileRawName(Path p) {
        if (p == null) {
            return "";
        }

        String filename = p.getFileName().toString();
        int i = filename.lastIndexOf('.');
        if (i < 0) {
            return filename;
        }

        return filename.substring(0, i);
    }

    public static String getFileExt(Path p) {
        if (p == null) {
            return "";
        }

        String filename = p.getFileName().toString();
        int i = filename.lastIndexOf('.');
        if (i < 0) {
            return filename;
        }

        return filename.substring(i+1);
    }

    private static String replaceSeparator(String s, char separator) {
        return emptyIfNull(s).replace(separator, File.separatorChar);
    }

    private static String emptyIfNull(String s) {
        return (s == null) ? "" : s;
    }
}
