package com.helospark.spark.builder.preferences;

import java.util.LinkedHashSet;

import com.helospark.spark.builder.preferences.impl.BooleanPluginPreference;
import com.helospark.spark.builder.preferences.impl.StringPluginPreference;

/**
 * Holds all the {@link PluginPreference}s for the Jenerate plugin
 * 
 * @author maudrain
 */
public final class PluginPreferenceList {

    private PluginPreferenceList() {
        /* Only static constants */
    }

    public static final PluginPreference<String> CREATE_BUILDER_METHOD_PATTERN = new StringPluginPreference("create_builder_method_pattern",
            "Create builder method pattern", "builder");

    public static final PluginPreference<String> BUILDER_CLASS_NAME_PATTERN = new StringPluginPreference("builder_class_name_pattern",
            "Builder class name pattern", "Builder");

    public static final PluginPreference<String> BUILD_METHOD_NAME_PATTERN = new StringPluginPreference("build_method_name",
            "Build method name pattern", "build");

    public static final PluginPreference<String> BUILDERS_METHOD_NAME_PATTERN = new StringPluginPreference("builders_method_name_pattern",
            "Builder's methods name pattern", "with[FieldName]");

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_BUILDER_METHOD = new BooleanPluginPreference("generate_javadoc_on_builder_method",
            "Generate Javadoc on builder method", Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_BUILDER_CLASS = new BooleanPluginPreference("generate_javadoc_on_builder_class",
            "Generate Javadoc on builder class", Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD = new BooleanPluginPreference("generate_javadoc_on_each_builder_method",
            "Generate Javadoc on each builder method", Boolean.FALSE);

    public static final PluginPreference<Boolean> ADD_NONNULL_ON_RETURN = new BooleanPluginPreference("add_nonnull_on_return",
            "Generate @NonNull on return values", Boolean.TRUE);

    public static final PluginPreference<Boolean> ADD_NONNULL_ON_PARAMETERS = new BooleanPluginPreference("add_nonnull_on_parameter",
            "Generate @NonNull on parameters", Boolean.TRUE);

    public static final PluginPreference<Boolean> OVERRIDE_PREVIOUS_BUILDER = new BooleanPluginPreference("override_previous_builder",
            "Override previous builder", Boolean.TRUE);

    /**
     * @return all preferences of the Jenerate plugin. The ordering of the
     *         preferences is important because for example the preference page
     *         uses it to generate the preferences fields.
     */
    public static LinkedHashSet<PluginPreference<?>> getAllPreferences() {
        LinkedHashSet<PluginPreference<?>> allPreferences = new LinkedHashSet<PluginPreference<?>>();
        allPreferences.add(CREATE_BUILDER_METHOD_PATTERN);
        allPreferences.add(BUILDER_CLASS_NAME_PATTERN);
        allPreferences.add(BUILD_METHOD_NAME_PATTERN);
        allPreferences.add(BUILDERS_METHOD_NAME_PATTERN);
        allPreferences.add(GENERATE_JAVADOC_ON_BUILDER_METHOD);
        allPreferences.add(GENERATE_JAVADOC_ON_BUILDER_CLASS);
        allPreferences.add(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD);
        allPreferences.add(ADD_NONNULL_ON_RETURN);
        allPreferences.add(ADD_NONNULL_ON_PARAMETERS);
        allPreferences.add(OVERRIDE_PREVIOUS_BUILDER);
        return allPreferences;
    }
}
