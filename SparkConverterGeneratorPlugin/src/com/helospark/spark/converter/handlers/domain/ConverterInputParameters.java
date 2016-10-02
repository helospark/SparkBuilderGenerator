package com.helospark.spark.converter.handlers.domain;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;

public class ConverterInputParameters {
    private ICompilationUnit sourceCompilationUnit;
    private ICompilationUnit destinationCompilationUnit;
    private String destinationPackageFragment;
    private Boolean recursiveGeneration;
    private IJavaProject javaProject;

    private ConverterInputParameters(Builder builder) {
        this.sourceCompilationUnit = builder.sourceCompilationUnit;
        this.destinationCompilationUnit = builder.destinationCompilationUnit;
        this.destinationPackageFragment = builder.destinationPackageFragment;
        this.recursiveGeneration = builder.recursiveGeneration;
        this.javaProject = builder.javaProject;
    }

    public ICompilationUnit getSourceCompilationUnit() {
        return sourceCompilationUnit;
    }

    public ICompilationUnit getDestinationCompilationUnit() {
        return destinationCompilationUnit;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ICompilationUnit sourceCompilationUnit;
        private ICompilationUnit destinationCompilationUnit;
        private String destinationPackageFragment;
        private Boolean recursiveGeneration;
        private IJavaProject javaProject;

        private Builder() {
        }

        public Builder withSourceCompilationUnit(ICompilationUnit sourceCompilationUnit) {
            this.sourceCompilationUnit = sourceCompilationUnit;
            return this;
        }

        public Builder withDestinationCompilationUnit(ICompilationUnit destinationCompilationUnit) {
            this.destinationCompilationUnit = destinationCompilationUnit;
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
