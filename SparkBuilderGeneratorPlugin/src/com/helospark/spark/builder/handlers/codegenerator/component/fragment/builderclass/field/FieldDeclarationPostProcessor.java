package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY;
import static com.helospark.spark.builder.preferences.StaticPreferences.EMPTY_OPTIONAL_CREATOR_STATIC_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.OPTIONAL_CLASS_NAME;
import static com.helospark.spark.builder.preferences.StaticPreferences.OPTIONAL_FULLY_QUALIFIED_NAME;

import java.util.Optional;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Post process field declaration in a builder.
 * Adds field initialization.
 * @author helospark
 */
public class FieldDeclarationPostProcessor {
    private PreferencesManager preferencesManager;
    private FullyQualifiedNameExtractor fullyQualifiedNameExtractor;
    private StaticMethodInvocationFragment staticMethodInvocationFragment;
    private ImportRepository importRepository;

    public FieldDeclarationPostProcessor(PreferencesManager preferencesManager, FullyQualifiedNameExtractor fullyQualifiedNameExtractor,
            StaticMethodInvocationFragment staticMethodInvocationFragment, ImportRepository importRepository) {
        this.preferencesManager = preferencesManager;
        this.fullyQualifiedNameExtractor = fullyQualifiedNameExtractor;
        this.staticMethodInvocationFragment = staticMethodInvocationFragment;
        this.importRepository = importRepository;
    }

    public VariableDeclarationFragment postProcessFragment(AST ast, FieldDeclaration originalFieldDeclaration, VariableDeclarationFragment variableDeclarationFragment) {
        if (isPostProcessingRequired()) {
            Optional<String> result = fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(originalFieldDeclaration);
            if (result.isPresent()) {
                postProcessDeclaration(ast, variableDeclarationFragment, result.get());
            }
        }
        return variableDeclarationFragment;
    }

    private Boolean isPostProcessingRequired() {
        return preferencesManager.getPreferenceValue(INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY);
    }

    private void postProcessDeclaration(AST ast, VariableDeclarationFragment variableDeclarationFragment, String fullyQualifiedName) {
        if (fullyQualifiedName.equals(OPTIONAL_FULLY_QUALIFIED_NAME)) {
            MethodInvocation optionalEmptyCall = staticMethodInvocationFragment.createStaticMethodInvocation(ast, OPTIONAL_CLASS_NAME, EMPTY_OPTIONAL_CREATOR_STATIC_METHOD);
            variableDeclarationFragment.setInitializer(optionalEmptyCall);
            importRepository.addImport(OPTIONAL_FULLY_QUALIFIED_NAME);
        }
    }
}