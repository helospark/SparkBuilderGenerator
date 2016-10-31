package com.helospark.spark.converter.handlers.domain;

import javax.annotation.Generated;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

public class ConverterInputParameters {
    private IType sourceType;
    private IType destinationType;
    private String destinationPackageFragment;
    private Boolean recursiveGeneration;
    private IJavaProject javaProject;

    @Generated("SparkTools")
    private ConverterInputParameters(Builder builder) {
        this.sourceType = builder.sourceType;
        this.destinationType = builder.destinationType;
        this.destinationPackageFragment = builder.destinationPackageFragment;
        this.recursiveGeneration = builder.recursiveGeneration;
        this.javaProject = builder.javaProject;
    }

    public IType getSourceType() {
        return sourceType;
    }

    public IType getDestinationType() {
        return destinationType;
    }

    public String getDestinationPackageFragment() {
        return destinationPackageFragment;
    }

    public Boolean getRecursiveGeneration() {
        return recursiveGeneration;
    }

    public IJavaProject getJavaProject() {
        return javaProject;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private IType sourceType;
        private IType destinationType;
        private String destinationPackageFragment;
        private Boolean recursiveGeneration;
        private IJavaProject javaProject;

        private Builder() {
        }

        public Builder withSourceType(IType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder withDestinationType(IType destinationType) {
            this.destinationType = destinationType;
            return this;
        }

        public Builder withDestinationPackageFragment(String destinationPackageFragment) {
            this.destinationPackageFragment = destinationPackageFragment;
            return this;
        }

        public Builder withRecursiveGeneration(Boolean recursiveGeneration) {
            this.recursiveGeneration = recursiveGeneration;
            return this;
        }

        public Builder withJavaProject(IJavaProject javaProject) {
            this.javaProject = javaProject;
            return this;
        }

        public ConverterInputParameters build() {
            return new ConverterInputParameters(this);
        }
    }

}
