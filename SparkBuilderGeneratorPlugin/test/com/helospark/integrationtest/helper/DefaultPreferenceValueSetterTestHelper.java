package com.helospark.integrationtest.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDERS_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDER_CLASS_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_BUILDER_METHOD_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_CLASS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.OVERRIDE_PREVIOUS_BUILDER;
import static org.mockito.BDDMockito.given;

import com.helospark.spark.builder.preferences.PreferencesManager;

public class DefaultPreferenceValueSetterTestHelper {

    public static void setDefaultTestValue(PreferencesManager preferencesManager) {
        given(preferencesManager.getPreferenceValue(CREATE_BUILDER_METHOD_PATTERN)).willReturn(CREATE_BUILDER_METHOD_PATTERN.getDefaultValue());
        given(preferencesManager.getPreferenceValue(BUILDER_CLASS_NAME_PATTERN)).willReturn(BUILDER_CLASS_NAME_PATTERN.getDefaultValue());
        given(preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN)).willReturn(BUILD_METHOD_NAME_PATTERN.getDefaultValue());
        given(preferencesManager.getPreferenceValue(BUILDERS_METHOD_NAME_PATTERN)).willReturn(BUILDERS_METHOD_NAME_PATTERN.getDefaultValue());
        given(preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_BUILDER_METHOD)).willReturn(GENERATE_JAVADOC_ON_BUILDER_METHOD.getDefaultValue());
        given(preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_BUILDER_CLASS)).willReturn(GENERATE_JAVADOC_ON_BUILDER_CLASS.getDefaultValue());
        given(preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)).willReturn(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD.getDefaultValue());
        given(preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)).willReturn(ADD_NONNULL_ON_RETURN.getDefaultValue());
        given(preferencesManager.getPreferenceValue(ADD_NONNULL_ON_PARAMETERS)).willReturn(ADD_NONNULL_ON_PARAMETERS.getDefaultValue());
        given(preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION)).willReturn(ADD_GENERATED_ANNOTATION.getDefaultValue());
        given(preferencesManager.getPreferenceValue(OVERRIDE_PREVIOUS_BUILDER)).willReturn(OVERRIDE_PREVIOUS_BUILDER.getDefaultValue());
    }
}
