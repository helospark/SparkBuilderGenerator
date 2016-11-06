package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.helper;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.codegenerator.ConverterTypeCodeGenerator;

public class ConverterConvertMethodGenerator {
    private List<ConverterTypeCodeGenerator> codeGenerators;

    public ConverterConvertMethodGenerator(List<ConverterTypeCodeGenerator> codeGenerators) {
        this.codeGenerators = codeGenerators;
    }

    public MethodDeclaration generate(CompilationUnitModificationDomain compilationUnitModificationDomain, ConvertableDomain convertableDomain) {
        AST ast = compilationUnitModificationDomain.getAst();
        String destinationTypeName = convertableDomain.getDestinationObject().getType().getFullyQualifiedName();

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        SimpleType sourceType = ast.newSimpleType(ast.newName(convertableDomain.getSourceObject().getType().getFullyQualifiedName()));
        methodParameterDeclaration.setType(sourceType);
        methodParameterDeclaration.setName(ast.newSimpleName("asd"));

        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName("convert"));
        newMethod.setReturnType2(ast.newSimpleType(ast.newName(destinationTypeName)));
        newMethod.parameters().add(methodParameterDeclaration);
        Block body = ast.newBlock();

        Expression lastDeclaration = null;
        if (convertableDomain.isUseBuilder()) {
            MethodInvocation methodInvocation = compilationUnitModificationDomain.getAst().newMethodInvocation();
            methodInvocation.setName(ast.newSimpleName("builder"));
            methodInvocation.setExpression(ast.newName(destinationTypeName));

            lastDeclaration = methodInvocation;
        } else {
            ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
            SimpleType destinationType = ast.newSimpleType(ast.newName(destinationTypeName));
            newClassInstanceCreation.setType((SimpleType) ASTNode.copySubtree(ast, destinationType));

            VariableDeclarationFragment singleVariableDeclaration = ast.newVariableDeclarationFragment();
            singleVariableDeclaration.setName(ast.newSimpleName("destination"));
            singleVariableDeclaration.setInitializer(newClassInstanceCreation);

            VariableDeclarationStatement vds = ast.newVariableDeclarationStatement(singleVariableDeclaration);
            vds.setType((SimpleType) ASTNode.copySubtree(ast, destinationType));
            body.statements().add(vds);
        }

        for (ConvertableDomainParameter parameter : convertableDomain.getConvertableDomainParameters()) {
            if (!convertableDomain.isUseBuilder()) {
                lastDeclaration = ast.newName("destination");
            }
            Expression generatedCopy = copyParameter(compilationUnitModificationDomain, body, lastDeclaration, ast.newName("asd"), parameter);
            if (!convertableDomain.isUseBuilder()) {
                body.statements().add(ast.newExpressionStatement(generatedCopy));
            } else {
                lastDeclaration = generatedCopy;
            }
        }
        ReturnStatement returnStatement = ast.newReturnStatement();
        if (convertableDomain.isUseBuilder()) {
            MethodInvocation builderMethodInvocation = ast.newMethodInvocation();
            builderMethodInvocation.setExpression(lastDeclaration);
            builderMethodInvocation.setName(ast.newSimpleName("build"));
            returnStatement.setExpression(builderMethodInvocation);
        } else {
            returnStatement.setExpression(ast.newName("destination"));
        }
        body.statements().add(returnStatement);
        newMethod.setBody(body);
        return newMethod;
    }

    private Expression copyParameter(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression lastDeclaration, Expression source,
            ConvertableDomainParameter parameter) {
        ConverterTypeCodeGenerator codeGenerator = findCodeGenerator(parameter);
        return codeGenerator.generate(compilationUnitModificationDomain, body, lastDeclaration, source, parameter);
    }

    private ConverterTypeCodeGenerator findCodeGenerator(ConvertableDomainParameter parameter) {
        return codeGenerators.stream()
                .filter(generator -> generator.canHandle(parameter.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Generation type not supported: " + parameter.getType()));
    }

}
