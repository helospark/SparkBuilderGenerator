package com.helospark.spark.builder.handlers.codegenerator.domain;

import java.util.Optional;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.InstanceFieldAccessStrategy;

/**
 * Domain object representing a field in the builder.
 * @author helospark
 */
public class BuilderField {
    protected Type fieldType;
    protected String originalFieldName;
    protected String builderFieldName;
    protected boolean mandatory = false;
    protected Expression defaultValue;
    protected Optional<InstanceFieldAccessStrategy> originalFieldAccessStrategy;

    public Type getFieldType() {
        return fieldType;
    }

    public String getOriginalFieldName() {
        return originalFieldName;
    }

    public String getBuilderFieldName() {
        return builderFieldName;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Optional<InstanceFieldAccessStrategy> getOriginalFieldAccessStrategy() {
        return originalFieldAccessStrategy;
    }

}
