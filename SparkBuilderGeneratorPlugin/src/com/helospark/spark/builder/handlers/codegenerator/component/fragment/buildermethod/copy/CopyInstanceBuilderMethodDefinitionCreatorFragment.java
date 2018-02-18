package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.COPY_INSTANCE_BUILDER_METHOD_PATTERN;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StaticBuilderMethodSignatureGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates method definition of the static copy instance builder method. Created definition is something like
 * <pre>
 * /**
 * * Javadoc comment.
 * * /
 * \@Generated("SparkTools")
 * public static Builder builder(OriginalType originalType);
 * </pre>
 * @author helospark
 */
public class CopyInstanceBuilderMethodDefinitionCreatorFragment {
    private TemplateResolver templateResolver;
    private PreferencesManager preferenceManager;
    private JavadocAdder javadocAdder;
    private StaticBuilderMethodSignatureGeneratorFragment staticBuilderMethodSignatureGeneratorFragment;

    public CopyInstanceBuilderMethodDefinitionCreatorFragment(TemplateResolver templateResolver, PreferencesManager preferenceManager, JavadocAdder javadocAdder,
            StaticBuilderMethodSignatureGeneratorFragment staticBuilderMethodSignatureGeneratorFragment) {
        this.templateResolver = templateResolver;
        this.preferenceManager = preferenceManager;
        this.javadocAdder = javadocAdder;
        this.staticBuilderMethodSignatureGeneratorFragment = staticBuilderMethodSignatureGeneratorFragment;
    }

    public MethodDeclaration createBuilderMethod(AST ast, TypeDeclaration originalType, String builderName, String methodParameterName) {
        MethodDeclaration builderMethod = staticBuilderMethodSignatureGeneratorFragment.create(ast, getBuilderMethodName(originalType), builderName);
        builderMethod.parameters().add(createParameter(ast, originalType, methodParameterName));
        javadocAdder.addJavadocForCopyInstanceBuilderMethod(ast, originalType.getName().toString(), methodParameterName, builderMethod);
        return builderMethod;
    }

    private SingleVariableDeclaration createParameter(AST ast, TypeDeclaration originalType, String methodParameterName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        methodParameterDeclaration.setName(ast.newSimpleName(methodParameterName));
        return methodParameterDeclaration;
    }

    private String getBuilderMethodName(TypeDeclaration originalType) {
        Map<String, String> replacements = Collections.singletonMap("className", originalType.getName().toString());
        return templateResolver.resolveTemplate(preferenceManager.getPreferenceValue(COPY_INSTANCE_BUILDER_METHOD_PATTERN), replacements);
    }

}
