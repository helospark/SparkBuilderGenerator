package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StagedBuilderMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

@SuppressWarnings("unchecked")
public class StagedBuilderWithMethodAdderFragment {
    private StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment;

    public StagedBuilderWithMethodAdderFragment(
            StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment) {
        this.stagedBuilderMethodDefiniationCreatorFragment = stagedBuilderMethodDefiniationCreatorFragment;
    }

    // TODO fix parameters
    public void addWithMethodToBuilder(AST ast, TypeDeclaration stagedBuilderType,
            NamedVariableDeclarationField namedVariableDeclarationField,
            StagedBuilderProperties nextStage) {
        String originalFieldName = namedVariableDeclarationField.getOriginalFieldName();
        String builderFieldName = namedVariableDeclarationField.getBuilderFieldName();
        Block newBlock = createWithMethodBody(ast, originalFieldName, builderFieldName);
        MethodDeclaration newWithMethod = stagedBuilderMethodDefiniationCreatorFragment.createNewWithMethod(ast,
                namedVariableDeclarationField, nextStage);
        newWithMethod.setBody(newBlock);
        stagedBuilderType.bodyDeclarations().add(newWithMethod);
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

}
