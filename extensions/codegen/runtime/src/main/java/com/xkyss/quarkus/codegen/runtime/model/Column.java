package com.xkyss.quarkus.codegen.runtime.model;


import com.xkyss.core.naming.Converter;

public class Column {
    private String name;
    private ColumnType type;
    private int size;
    private boolean nullable;
    private boolean isPrimary;
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getJavaType() {
        return TypeHelper.javaTypeOf(type);
    }

    public String getJavaSimpleType() {
        return TypeHelper.javaSimpleTypeOf(type);
    }

    public String getNameCamel() {
        return Converter.fromMacro(name).toCamel();
    }

    public String getNamePascal() {
        return Converter.fromMacro(name).toPascal();
    }

    public String getNameMacro() {
        return Converter.fromMacro(name).toMacro();
    }
}
