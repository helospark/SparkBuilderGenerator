package com.helospark.spark.builder.handlers.codegenerator.domain;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

/**
 * Domain object representing a field in the builder that is setting a class field.
 * @author helospark
 */
public class ClassFieldSetterBuilderField extends BuilderField {
    private FieldDeclaration fieldDeclaration;

    @Generated("SparkTools")
    private ClassFieldSetterBuilderField(Builder builder) {
        this.fieldType = builder.fieldType;
        this.originalFieldName = builder.originalFieldName;
        this.builderFieldName = builder.builderFieldName;
        this.fieldDeclaration = builder.fieldDeclaration;
    }

    public FieldDeclaration getFieldDeclaration() {
        return fieldDeclaration;
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
        private FieldDeclaration fieldDeclaration;

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

        public Builder withFieldDeclaration(FieldDeclaration fieldDeclaration) {
            this.fieldDeclaration = fieldDeclaration;
            return this;
        }

        public ClassFieldSetterBuilderField build() {
            return new ClassFieldSetterBuilderField(this);
        }
    }

}
