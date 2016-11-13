package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class ConverterConstructorEmitter {

    public void addConstructor(TypeDeclaration converter, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        AST ast = compilationUnit.getAst();
        MethodDeclaration constructorMethod = ast.newMethodDeclaration();
        constructorMethod.setConstructor(true);
        constructorMethod.setName(ast.newSimpleName(generationRequest.getClassName()));
        constructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        addDependenciesAsParameters(ast, constructorMethod, generationRequest);
        addBody(ast, constructorMethod, generationRequest);

        converter.bodyDeclarations().add(constructorMethod);
    }

    private void addDependenciesAsParameters(AST ast, MethodDeclaration constructorMethod, ConverterTypeCodeGenerationRequest generationRequest) {
        for (ConverterTypeCodeGenerationRequest dependency : generationRequest.getDependencies()) {
            SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
            SimpleType sourceType = ast.newSimpleType(ast.newName(dependency.getClassName()));
            methodParameterDeclaration.setType(sourceType);
            methodParameterDeclaration.setName(ast.newSimpleName(dependency.getFieldName()));

            constructorMethod.parameters().add(methodParameterDeclaration);
        }
    }

    private void addBody(AST ast, MethodDeclaration constructorMethod, ConverterTypeCodeGenerationRequest generationRequest) {
        Block body = ast.newBlock();

        for (ConverterTypeCodeGenerationRequest dependency : generationRequest.getDependencies()) {
            Assignment assignment = ast.newAssignment();
            FieldAccess fieldAccess = ast.newFieldAccess();
            fieldAccess.setExpression(ast.newThisExpression());
            fieldAccess.setName(ast.newSimpleName(dependency.getFieldName()));

            Expression parameterAccess = ast.newSimpleName(dependency.getFieldName());

            assignment.setLeftHandSide(fieldAccess);
            assignment.setRightHandSide(parameterAccess);

            body.statements().add(ast.newExpressionStatement(assignment));
        }
        constructorMethod.setBody(body);
    }

}
