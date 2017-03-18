package com.helospark.spark.builder.handlers.codegenerator.component.fragment;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class StagedBuilderMethodDefiniationCreatorFragment {
    private PreferencesManager preferencesManager;
    private BuilderMethodNameBuilder builderClassMethodNameGeneratorService;
    private NonNullAnnotationAttacher nonNullAnnotationAttacher;

    public StagedBuilderMethodDefiniationCreatorFragment(PreferencesManager preferencesManager, BuilderMethodNameBuilder builderClassMethodNameGeneratorService,
            NonNullAnnotationAttacher nonNullAnnotationAttacher) {
        this.preferencesManager = preferencesManager;
        this.builderClassMethodNameGeneratorService = builderClassMethodNameGeneratorService;
        this.nonNullAnnotationAttacher = nonNullAnnotationAttacher;
    }

    public MethodDeclaration createNewWithMethod(AST ast, NamedVariableDeclarationField namedVariableDeclarationField) {
        String fieldName = namedVariableDeclarationField.getBuilderFieldName();
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(builderClassMethodNameGeneratorService.build(fieldName)));
        //        builderMethod.setReturnType2(ast.newSimpleType(ast.newName(newType.getName().toString()))); // TODO: next declaration
        builderMethod.parameters().add(createMethodParameter(ast, namedVariableDeclarationField.getFieldDeclaration(), fieldName));

        // TODO: Override annotation
        if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
            nonNullAnnotationAttacher.attachNonNull(ast, builderMethod);
        }
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        return builderMethod;
    }

    private SingleVariableDeclaration createMethodParameter(AST ast, FieldDeclaration field, String fieldName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType((Type) ASTNode.copySubtree(ast, field.getType()));
        methodParameterDeclaration.setName(ast.newSimpleName(fieldName));
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS)) {
            nonNullAnnotationAttacher.attachNonNull(ast, methodParameterDeclaration);
        }
        return methodParameterDeclaration;
    }
}
