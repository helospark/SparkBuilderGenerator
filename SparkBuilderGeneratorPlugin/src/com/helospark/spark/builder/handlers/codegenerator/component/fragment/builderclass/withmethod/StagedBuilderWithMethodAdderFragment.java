package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import static com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher.OVERRIDE_ANNOTATION;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

@SuppressWarnings("unchecked")
public class StagedBuilderWithMethodAdderFragment {
    private StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment;
    private MarkerAnnotationAttacher markerAnnotationAttacher;

    public StagedBuilderWithMethodAdderFragment(
            StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment,
            MarkerAnnotationAttacher markerAnnotationAttacher) {
        this.stagedBuilderMethodDefiniationCreatorFragment = stagedBuilderMethodDefiniationCreatorFragment;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
    }

    public void addWithMethodToBuilder(AST ast, TypeDeclaration stagedBuilderType,
            NamedVariableDeclarationField namedVariableDeclarationField,
            StagedBuilderProperties nextStage) {
        Block newBlock = createWithMethodBody(ast, namedVariableDeclarationField);
        MethodDeclaration newWithMethod = stagedBuilderMethodDefiniationCreatorFragment.createNewWithMethod(ast,
                namedVariableDeclarationField, nextStage);
        newWithMethod.setBody(newBlock);
        markerAnnotationAttacher.attachAnnotation(ast, newWithMethod, OVERRIDE_ANNOTATION);
        stagedBuilderType.bodyDeclarations().add(newWithMethod);
    }

    private Block createWithMethodBody(AST ast, NamedVariableDeclarationField namedVariableDeclarationField) {
        String originalFieldName = namedVariableDeclarationField.getOriginalFieldName();
        String builderFieldName = namedVariableDeclarationField.getBuilderFieldName();

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

}
