package com.helospark.spark.builder.handlers.codegenerator.domain;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.FieldDeclaration;

public class NamedVariableDeclarationField {
    private FieldDeclaration fieldDeclaration;
    private String originalFieldName;
    private String builderFieldName;

    @Generated("SparkTools")
    private NamedVariableDeclarationField(Builder builder) {
        this.fieldDeclaration = builder.fieldDeclaration;
        this.originalFieldName = builder.originalFieldName;
        this.builderFieldName = builder.builderFieldName;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    public FieldDeclaration getFieldDeclaration() {
        return fieldDeclaration;
    }

    public String getOriginalFieldName() {
        return originalFieldName;
    }

    public String getBuilderFieldName() {
        return builderFieldName;
    }

    @Generated("SparkTools")
    public static final class Builder {
        private FieldDeclaration fieldDeclaration;
        private String originalFieldName;
        private String builderFieldName;

        private Builder() {
        }

        public Builder withFieldDeclaration(FieldDeclaration fieldDeclaration) {
            this.fieldDeclaration = fieldDeclaration;
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

        public NamedVariableDeclarationField build() {
            return new NamedVariableDeclarationField(this);
        }
    }

}
