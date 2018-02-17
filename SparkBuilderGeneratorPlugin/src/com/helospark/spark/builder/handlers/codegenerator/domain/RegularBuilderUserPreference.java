package com.helospark.spark.builder.handlers.codegenerator.domain;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

public class RegularBuilderUserPreference {
    private boolean generateCopyMethod;
    private List<BuilderField> builderFields;

    @Generated("SparkTools")
    private RegularBuilderUserPreference(Builder builder) {
        this.generateCopyMethod = builder.generateCopyMethod;
        this.builderFields = builder.builderFields;
    }

    public boolean isGenerateCopyMethod() {
        return generateCopyMethod;
    }

    public List<BuilderField> getBuilderFields() {
        return builderFields;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private boolean generateCopyMethod;
        private List<BuilderField> builderFields = Collections.emptyList();

        private Builder() {
        }

        public Builder withGenerateCopyMethod(boolean generateCopyMethod) {
            this.generateCopyMethod = generateCopyMethod;
            return this;
        }

        public Builder withBuilderFields(List<BuilderField> builderFields) {
            this.builderFields = builderFields;
            return this;
        }

        public RegularBuilderUserPreference build() {
            return new RegularBuilderUserPreference(this);
        }
    }

}
