package com.xkyss.quarkus.codegen.runtime.model;

import java.sql.Types;

public enum ColumnType {
    Unknown,

    BigInt,
    Bit,
    Blob,
    Boolean,
    Char,
    Clob,
    Date,
    Decimal,
    Double,
    Float,
    Integer,
    LongVarBinary,
    LongVarChar,
    NChar,
    NClob,
    NVarChar,
    LongNVarChar,
    Null,
    Numeric,
    Real,
    Ref,
    SmallInt,
    Time,
    TimeStamp,
    TinyInt,
    VarBinary,
    VarChar
    ;

    public static ColumnType ofSqlType(int t) {

        switch (t) {
            case Types.NULL: return Null;
            case Types.BOOLEAN: return Boolean;
            case Types.BIT: return Bit;
            case Types.TINYINT: return TinyInt;
            case Types.SMALLINT: return SmallInt;
            case Types.INTEGER: return Integer;
            case Types.BIGINT: return BigInt;
            case Types.FLOAT: return Float;
            case Types.REAL: return Real;
            case Types.DOUBLE: return Double;
            case Types.NUMERIC: return Numeric;
            case Types.DECIMAL: return Decimal;
            case Types.CHAR: return Char;
            case Types.VARCHAR: return VarChar;
            case Types.LONGVARCHAR: return LongVarChar;
            case Types.DATE: return Date;
            case Types.TIME: return Time;
            case Types.TIMESTAMP: return TimeStamp;
            default: return Unknown;
        }
    }
}
