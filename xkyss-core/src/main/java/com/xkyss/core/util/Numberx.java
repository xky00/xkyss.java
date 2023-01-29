package com.xkyss.core.util;

import static org.apache.commons.lang3.Validate.isTrue;

public class Numberx {

    /**
     * 一个正整数可以被分成几份(向上取整)
     * @param total 总数
     * @param len 每份数量
     * @return 多少份
     */
    public static int ceil(int total, int len) {
        isTrue(total > 0);
        isTrue(len > 0);

        if (total <= len) {
            return 1;
        }

        int x = total / len;
        if (total % len > 0) {
            x++;
        }

        return x;
    }

    /**
     * 将一个正整数分成几份(向上取整)
     * @param total 总数
     * @param len 每份数量
     * @return 切分数组
     */
    public static int[] split(int total, int len) {
        isTrue(total > 0);
        isTrue(len > 0);

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
