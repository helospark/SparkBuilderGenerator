package com.helospark.spark.converter.handlers.domain;

import java.util.List;

import javax.annotation.Generated;

public class ConverterTypeCodeGenerationRequest {
    private String className;
    private String packageName;
    private List<ConverterTypeCodeGenerationRequest> dependencies;
    private List<ConverterMethodCodeGenerationRequest> methods;

    @Generated("SparkTools")
    private ConverterTypeCodeGenerationRequest(Builder builder) {
        this.className = builder.className;
        this.packageName = builder.packageName;
        this.dependencies = builder.dependencies;
        this.methods = builder.methods;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<ConverterTypeCodeGenerationRequest> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<ConverterTypeCodeGenerationRequest> dependencies) {
        this.dependencies = dependencies;
    }

    public List<ConverterMethodCodeGenerationRequest> getMethods() {
        return methods;
    }

    public void setMethods(List<ConverterMethodCodeGenerationRequest> methods) {
        this.methods = methods;
    }

    public void addMethod(ConverterMethodCodeGenerationRequest method) {
        methods.add(method);
    }

    public void addDependency(ConverterTypeCodeGenerationRequest dependency) {
        this.dependencies.add(dependency);
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private String className;
        private String packageName;
        private List<ConverterTypeCodeGenerationRequest> dependencies;
        private List<ConverterMethodCodeGenerationRequest> methods;

        private Builder() {
        }

        public Builder withClassName(String className) {
            this.className = className;
            return this;
        }

        public Builder withPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder withDependencies(List<ConverterTypeCodeGenerationRequest> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Builder withMethods(List<ConverterMethodCodeGenerationRequest> methods) {
            this.methods = methods;
            return this;
        }

        public ConverterTypeCodeGenerationRequest build() {
            return new ConverterTypeCodeGenerationRequest(this);
        }
    }
}
