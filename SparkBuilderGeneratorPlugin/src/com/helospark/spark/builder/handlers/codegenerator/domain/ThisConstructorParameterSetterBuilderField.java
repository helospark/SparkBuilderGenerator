package com.helospark.spark.builder.handlers.codegenerator.domain;

import java.util.Optional;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.InstanceFieldAccessStrategy;

/**
 * Domain object representing a field in the builder that is a parameter in the this() constructor call.
 * @author helospark
 */
public class ThisConstructorParameterSetterBuilderField extends BuilderField {
    private Integer index;

    @Generated("SparkTools")
    private ThisConstructorParameterSetterBuilderField(Builder builder) {
        this.fieldType = builder.fieldType;
        this.originalFieldName = builder.originalFieldName;
        this.builderFieldName = builder.builderFieldName;
        this.originalFieldAccessStrategy = builder.originalFieldAccessStrategy;
        this.index = builder.index;
    }

    public Integer getIndex() {
        return index;
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
        private Optional<InstanceFieldAccessStrategy> originalFieldAccessStrategy = Optional.empty();
        private Integer index;

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

        public Builder withOriginalFieldAccessStrategy(Optional<InstanceFieldAccessStrategy> originalFieldAccessStrategy) {
            this.originalFieldAccessStrategy = originalFieldAccessStrategy;
            return this;
        }

        public Builder withIndex(Integer index) {
            this.index = index;
            return this;
        }

        public ThisConstructorParameterSetterBuilderField build() {
            return new ThisConstructorParameterSetterBuilderField(this);
        }
    }

}
