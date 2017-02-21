package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain;

import javax.annotation.Generated;

public class CompletitionProposalResponse {
    private String autocompleString;
    private String displayName;
    private String description;
    private Class<?> type;

    @Generated("SparkTools")
    private CompletitionProposalResponse(Builder builder) {
        this.autocompleString = builder.autocompleString;
        this.displayName = builder.displayName;
        this.description = builder.description;
        this.type = builder.type;
    }

    public String getAutocompleString() {
        return autocompleString;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getType() {
        return type;
    }

    /**
     * Creates builder to build {@link CompletitionProposalResponse}.
     * 
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link CompletitionProposalResponse}.
     */
    @Generated("SparkTools")
    public static final class Builder {
        private String autocompleString;
        private String displayName;
        private String description;
        private Class<?> type;

        private Builder() {
        }

        /**
         * Builder method for autocompleString parameter.
         * 
         * @return builder
         */

        public Builder withAutocompleString(String autocompleString) {
            this.autocompleString = autocompleString;
            return this;
        }

        /**
         * Builder method for displayName parameter.
         * 
         * @return builder
         */

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Builder method for description parameter.
         * 
         * @return builder
         */

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builder method for type parameter.
         * 
         * @return builder
         */

        public Builder withType(Class<?> type) {
            this.type = type;
            return this;
        }

        /**
         * Builder method of the builder.
         * 
         * @return built class
         */
        public CompletitionProposalResponse build() {
            return new CompletitionProposalResponse(this);
        }
    }

}
