package com.helospark.spark.builder.handlers.codegenerator.domain;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.Type;

/**
 * Domain object representing a field that can be set via setter into super class.
 * @author helospark
 */
public class SuperSetterBasedBuilderField extends BuilderField {
    private String setterName;

    @Generated("SparkTools")
    private SuperSetterBasedBuilderField(Builder builder) {
        this.fieldType = builder.fieldType;
        this.originalFieldName = builder.originalFieldName;
        this.builderFieldName = builder.builderFieldName;
        this.setterName = builder.setterName;
    }

    public String getSetterName() {
        return setterName;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private Type fieldType;
        private String originalFieldName;
        private String builderFieldName;
        private String setterName;

        private Builder() {
        }

        public Builder withFieldType(Type fieldType) {
            this.fieldType = fieldType;
            return this;
        }

        public Builder withOriginalFieldName(String originalFieldName) {
            this.originalFieldName = originalFieldName;
            return this;
        }

        public Builder withBuilderFieldName(String builderFieldName) {
            this.builderFieldName = builderFieldName;
            return this;
        }

        public Builder withSetterName(String setterName) {
            this.setterName = setterName;
            return this;
        }

        public SuperSetterBasedBuilderField build() {
            return new SuperSetterBasedBuilderField(this);
        }
    }
}
