package com.helospark.spark.builder.handlers.codegenerator.domain;

import org.eclipse.jdt.core.dom.Type;

/**
 * Domain object representing a field in the builder.
 * @author helospark
 */
public class BuilderField {
    protected Type fieldType;
    protected String originalFieldName;
    protected String builderFieldName;

    public Type getFieldType() {
        return fieldType;
    }

    public String getOriginalFieldName() {
        return originalFieldName;
    }

    public String getBuilderFieldName() {
        return builderFieldName;
    }

}
