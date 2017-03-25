package com.helospark.spark.builder.dialogs;

/**
 * Result of the staged builder property change dialog.
 * @author helospark
 */
public class StagedBuilderStagePropertiesDialogResult {
    private boolean mandatory;
    private String fieldName;
    private int originalIndex;

    public StagedBuilderStagePropertiesDialogResult(boolean isMandatory, String fieldName, int originalIndex) {
        this.mandatory = isMandatory;
        this.fieldName = fieldName;
        this.originalIndex = originalIndex;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean selected) {
        this.mandatory = selected;
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
