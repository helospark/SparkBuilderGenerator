package com.helospark.spark.builder.dialogs;

public class StagedBuilderStagePropertiesDialogResult {
    private boolean selected;
    private String fieldName;
    private int originalIndex;

    public StagedBuilderStagePropertiesDialogResult(boolean isMandatory, String fieldName, int originalIndex) {
        this.selected = isMandatory;
        this.fieldName = fieldName;
        this.originalIndex = originalIndex;
    }

    public boolean isMandatory() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

}
