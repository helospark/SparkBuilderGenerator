package com.helospark.spark.builder.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.preferences.impl.BooleanPluginPreference;
import com.helospark.spark.builder.preferences.impl.NamedElementListPluginPreference;
import com.helospark.spark.builder.preferences.impl.StringPluginPreference;

/**
 * Holds all the {@link PluginPreference}s for this plugin.
 *
 * @author maudrain, helospark
 */
public final class PluginPreferenceList {

    public static final PluginPreference<BuilderType> DEFAULT_BUILDER_GENERATOR = new NamedElementListPluginPreference<>("org.helospark.defaultBuilderGenerator",
            "Default builder generator implementation", Arrays.asList(BuilderType.values()), BuilderType.REGULAR);

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

    public static final PluginPreference<Boolean> OVERRIDE_PREVIOUS_BUILDER = new BooleanPluginPreference("override_previous_builder",
            "Override previous builder", Boolean.TRUE);

    public static final PluginPreference<Boolean> REMOVE_PREFIX_AND_SUFFIX_FROM_BUILDER_NAMES = new BooleanPluginPreference(
            "org.helospark.builder.removePrefixAndPostfixFromBuilderNames",
            "Remove prefix and suffix (set in Preferences->Java->Code style)\nfrom builder names", Boolean.TRUE);

    public static final PluginPreference<Boolean> INCLUDE_VISIBLE_FIELDS_FROM_SUPERCLASS = new BooleanPluginPreference(
            "org.helospark.builder.includeVisibleFieldsFromSuperclass",
            "Include visible fields from superclass", Boolean.TRUE);

    public static final PluginPreference<Boolean> INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY = new BooleanPluginPreference(
            "org.helospark.builder.initializeOptionalFieldsToEmpty",
            "Initialize optional fields to Optional.empty() value", Boolean.TRUE);

    public static final PluginPreference<Boolean> INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS = new BooleanPluginPreference(
            "org.helospark.builder.initializeCollectionToEmptyCollection",
            "Initialize collections to empty collections", Boolean.TRUE);

    public static final PluginPreference<Boolean> ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS = new BooleanPluginPreference(
            "org.helospark.builder.alwaysGenerateBuilderToFirstClass",
            "Always generate builder to first (top level) class", Boolean.FALSE);

    public static final PluginPreference<String> STAGED_BUILDER_STAGE_INTERFACE_NAME = new StringPluginPreference("org.helospark.builder.stagedEditorStageInterfaceName",
            "Stage interface pattern for staged builder", "I[FieldName]Stage");

    public static final PluginPreference<String> STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME = new StringPluginPreference("org.helospark.builder.stagedEditorLastStageInterfaceName",
            "Last stage interface name for staged builder", "IBuildStage");

    public static final PluginPreference<Boolean> STAGED_BUILDER_SKIP_STATIC_BUILDER_METHOD = new BooleanPluginPreference("org.helospark.builder.skipStaticBuilderMethod",
            "Skip static builder() method for staged builder", Boolean.FALSE);

    public static final PluginPreference<Boolean> STAGED_BUILDER_GENERATE_JAVADOC_ON_STAGE_INTERFACE = new BooleanPluginPreference(
            "org.helospark.builder.generateJavadocOnStageInterface",
            "Generate Javadoc on stage interface for staged builder", Boolean.FALSE);

    public static final PluginPreference<Boolean> STAGED_BUILDER_ADD_GENERATED_ANNOTATION_ON_STAGE_INTERFACE = new BooleanPluginPreference(
            "org.helospark.builder.addGeneratedAnnotationOnStageInterface",
            "Add @Generated annotation on generated interfaces", Boolean.TRUE);

    public static final PluginPreference<Boolean> REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG = new BooleanPluginPreference(
            "org.helospark.builder.showFieldFilterDialogForRegularBuilder",
            "Show dialog to filter which fields are included in the builder", Boolean.FALSE);

    public static final PluginPreference<Boolean> INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR = new BooleanPluginPreference(
            "org.helospark.builder.includeParametersFromSuperclassConstructor",
            "Include parameters as fields from superclass constructor", Boolean.TRUE);

    public static final PluginPreference<Boolean> PREFER_TO_USE_EMPTY_SUPERCLASS_CONSTRUCTOR = new BooleanPluginPreference(
            "org.helospark.builder.preferToUseEmptySuperclassConstructor",
            "Prefer to use empty superclass constructor", Boolean.TRUE);

    public static List<PluginPreferenceGroup> getAllPreferences() {
        return Arrays.asList(createGeneralPreferencesGroup(),
                createRegularBuilderPreferencesGroup(),
                createStagedPreferencesGroup());
    }

    private static PluginPreferenceGroup createGeneralPreferencesGroup() {
        List<PluginPreference<?>> generalPreferences = new ArrayList<>();
        generalPreferences.add(DEFAULT_BUILDER_GENERATOR);
        generalPreferences.add(CREATE_BUILDER_METHOD_PATTERN);
        generalPreferences.add(BUILDER_CLASS_NAME_PATTERN);
        generalPreferences.add(BUILD_METHOD_NAME_PATTERN);
        generalPreferences.add(BUILDERS_METHOD_NAME_PATTERN);
        generalPreferences.add(GENERATE_JAVADOC_ON_BUILDER_METHOD);
        generalPreferences.add(GENERATE_JAVADOC_ON_BUILDER_CLASS);
        generalPreferences.add(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD);
        generalPreferences.add(ADD_NONNULL_ON_RETURN);
        generalPreferences.add(ADD_NONNULL_ON_PARAMETERS);
        generalPreferences.add(INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY);
        generalPreferences.add(INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS);
        generalPreferences.add(ADD_GENERATED_ANNOTATION);
        generalPreferences.add(OVERRIDE_PREVIOUS_BUILDER);
        generalPreferences.add(REMOVE_PREFIX_AND_SUFFIX_FROM_BUILDER_NAMES);
        generalPreferences.add(INCLUDE_VISIBLE_FIELDS_FROM_SUPERCLASS);
        generalPreferences.add(INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR);
        generalPreferences.add(PREFER_TO_USE_EMPTY_SUPERCLASS_CONSTRUCTOR);
        generalPreferences.add(ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS);
        return new PluginPreferenceGroup("General settings", generalPreferences);
    }

    private static PluginPreferenceGroup createStagedPreferencesGroup() {
        List<PluginPreference<?>> stagedBuilderPreferences = new ArrayList<>();
        stagedBuilderPreferences.add(STAGED_BUILDER_STAGE_INTERFACE_NAME);
        stagedBuilderPreferences.add(STAGED_BUILDER_LAST_STAGE_INTERFACE_NAME);
        stagedBuilderPreferences.add(STAGED_BUILDER_SKIP_STATIC_BUILDER_METHOD);
        stagedBuilderPreferences.add(STAGED_BUILDER_GENERATE_JAVADOC_ON_STAGE_INTERFACE);
        stagedBuilderPreferences.add(STAGED_BUILDER_ADD_GENERATED_ANNOTATION_ON_STAGE_INTERFACE);
        return new PluginPreferenceGroup("Staged builder settings", stagedBuilderPreferences);
    }

    private static PluginPreferenceGroup createRegularBuilderPreferencesGroup() {
        List<PluginPreference<?>> regularBuilderPreferences = new ArrayList<>();
        regularBuilderPreferences.add(REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG);
        return new PluginPreferenceGroup("Regular builder settings", regularBuilderPreferences);
    }
}
