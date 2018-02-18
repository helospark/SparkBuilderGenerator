package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates the signature of the builder method.
 * <pre>
 * public static void builder(_not_yet_filled_);
 * </pre>
 * @author helospark
 */
public class StaticBuilderMethodSignatureGeneratorFragment {
    private GeneratedAnnotationPopulator generatedAnnotationPopulator;
    private PreferencesManager preferencesManager;

    public StaticBuilderMethodSignatureGeneratorFragment(GeneratedAnnotationPopulator generatedAnnotationPopulator, PreferencesManager preferencesManager) {
        this.generatedAnnotationPopulator = generatedAnnotationPopulator;
        this.preferencesManager = preferencesManager;
    }

    public MethodDeclaration create(AST ast, String builderMethodName, String builderName) {
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(builderMethodName));
        addGenerateAnnotationIfNeeded(ast, builderMethod);
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
        builderMethod.setReturnType2(ast.newSimpleType(ast.newName(builderName)));
        return builderMethod;
    }

    private void addGenerateAnnotationIfNeeded(AST ast, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION)) {
            generatedAnnotationPopulator.addGeneratedAnnotation(ast, builderMethod);
        }
    }

}
