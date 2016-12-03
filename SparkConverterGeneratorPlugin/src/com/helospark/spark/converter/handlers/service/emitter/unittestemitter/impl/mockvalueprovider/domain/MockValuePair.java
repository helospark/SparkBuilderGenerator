package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;

public class MockValuePair {
    private Expression sourceExpression;
    private Expression destinationExpression;
    private boolean isSourceSameAsDestination;
    private ConverterTypeCodeGenerationRequest convertingDependency;
    private ConvertableDomainParameter parameterProperties;

    @Generated("SparkTools")
    private MockValuePair(Builder builder) {
        this.sourceExpression = builder.sourceExpression;
        this.destinationExpression = builder.destinationExpression;
        this.isSourceSameAsDestination = builder.isSourceSameAsDestination;
        this.convertingDependency = builder.convertingDependency;
        this.parameterProperties = builder.parameterProperties;
    }

    public Expression getSourceExpression() {
        return sourceExpression;
    }

    public Expression getDestinationExpression() {
        return destinationExpression;
    }

    public ConvertableDomainParameter getParameterProperties() {
        return parameterProperties;
    }

    public boolean isSourceSameAsDestination() {
        return isSourceSameAsDestination;
    }

    public ConverterTypeCodeGenerationRequest getConvertingDependency() {
        return convertingDependency;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private Expression sourceExpression;
        private Expression destinationExpression;
        private boolean isSourceSameAsDestination;
        private ConverterTypeCodeGenerationRequest convertingDependency;
        private ConvertableDomainParameter parameterProperties;

        private Builder() {
        }

        public Builder withSourceExpression(Expression sourceExpression) {
            this.sourceExpression = sourceExpression;
            return this;
        }

        public Builder withDestinationExpression(Expression destinationExpression) {
            this.destinationExpression = destinationExpression;
            return this;
        }

        public Builder withIsSourceSameAsDestination(boolean isSourceSameAsDestination) {
            this.isSourceSameAsDestination = isSourceSameAsDestination;
            return this;
        }

        public Builder withConvertingDependency(ConverterTypeCodeGenerationRequest convertingDependency) {
            this.convertingDependency = convertingDependency;
            return this;
        }

        public Builder withParameterProperties(ConvertableDomainParameter parameterProperties) {
            this.parameterProperties = parameterProperties;
            return this;
        }

        public MockValuePair build() {
            return new MockValuePair(this);
        }
    }

}
