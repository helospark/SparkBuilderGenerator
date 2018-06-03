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
    private boolean shouldCreateCopyMethod;
    private boolean addJacksonDeserializeAnnotation;
    private boolean createDefaultConstructor;

    @Generated("SparkTools")
    private RegularBuilderDialogData(CustomBuilder customBuilder) {
        this.regularBuilderFieldIncludeFieldIncludeDomains = customBuilder.regularBuilderFieldIncludeFieldIncludeDomains;
        this.shouldCreateCopyMethod = customBuilder.shouldCreateCopyMethod;
        this.addJacksonDeserializeAnnotation = customBuilder.addJacksonDeserializeAnnotation;
        this.createDefaultConstructor = customBuilder.createDefaultConstructor;
    }

    public List<RegularBuilderFieldIncludeFieldIncludeDomain> getRegularBuilderFieldIncludeFieldIncludeDomains() {
        return regularBuilderFieldIncludeFieldIncludeDomains;
    }

    public boolean isShouldCreateCopyMethod() {
        return shouldCreateCopyMethod;
    }

    public boolean isAddJacksonDeserializeAnnotation() {
        return addJacksonDeserializeAnnotation;
    }

    public boolean isCreateDefaultConstructor() {
        return createDefaultConstructor;
    }

    @Generated("SparkTools")
    public static CustomBuilder builder() {
        return new CustomBuilder();
    }

    @Generated("SparkTools")
    public static final class CustomBuilder {
        private List<RegularBuilderFieldIncludeFieldIncludeDomain> regularBuilderFieldIncludeFieldIncludeDomains = Collections.emptyList();
        private boolean shouldCreateCopyMethod;
        private boolean addJacksonDeserializeAnnotation;
        private boolean createDefaultConstructor;

        private CustomBuilder() {
        }

        public CustomBuilder withRegularBuilderFieldIncludeFieldIncludeDomains(List<RegularBuilderFieldIncludeFieldIncludeDomain> regularBuilderFieldIncludeFieldIncludeDomains) {
            this.regularBuilderFieldIncludeFieldIncludeDomains = regularBuilderFieldIncludeFieldIncludeDomains;
            return this;
        }

        public CustomBuilder withShouldCreateCopyMethod(boolean shouldCreateCopyMethod) {
            this.shouldCreateCopyMethod = shouldCreateCopyMethod;
            return this;
        }

        public CustomBuilder withAddJacksonDeserializeAnnotation(boolean addJacksonDeserializeAnnotation) {
            this.addJacksonDeserializeAnnotation = addJacksonDeserializeAnnotation;
            return this;
        }

        public CustomBuilder withCreateDefaultConstructor(boolean createDefaultConstructor) {
            this.createDefaultConstructor = createDefaultConstructor;
            return this;
        }

        public RegularBuilderDialogData build() {
            return new RegularBuilderDialogData(this);
        }
    }

}
