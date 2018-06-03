package com.helospark.spark.builder.handlers.codegenerator.domain;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

/**
 * One-time user preference that has to be entered by the user on every builder generation.
 * @author helospark
 */
public class RegularBuilderUserPreference {
    private boolean generateCopyMethod;
    private boolean addJacksonDeserializer;
    private boolean createDefaultConstructor;
    private List<BuilderField> builderFields;

    @Generated("SparkTools")
    private RegularBuilderUserPreference(CustomBuilder customBuilder) {
        this.generateCopyMethod = customBuilder.generateCopyMethod;
        this.addJacksonDeserializer = customBuilder.addJacksonDeserializer;
        this.createDefaultConstructor = customBuilder.createDefaultConstructor;
        this.builderFields = customBuilder.builderFields;
    }

    public boolean isGenerateCopyMethod() {
        return generateCopyMethod;
    }

    public List<BuilderField> getBuilderFields() {
        return builderFields;
    }

    public boolean isAddJacksonDeserializer() {
        return addJacksonDeserializer;
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
        private boolean generateCopyMethod;
        private boolean addJacksonDeserializer;
        private boolean createDefaultConstructor;
        private List<BuilderField> builderFields = Collections.emptyList();

        private CustomBuilder() {
        }

        public CustomBuilder withGenerateCopyMethod(boolean generateCopyMethod) {
            this.generateCopyMethod = generateCopyMethod;
            return this;
        }

        public CustomBuilder withAddJacksonDeserializer(boolean addJacksonDeserializer) {
            this.addJacksonDeserializer = addJacksonDeserializer;
            return this;
        }

        public CustomBuilder withCreateDefaultConstructor(boolean createDefaultConstructor) {
            this.createDefaultConstructor = createDefaultConstructor;
            return this;
        }

        public CustomBuilder withBuilderFields(List<BuilderField> builderFields) {
            this.builderFields = builderFields;
            return this;
        }

        public RegularBuilderUserPreference build() {
            return new RegularBuilderUserPreference(this);
        }
    }

}
