package com.xkyss.extensions.java.lang.String;


import com.xkyss.extensions.constant.Constants;
import com.xkyss.extensions.java.lang.CharSequence.CharSequenceExt;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@SuppressWarnings("DuplicatedCode")
@Extension
public class StringExt extends CharSequenceExt {

    /**
     * 转换字符串为boolean值
     *
     * @param self 字符串
     * @return boolean值
     */
    public static boolean toBoolean(@This String self) {
        if (self.isBlank()) {
            self = self.trim().toLowerCase();
            return Constants.TRUE_SET.contains(self);
        }
        return false;
    }
}
