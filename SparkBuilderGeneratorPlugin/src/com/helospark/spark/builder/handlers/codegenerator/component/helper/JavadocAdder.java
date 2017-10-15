package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_GENERATE_JAVADOC_ON_STAGE_INTERFACE;
import static com.helospark.spark.builder.preferences.StaticPreferences.PARAM_JAVADOC_TAG_NAME;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;
import com.helospark.spark.builder.preferences.StaticPreferences;

/**
 * Decides whether javadoc is needed, and if needed adds them.
 * @author helospark
 */
public class JavadocAdder {
    private JavadocGenerator javadocGenerator;
    private PreferencesManager preferencesManager;

    public JavadocAdder(JavadocGenerator javadocGenerator, PreferencesManager preferencesManager) {
        this.javadocGenerator = javadocGenerator;
        this.preferencesManager = preferencesManager;
    }

    public void addJavadocForWithMethod(AST ast, String fieldName, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
            Map<String, String> javadocTags = new HashMap<>();
            javadocTags.put(RETURN_JAVADOC_TAG_NAME, "builder");
            javadocTags.put(PARAM_JAVADOC_TAG_NAME, String.format(Locale.ENGLISH, "%s to set", fieldName));
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast,
                    String.format(Locale.ENGLISH, "Builder method for %s parameter.", fieldName),
                    javadocTags);
            builderMethod.setJavadoc(javadoc);
        }
    }

    public void addJavadocForBuildMethod(AST ast, MethodDeclaration buildMethod) {
        if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast, "Builder method of the builder.",
                    Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "built class"));
            buildMethod.setJavadoc(javadoc);
        }
    }

    public void addJavadocForStagedInterface(AST ast, String interfaceName, TypeDeclaration addedInterface) {
        if (preferencesManager.getPreferenceValue(STAGED_BUILDER_GENERATE_JAVADOC_ON_STAGE_INTERFACE)) {
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast,
                    String.format(Locale.ENGLISH, "Definition of a stage for staged builder."), Collections.emptyMap());
            addedInterface.setJavadoc(javadoc);
        }
    }

    public void addJavadocForBuilderMethod(AST ast, String typeName, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_METHOD)) {
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast, String.format(Locale.ENGLISH, "Creates builder to build {@link %s}.", typeName),
                    Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "created builder"));
            builderMethod.setJavadoc(javadoc);
        }
    }

    public void addJavadocForWithBuilderMethod(AST ast, String typeName, String parameterName, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_METHOD)) {
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast,
                    String.format(Locale.ENGLISH, "Creates builder to build {@link %s} and setting %s parameter.", typeName, parameterName),
                    Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "created builder"));
            builderMethod.setJavadoc(javadoc);
        }
    }
}
