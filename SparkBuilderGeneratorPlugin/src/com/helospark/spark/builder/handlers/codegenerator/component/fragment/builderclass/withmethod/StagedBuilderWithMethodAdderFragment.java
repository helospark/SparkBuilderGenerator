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
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Adds a with method for staged builder.
 * Generated code is something like:
 * <pre>
 * public ISecondStage withFirstField(String firstField) {
 *   this.firstField = firstField;
 *   return this;
 * }
 * </pre>
 * @author helospark
 */
public class StagedBuilderWithMethodAdderFragment {
    private StagedBuilderWithMethodDefiniationCreatorFragment stagedBuilderWithMethodDefiniationCreatorFragment;
    private MarkerAnnotationAttacher markerAnnotationAttacher;

    public StagedBuilderWithMethodAdderFragment(
            StagedBuilderWithMethodDefiniationCreatorFragment stagedBuilderWithMethodDefiniationCreatorFragment,
            MarkerAnnotationAttacher markerAnnotationAttacher) {
        this.stagedBuilderWithMethodDefiniationCreatorFragment = stagedBuilderWithMethodDefiniationCreatorFragment;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
    }

    public void addWithMethodToBuilder(AST ast, TypeDeclaration stagedBuilderType,
            BuilderField builderField,
            StagedBuilderProperties nextStage) {
        Block newBlock = createWithMethodBody(ast, builderField);
        MethodDeclaration newWithMethod = stagedBuilderWithMethodDefiniationCreatorFragment.createNewWithMethod(ast,
                builderField, nextStage);
        newWithMethod.setBody(newBlock);
        markerAnnotationAttacher.attachAnnotation(ast, newWithMethod, OVERRIDE_ANNOTATION);
        stagedBuilderType.bodyDeclarations().add(newWithMethod);
    }

    private Block createWithMethodBody(AST ast, BuilderField builderField) {
        String originalFieldName = builderField.getOriginalFieldName();
        String builderFieldName = builderField.getBuilderFieldName();

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
