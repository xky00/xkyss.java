package com.xkyss.core.util;

public class Numberx {
    public static boolean isAll(Number x, Number... numbers) {
        if (x == null) {
            return false;
        }

        for (Number n: numbers) {
            if (!x.equals(n)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllZero(Number... numbers) {
        for (Number n: numbers) {
            if (n != null && n.hashCode() != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 一个正整数可以被分成几份(向上取整)
     * @param total 总数
     * @param len 每份数量
     * @return 多少份
     */
    public static int ceil(int total, int len) {
        Checkx.isTrue(total > 0);
        Checkx.isTrue(len > 0);

        if (total <= len) {
            return 1;
        }

        int x = total / len;
        if (total % len > 0) {
            x++;
        }

        return x;
    }

    public static int[] split(int total, int len) {
        Checkx.isTrue(total > 0);
        Checkx.isTrue(len > 0);

        if (total <= len) {
            return new int[] { total };
        }

        int size = ceil(total, len);
        int[] ret = new int[size];
        for (int i=0; i<size; i++) {
            int remain = total - len * (i + 1);
            ret[i] = (remain >= 0) ? len : (remain + len);
        }

        return ret;
    }
}
