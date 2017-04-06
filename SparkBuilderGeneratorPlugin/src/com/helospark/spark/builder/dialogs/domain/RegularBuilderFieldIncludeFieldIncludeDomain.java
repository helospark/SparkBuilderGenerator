package com.helospark.spark.builder.dialogs.domain;

public class RegularBuilderFieldIncludeFieldIncludeDomain {
    private boolean includeField;
    private String fieldName;

    public RegularBuilderFieldIncludeFieldIncludeDomain(boolean includeField, String fieldName) {
        this.includeField = includeField;
        this.fieldName = fieldName;
    }

    public boolean isIncludeField() {
        return includeField;
    }

    public void setIncludeField(boolean includeField) {
        this.includeField = includeField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
