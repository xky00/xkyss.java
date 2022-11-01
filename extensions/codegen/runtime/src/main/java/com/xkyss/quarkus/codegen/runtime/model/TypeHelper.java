package com.xkyss.quarkus.codegen.runtime.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

@SuppressWarnings("DuplicateBranchesInSwitch")
public class TypeHelper {

    public static String javaTypeOf(ColumnType type) {
        switch (type) {
            case Null: return "null";
            case Boolean:
            case Bit: return Boolean.class.getName();
            case TinyInt: return Byte.class.getName();
            case SmallInt: return Short.class.getName();
            case Integer: return Integer.class.getName();
            case BigInt: return Long.class.getName();
            case Float: return Float.class.getName();
//            case Real: return Double.class.getName(); // 不确定
            case Double: return Double.class.getName();
//            case Numeric: return Number.class.getName();
            case Decimal: return BigDecimal.class.getName();
            case Char:
            case VarChar: return String.class.getName();
//            case LongVarChar: return LongVarChar;
            case Date: return Date.class.getName();
            // 与常规的Mysql-Java类型对应不一致
            case Time: return Date.class.getName();
            case TimeStamp: return Date.class.getName();
//            case Time: return Time.class.getName();
//            case TimeStamp: return Timestamp.class.getName();
            default: return "Unknown";
        }
    }

    public static String javaSimpleTypeOf(ColumnType type) {
        switch (type) {
            case Null: return "null";
            case Boolean:
            case Bit: return Boolean.class.getSimpleName();
            case TinyInt: return Byte.class.getSimpleName();
            case SmallInt: return Short.class.getSimpleName();
            case Integer: return Integer.class.getSimpleName();
            case BigInt: return Long.class.getSimpleName();
            case Float: return Float.class.getSimpleName();
//            case Real: return Double.class.getSimpleName(); // 不确定
            case Double: return Double.class.getSimpleName();
//            case Numeric: return Number.class.getSimpleName();
            case Decimal: return BigDecimal.class.getSimpleName();
            case Char:
            case VarChar: return String.class.getSimpleName();
//            case LongVarChar: return LongVarChar;
            case Date: return Date.class.getSimpleName();
            // 与常规的Mysql-Java类型对应不一致
            case Time: return Date.class.getSimpleName();
            case TimeStamp: return Date.class.getSimpleName();
//            case Time: return Time.class.getSimpleName();
//            case TimeStamp: return Timestamp.class.getSimpleName();
            default: return "Unknown";
        }
    }
}
