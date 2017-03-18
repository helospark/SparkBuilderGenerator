package com.helospark.spark.builder.preferences;

import java.util.LinkedHashSet;

import com.helospark.spark.builder.preferences.impl.BooleanPluginPreference;
import com.helospark.spark.builder.preferences.impl.StringPluginPreference;

/**
 * Holds all the {@link PluginPreference}s for this plugin.
 *
 * @author maudrain, helospark
 */
public final class PluginPreferenceList {

    private PluginPreferenceList() {
        /* Only static constants */
    }

    public static final PluginPreference<String> CREATE_BUILDER_METHOD_PATTERN = new StringPluginPreference("create_builder_method_pattern",
            "Create builder method templated pattern", "builder");

    public static final PluginPreference<String> BUILDER_CLASS_NAME_PATTERN = new StringPluginPreference("builder_class_name_pattern",
            "Builder class name templated pattern", "Builder");

    public static final PluginPreference<String> BUILD_METHOD_NAME_PATTERN = new StringPluginPreference("build_method_name",
            "Build method name templated pattern", "build");

    public static final PluginPreference<String> BUILDERS_METHOD_NAME_PATTERN = new StringPluginPreference("builders_method_name_pattern",
            "Builder's methods name templated pattern", "with[FieldName]");

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_BUILDER_METHOD = new BooleanPluginPreference("generate_javadoc_on_builder_method",
            "Generate Javadoc on builder method", Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_BUILDER_CLASS = new BooleanPluginPreference("generate_javadoc_on_builder_class",
            "Generate Javadoc on builder class", Boolean.TRUE);

    public static final PluginPreference<Boolean> GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD = new BooleanPluginPreference("generate_javadoc_on_each_builder_method",
            "Generate Javadoc on each builder method", Boolean.FALSE);

    public static final PluginPreference<Boolean> ADD_NONNULL_ON_RETURN = new BooleanPluginPreference("add_nonnull_on_return",
            "Generate @Nonnull on return values", Boolean.FALSE);

    public static final PluginPreference<Boolean> ADD_NONNULL_ON_PARAMETERS = new BooleanPluginPreference("add_nonnull_on_parameter",
            "Generate @Nonnull on parameters", Boolean.FALSE);

    public static final PluginPreference<Boolean> ADD_GENERATED_ANNOTATION = new BooleanPluginPreference("add_generated_annotation",
            "Add @Generated annotation", Boolean.TRUE);

    public static final PluginPreference<Boolean> INITIALIZE_OPTIONAL = new BooleanPluginPreference("initialize_optional",
            "Initialize Optional in method", Boolean.FALSE);

    public static final PluginPreference<Boolean> OVERRIDE_PREVIOUS_BUILDER = new BooleanPluginPreference("override_previous_builder",
            "Override previous builder", Boolean.TRUE);

    public static final PluginPreference<Boolean> REMOVE_PREFIX_AND_SUFFIX_FROM_BUILDER_NAMES = new BooleanPluginPreference(
            "org.helospark.builder.removePrefixAndPostfixFromBuilderNames",
            "Remove prefix and suffix (set in Preferences->Java->Code style)\nfrom builder names", Boolean.TRUE);

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
        allPreferences.add(ADD_GENERATED_ANNOTATION);
        allPreferences.add(OVERRIDE_PREVIOUS_BUILDER);
        allPreferences.add(REMOVE_PREFIX_AND_SUFFIX_FROM_BUILDER_NAMES);
        return allPreferences;
    }
}
