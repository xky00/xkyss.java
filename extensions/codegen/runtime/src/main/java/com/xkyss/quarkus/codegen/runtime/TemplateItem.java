package com.xkyss.quarkus.codegen.runtime;

public class TemplateItem {
    private String sourcePackage;
    private String targetPackage;
    private String entity;

    public TemplateItem() {

    }

    public TemplateItem(String sourcePackage, String targetPackage, String entity) {
        this.sourcePackage = sourcePackage;
        this.targetPackage = targetPackage;
        this.entity = entity;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public void setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
