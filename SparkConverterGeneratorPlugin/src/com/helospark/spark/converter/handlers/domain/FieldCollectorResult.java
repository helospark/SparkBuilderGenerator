package com.helospark.spark.converter.handlers.domain;

import javax.annotation.Generated;

public class FieldCollectorResult {
    private AccessFieldDeclaration source;
    private AccessFieldDeclaration destination;
    private FieldCollectorResultKind fieldCollectorResultKind;

    @Generated("SparkTools")
    private FieldCollectorResult(Builder builder) {
        this.source = builder.source;
        this.destination = builder.destination;
        this.fieldCollectorResultKind = builder.fieldCollectorResultKind;
    }

    /**
     * Creates builder to build {@link FieldCollectorResult}.
     * 
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link FieldCollectorResult}.
     */
    @Generated("SparkTools")
    public static class Builder {
        private AccessFieldDeclaration source;
        private AccessFieldDeclaration destination;
        private FieldCollectorResultKind fieldCollectorResultKind;

        private Builder() {
        }

        /**
         * Builder method for source parameter.
         * 
         * @return Builder
         */
        public Builder withSource(AccessFieldDeclaration source) {
            this.source = source;
            return this;
        }

        /**
         * Builder method for destination parameter.
         * 
         * @return Builder
         */
        public Builder withDestination(AccessFieldDeclaration destination) {
            this.destination = destination;
            return this;
        }

        /**
         * Builder method for fieldCollectorResultKind parameter.
         * 
         * @return Builder
         */
        public Builder withFieldCollectorResultKind(FieldCollectorResultKind fieldCollectorResultKind) {
            this.fieldCollectorResultKind = fieldCollectorResultKind;
            return this;
        }

        /**
         * Builder method of the builder.
         * 
         * @return Built class
         */
        public FieldCollectorResult build() {
            return new FieldCollectorResult(this);
        }
    }

}
