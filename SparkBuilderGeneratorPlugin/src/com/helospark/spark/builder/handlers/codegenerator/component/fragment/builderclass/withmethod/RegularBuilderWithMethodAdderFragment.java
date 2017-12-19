package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Adds a with method for regular builder.
 * Generated code is something like:
 * <pre>
 * public Builder withFirstField(String firstField) {
 *   this.firstField = firstField;
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderWithMethodAdderFragment {
    private PreferencesManager preferencesManager;
    private JavadocAdder javadocAdder;
    private MarkerAnnotationAttacher markerAnnotationAttacher;
    private BuilderMethodNameBuilder builderClassMethodNameGeneratorService;
    private WithMethodParameterCreatorFragment withMethodParameterCreatorFragment;

    public RegularBuilderWithMethodAdderFragment(PreferencesManager preferencesManager,
            JavadocAdder javadocAdder, MarkerAnnotationAttacher markerAnnotationAttacher,
            BuilderMethodNameBuilder builderClassMethodNameGeneratorService,
            WithMethodParameterCreatorFragment withMethodParameterCreatorFragment) {
        this.preferencesManager = preferencesManager;
        this.javadocAdder = javadocAdder;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
        this.builderClassMethodNameGeneratorService = builderClassMethodNameGeneratorService;
        this.withMethodParameterCreatorFragment = withMethodParameterCreatorFragment;
    }

    public void addWithMethodToBuilder(AST ast, TypeDeclaration newType,
            BuilderField builderField) {
        String originalFieldName = builderField.getOriginalFieldName();
        String builderFieldName = builderField.getBuilderFieldName();
        Block newBlock = createWithMethodBody(ast, originalFieldName, builderFieldName);
        SingleVariableDeclaration methodParameterDeclaration = withMethodParameterCreatorFragment.createWithMethodParameter(ast,
                builderField.getFieldType(), builderFieldName);
        MethodDeclaration newMethod = createNewWithMethod(ast, builderFieldName, newBlock, methodParameterDeclaration,
                newType, builderField);
        newType.bodyDeclarations().add(newMethod);
    }

    private Block createWithMethodBody(AST ast, String originalFieldName, String builderFieldName) {
        Block newBlock = ast.newBlock();
        ReturnStatement builderReturnStatement = ast.newReturnStatement();
        builderReturnStatement.setExpression(ast.newThisExpression());

        Assignment newAssignment = ast.newAssignment();

        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(ast.newSimpleName(originalFieldName));
        newAssignment.setLeftHandSide(fieldAccess);
        newAssignment.setRightHandSide(ast.newSimpleName(builderFieldName));

        newBlock.statements().add(ast.newExpressionStatement(newAssignment));
        newBlock.statements().add(builderReturnStatement);
        return newBlock;
    }

    private MethodDeclaration createNewWithMethod(AST ast, String fieldName, Block newBlock,
            SingleVariableDeclaration methodParameterDeclaration, TypeDeclaration builderType,
            BuilderField builderField) {
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(builderClassMethodNameGeneratorService.build(fieldName)));
        builderMethod.setReturnType2(ast.newSimpleType(
                ast.newName(builderType.getName().getIdentifier())));
        builderMethod.setBody(newBlock);
        builderMethod.parameters().add(methodParameterDeclaration);

        javadocAdder.addJavadocForWithMethod(ast, fieldName, builderMethod);
        if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
            markerAnnotationAttacher.attachNonNull(ast, builderMethod);
        }
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        return builderMethod;
    }

}
