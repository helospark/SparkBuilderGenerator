package com.helospark.spark.converter.handlers.service.domain;

import java.util.List;

import javax.annotation.Generated;

import org.eclipse.jdt.core.IType;

public class ConvertableDomain {
    private List<ConvertableDomainParameter> convertableDomainParameters;
    private boolean useBuilder;
    private IType sourceObject;
    private IType destinationObject;

    @Generated("SparkTools")
    private ConvertableDomain(Builder builder) {
        this.convertableDomainParameters = builder.convertableDomainParameters;
        this.useBuilder = builder.useBuilder;
        this.sourceObject = builder.sourceObject;
        this.destinationObject = builder.destinationObject;
    }

    public List<ConvertableDomainParameter> getConvertableDomainParameters() {
        return convertableDomainParameters;
    }

    public boolean isUseBuilder() {
        return useBuilder;
    }

    public IType getSourceObject() {
        return sourceObject;
    }

    public IType getDestinationObject() {
        return destinationObject;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private List<ConvertableDomainParameter> convertableDomainParameters;
        private boolean useBuilder;
        private IType sourceObject;
        private IType destinationObject;

        private Builder() {
        }

        public Builder withConvertableDomainParameters(List<ConvertableDomainParameter> convertableDomainParameters) {
            this.convertableDomainParameters = convertableDomainParameters;
            return this;
        }

        public Builder withUseBuilder(boolean useBuilder) {
            this.useBuilder = useBuilder;
            return this;
        }

        public Builder withSourceObject(IType sourceObject) {
            this.sourceObject = sourceObject;
            return this;
        }

        public Builder withDestinationObject(IType destinationObject) {
            this.destinationObject = destinationObject;
            return this;
        }

        public ConvertableDomain build() {
            return new ConvertableDomain(this);
        }
    }
}
