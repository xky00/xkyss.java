package com.xkyss.quarkus.codegen.runtime.model;


import com.xkyss.core.naming.Converter;

import java.util.ArrayList;
import java.util.List;

public class Table {

    /**
     * 类别
     */
    private String catalog;

    /**
     * Schema
     */
    private String schema;

    /**
     * 表名
     */
    private String name;

    /**
     * 匹配后的表名
     */
    private String matchedName;

    /**
     * 备注(注释)
     */
    private String remarks;

    /**
     * 字段
     */
    private List<Column> columns = new ArrayList<>();

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public String getNameCamel() {
        return Converter.fromMacro(matchedName).toCamel();
    }

    public String getNamePascal() {
        return Converter.fromMacro(matchedName).toPascal();
    }

    public String getNameMacro() {
        return Converter.fromMacro(matchedName).toMacro();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchedName() {
        return matchedName;
    }

    public void setMatchedName(String matchedName) {
        this.matchedName = matchedName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Column getColumn(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        if (columns == null || columns.size() == 0) {
            return null;
        }

        for (Column c: columns) {
            if (name.equalsIgnoreCase(c.getName())) {
                return c;
            }
        }

        return null;
    }

    public void addColumn(Column column) {
        if (column == null) {
            return;
        }
        columns.add(column);
    }

    public boolean isComposite() {
        int count = 0;
        for (Column c: columns) {
            if (c.isPrimary()) {
                count++;
                if (count > 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
