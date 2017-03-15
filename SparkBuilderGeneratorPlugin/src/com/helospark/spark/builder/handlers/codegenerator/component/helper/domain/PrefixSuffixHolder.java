package com.helospark.spark.builder.handlers.codegenerator.component.helper.domain;

import java.util.List;

import javax.annotation.Generated;

public class PrefixSuffixHolder {
    private List<String> prefixes;
    private List<String> suffixes;

    @Generated("SparkTools")
    private PrefixSuffixHolder(Builder builder) {
        this.prefixes = builder.prefixes;
        this.suffixes = builder.suffixes;
    }

    public List<String> getPrefixes() {
        return prefixes;
    }

    public List<String> getSuffixes() {
        return suffixes;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private List<String> prefixes;
        private List<String> suffixes;

        private Builder() {
        }

        public Builder withPrefixes(List<String> prefixes) {
            this.prefixes = prefixes;
            return this;
        }

        public Builder withSuffixes(List<String> suffixes) {
            this.suffixes = suffixes;
            return this;
        }

        public PrefixSuffixHolder build() {
            return new PrefixSuffixHolder(this);
        }
    }

}
