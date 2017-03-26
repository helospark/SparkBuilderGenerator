package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates method definition of the builder method. Created definition is something like
 * <pre>
 * public static Builder builder();
 * </pre>
 * @author helospark
 */
public class BuilderMethodDefinitionCreatorFragment {
    private TemplateResolver templateResolver;
    private PreferencesManager preferenceManager;
    private JavadocAdder javadocAdder;
    private GeneratedAnnotationPopulator generatedAnnotationPopulator;
    private PreferencesManager preferencesManager;

    public BuilderMethodDefinitionCreatorFragment(TemplateResolver templateResolver, PreferencesManager preferenceManager, JavadocAdder javadocAdder,
            GeneratedAnnotationPopulator generatedAnnotationPopulator, PreferencesManager preferencesManager) {
        this.templateResolver = templateResolver;
        this.preferenceManager = preferenceManager;
        this.javadocAdder = javadocAdder;
        this.generatedAnnotationPopulator = generatedAnnotationPopulator;
        this.preferencesManager = preferencesManager;
    }

    @SuppressWarnings("unchecked")
    public MethodDeclaration createBuilderMethod(AST ast, TypeDeclaration originalType, String builderName) {
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(getBuilderMethodName(originalType)));
        addGenerateAnnotationIfNeeded(ast, builderMethod);
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
        builderMethod.setReturnType2(ast.newSimpleType(ast.newName(builderName)));

        javadocAdder.addJavadocForBuilderMethod(ast, originalType.getName().toString(), builderMethod);

        return builderMethod;
    }

    private void addGenerateAnnotationIfNeeded(AST ast, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION)) {
            generatedAnnotationPopulator.addGeneratedAnnotation(ast, builderMethod);
        }
    }

    private String getBuilderMethodName(TypeDeclaration originalType) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("className", originalType.getName().toString());
        return templateResolver.resolveTemplate(preferenceManager.getPreferenceValue(PluginPreferenceList.CREATE_BUILDER_METHOD_PATTERN), replacements);
    }

}
