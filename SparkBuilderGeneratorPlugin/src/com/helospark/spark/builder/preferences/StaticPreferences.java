package com.helospark.spark.builder.preferences;

public class StaticPreferences {
    public static final String RETURN_JAVADOC_TAG_NAME = "@return";
    public static final String PARAM_JAVADOC_TAG_NAME = "@param";
    public static final String PLUGIN_GENERATED_ANNOTATION_NAME = "SparkTools";
    public static final String OPTIONAL_CLASS_NAME = "Optional";
    public static final String OPTIONAL_FULLY_QUALIFIED_NAME = "java.util." + OPTIONAL_CLASS_NAME;
    public static final String EMPTY_OPTIONAL_CREATOR_STATIC_METHOD = "empty";
    public static final String COLLECTIONS_CLASS_NAME = "Collections";
    public static final String COLLECTIONS_CLASS_FULLY_QUALIFIED_NAME = "java.util." + COLLECTIONS_CLASS_NAME;

    public static final String JSON_POJO_BUILDER_CLASS_NAME = "JsonPOJOBuilder";
    public static final String JSON_POJO_BUILDER_FULLY_QUALIFIED_NAME = "com.fasterxml.jackson.databind.annotation" + JSON_POJO_BUILDER_CLASS_NAME;

    public static final String JSON_DESERIALIZE_CLASS_NAME = "JsonDeserialize";
    public static final String JSON_DESERIALIZE_FULLY_QUALIFIED_NAME = "com.fasterxml.jackson.databind.annotation" + JSON_DESERIALIZE_CLASS_NAME;
}
