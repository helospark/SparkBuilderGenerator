package com.helospark.spark.builder.dialogs.domain;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

/**
 * Data for the dialog.
 * @author helospark
 */
public class RegularBuilderDialogData {
    private List<RegularBuilderFieldIncludeFieldIncludeDomain> regularBuilderFieldIncludeFieldIncludeDomains;
    private boolean shouldCreateInstanceCopy;
    private boolean addJacksonDeserializeAnnotation;

    public List<RegularBuilderFieldIncludeFieldIncludeDomain> getRegularBuilderFieldIncludeFieldIncludeDomains() {
        return regularBuilderFieldIncludeFieldIncludeDomains;
    }

    public boolean isShouldCreateInstanceCopy() {
        return shouldCreateInstanceCopy;
    }

    public boolean isAddJacksonDeserializeAnnotation() {
        return addJacksonDeserializeAnnotation;
    }

    @Generated("SparkTools")
    private RegularBuilderDialogData(Builder builder) {
        this.regularBuilderFieldIncludeFieldIncludeDomains = builder.regularBuilderFieldIncludeFieldIncludeDomains;
        this.shouldCreateInstanceCopy = builder.shouldCreateCopyMethod;
        this.addJacksonDeserializeAnnotation = builder.addJacksonDeserializeAnnotation;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private List<RegularBuilderFieldIncludeFieldIncludeDomain> regularBuilderFieldIncludeFieldIncludeDomains = Collections
                .emptyList();
        private boolean shouldCreateCopyMethod;
        private boolean addJacksonDeserializeAnnotation;

        private Builder() {
        }

        public Builder withRegularBuilderFieldIncludeFieldIncludeDomains(List<RegularBuilderFieldIncludeFieldIncludeDomain> regularBuilderFieldIncludeFieldIncludeDomains) {
            this.regularBuilderFieldIncludeFieldIncludeDomains = regularBuilderFieldIncludeFieldIncludeDomains;
            return this;
        }

        public Builder withShouldCreateCopyMethod(boolean shouldCreateCopyMethod) {
            this.shouldCreateCopyMethod = shouldCreateCopyMethod;
            return this;
        }

        public Builder withAddJacksonDeserializeAnnotation(boolean addJacksonDeserializeAnnotation) {
            this.addJacksonDeserializeAnnotation = addJacksonDeserializeAnnotation;
            return this;
        }

        public RegularBuilderDialogData build() {
            return new RegularBuilderDialogData(this);
        }
    }

}
