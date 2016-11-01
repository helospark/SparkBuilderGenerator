package com.helospark.spark.converter.handlers.service.domain;

import javax.annotation.Generated;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;

public class ConvertableDomainParameter {
    private TemplatedIType sourceType;
    private TemplatedIType destinationType;
    private ConvertType type;
    private String sourceParameterName;
    private String destinationParameterName;
    private String sourceMethodName;
    private String destinationMethodName;

    @Generated("SparkTools")
    private ConvertableDomainParameter(Builder builder) {
        this.sourceType = builder.sourceType;
        this.destinationType = builder.destinationType;
        this.type = builder.type;
        this.sourceParameterName = builder.sourceParameterName;
        this.destinationParameterName = builder.destinationParameterName;
        this.sourceMethodName = builder.sourceMethodName;
        this.destinationMethodName = builder.destinationMethodName;
    }

    public TemplatedIType getSourceType() {
        return sourceType;
    }

    public TemplatedIType getDestinationType() {
        return destinationType;
    }

    public ConvertType getType() {
        return type;
    }

    public String getSourceParameterName() {
        return sourceParameterName;
    }

    public String getDestinationParameterName() {
        return destinationParameterName;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public String getDestinationMethodName() {
        return destinationMethodName;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private TemplatedIType sourceType;
        private TemplatedIType destinationType;
        private ConvertType type;
        private String sourceParameterName;
        private String destinationParameterName;
        private String sourceMethodName;
        private String destinationMethodName;

        private Builder() {
        }

        public Builder withSourceType(TemplatedIType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder withDestinationType(TemplatedIType destinationType) {
            this.destinationType = destinationType;
            return this;
        }

        public Builder withType(ConvertType type) {
            this.type = type;
            return this;
        }

        public Builder withSourceParameterName(String sourceParameterName) {
            this.sourceParameterName = sourceParameterName;
            return this;
        }

        public Builder withDestinationParameterName(String destinationParameterName) {
            this.destinationParameterName = destinationParameterName;
            return this;
        }

        public Builder withSourceMethodName(String sourceMethodName) {
            this.sourceMethodName = sourceMethodName;
            return this;
        }

        public Builder withDestinationMethodName(String destinationMethodName) {
            this.destinationMethodName = destinationMethodName;
            return this;
        }

        public ConvertableDomainParameter build() {
            return new ConvertableDomainParameter(this);
        }
    }

}
