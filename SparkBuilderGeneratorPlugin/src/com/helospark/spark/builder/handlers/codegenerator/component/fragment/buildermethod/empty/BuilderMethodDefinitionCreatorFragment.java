package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StaticBuilderMethodSignatureGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates method definition of the builder method. Created definition is something like
 * <pre>
 * /**
 * * Javadoc comment.
 * * /
 * \@Generated("SparkTools")
 * public static Builder builder();
 * </pre>
 * @author helospark
 */
public class BuilderMethodDefinitionCreatorFragment {
    private TemplateResolver templateResolver;
    private PreferencesManager preferenceManager;
    private JavadocAdder javadocAdder;
    private StaticBuilderMethodSignatureGeneratorFragment staticBuilderMethodSignatureGeneratorFragment;

    public BuilderMethodDefinitionCreatorFragment(TemplateResolver templateResolver, PreferencesManager preferenceManager, JavadocAdder javadocAdder,
            StaticBuilderMethodSignatureGeneratorFragment staticBuilderMethodSignatureGeneratorFragment) {
        this.templateResolver = templateResolver;
        this.preferenceManager = preferenceManager;
        this.javadocAdder = javadocAdder;
        this.staticBuilderMethodSignatureGeneratorFragment = staticBuilderMethodSignatureGeneratorFragment;
    }

    public MethodDeclaration createBuilderMethod(AST ast, TypeDeclaration originalType, String builderName) {
        MethodDeclaration builderMethod = staticBuilderMethodSignatureGeneratorFragment.create(ast, getBuilderMethodName(originalType), builderName);
        javadocAdder.addJavadocForBuilderMethod(ast, originalType.getName().toString(), builderMethod);
        return builderMethod;
    }

    private String getBuilderMethodName(TypeDeclaration originalType) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("className", originalType.getName().toString());
        return templateResolver.resolveTemplate(preferenceManager.getPreferenceValue(PluginPreferenceList.CREATE_BUILDER_METHOD_PATTERN), replacements);
    }

}
