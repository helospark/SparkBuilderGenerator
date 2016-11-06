package com.helospark.spark.converter.handlers.domain;

import javax.annotation.Generated;

public class ConverterMethodCodeGenerationRequest {
    private String name;
    private String parameterName;
    private ConverterMethodType converterMethodType;
    private ConverterTypeCodeGenerationRequest containingType;
    private TemplatedIType sourceType;
    private TemplatedIType destinationType;

    @Generated("SparkTools")
    private ConverterMethodCodeGenerationRequest(Builder builder) {
        this.name = builder.name;
        this.parameterName = builder.parameterName;
        this.converterMethodType = builder.converterMethodType;
        this.containingType = builder.containingType;
        this.sourceType = builder.sourceType;
        this.destinationType = builder.destinationType;
    }

    public String getName() {
        return name;
    }

    public String getParameterName() {
        return parameterName;
    }

    public ConverterMethodType getConverterMethodType() {
        return converterMethodType;
    }

    public ConverterTypeCodeGenerationRequest getContainingType() {
        return containingType;
    }

    public TemplatedIType getSourceType() {
        return sourceType;
    }

    public TemplatedIType getDestinationType() {
        return destinationType;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private String name;
        private String parameterName;
        private ConverterMethodType converterMethodType;
        private ConverterTypeCodeGenerationRequest containingType;
        private TemplatedIType sourceType;
        private TemplatedIType destinationType;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withParameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public Builder withConverterMethodType(ConverterMethodType converterMethodType) {
            this.converterMethodType = converterMethodType;
            return this;
        }

        public Builder withContainingType(ConverterTypeCodeGenerationRequest containingType) {
            this.containingType = containingType;
            return this;
        }

        public Builder withSourceType(TemplatedIType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder withDestinationType(TemplatedIType destinationType) {
            this.destinationType = destinationType;
            return this;
        }

        public ConverterMethodCodeGenerationRequest build() {
            return new ConverterMethodCodeGenerationRequest(this);
        }
    }

}
