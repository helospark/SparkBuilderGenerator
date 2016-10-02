package com.helospark.spark.converter.preferences;

import java.util.LinkedHashSet;

import com.helospark.spark.converter.preferences.impl.BooleanPluginPreference;
import com.helospark.spark.converter.preferences.impl.StringPluginPreference;

/**
 * Holds all the {@link PluginPreference}s for the Jenerate plugin
 * 
 * @author maudrain
 */
public final class PluginPreferenceList {

    private PluginPreferenceList() {
        /* Only static constants */
    }

    public static final PluginPreference<String> CONVERTER_CLASS_NAME = new StringPluginPreference("converter_class_name",
            "Converter class name", "[destinationClass]Converter");

    public static final PluginPreference<String> CONVERTER_METHOD_NAME = new StringPluginPreference("converter_method_name",
            "Converter class name", "convert");

    public static final PluginPreference<Boolean> GENERATE_NONNULL_ANNOTATION = new BooleanPluginPreference("generate_nonnull_annotation",
            "Generate nonnull annotation", Boolean.TRUE);

    public static final PluginPreference<Boolean> PREFER_BUILD_OVER_SETTER = new BooleanPluginPreference("prefer_builder_over_setter",
            "Prefer builder over setter", Boolean.TRUE);

    public static final PluginPreference<String> BUILDER_METHOD_NAME_PATTERN = new StringPluginPreference("builder_method_name_pattern",
            "Builder name pattern", "builder");

    public static final PluginPreference<Boolean> GENERATE_CONVERTER_RECURSIVELY = new BooleanPluginPreference("recursively_create_converters",
            "Generate recursive converters", Boolean.TRUE);

    public static final PluginPreference<Boolean> CREATE_UNIT_TESTS = new BooleanPluginPreference("create_unit_tests",
            "Generate unit tests", Boolean.TRUE);

    public static final PluginPreference<String> UNIT_TEST_ROOT_FOLDER = new StringPluginPreference("unit_test_root_folder",
            "Root folder for tests", "test");

    public static final PluginPreference<String> TEST_CLASS_NAME_PATTERN = new StringPluginPreference("unit_test_class_pattern",
            "Unit test class name pattern", "[className]Test");

    public static final PluginPreference<String> TEST_METHOD_NAME_PATTERN = new StringPluginPreference("unit_test_method_name_pattern",
            "Unit test method name pattern", "test[methodName]Should[condition]");

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_TEST = new BooleanPluginPreference("generate_javadoc_on_test",
            "Generate Javadoc on test class", Boolean.TRUE);

    /**
     * @return all preferences of the Jenerate plugin. The ordering of the
     *         preferences is important because for example the preference page
     *         uses it to generate the preferences fields.
     */
    public static LinkedHashSet<PluginPreference<?>> getAllPreferences() {
        LinkedHashSet<PluginPreference<?>> allPreferences = new LinkedHashSet<PluginPreference<?>>();
        allPreferences.add(CONVERTER_CLASS_NAME);
        allPreferences.add(CONVERTER_METHOD_NAME);
        allPreferences.add(GENERATE_NONNULL_ANNOTATION);
        allPreferences.add(GENERATE_CONVERTER_RECURSIVELY);
        allPreferences.add(GENERATE_JAVADOC_ON_TEST);
        allPreferences.add(PREFER_BUILD_OVER_SETTER);
        allPreferences.add(BUILDER_METHOD_NAME_PATTERN);
        allPreferences.add(CREATE_UNIT_TESTS);
        allPreferences.add(UNIT_TEST_ROOT_FOLDER);
        allPreferences.add(TEST_CLASS_NAME_PATTERN);
        allPreferences.add(TEST_METHOD_NAME_PATTERN);
        return allPreferences;
    }
}
