package com.xkyss.quarkus.codegen.runtime.model;

import com.xkyss.experimental.naming.Converter;

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

    /**
     * 表名转换器
     */
    private Converter nameConverter = Converter.EMPTY;

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

    public String getNamePascal() {
        return nameConverter.toPascal();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchedName() {
        return matchedName;
    }

    public void setMatchedName(String matchedName) {
        this.matchedName = matchedName;
        this.nameConverter = Converter.fromAuto(this.matchedName);
    }

    public Converter getNameConverter() {
        return nameConverter;
    }

    public void setNameConverter(Converter nameConverter) {
        this.nameConverter = nameConverter;
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
}
