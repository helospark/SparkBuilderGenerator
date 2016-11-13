package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.helper.AstHelper;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.helper.CallableMethodGenerator;

public class OptionalConvertMethodEmitter implements MethodEmitter {
    private ImportPopulator importPopulator;
    private CallableMethodGenerator callableMethodGenerator;
    private AstHelper astHelper;

    public OptionalConvertMethodEmitter(ImportPopulator importPopulator, CallableMethodGenerator callableMethodGenerator, AstHelper astHelper) {
        this.importPopulator = importPopulator;
        this.callableMethodGenerator = callableMethodGenerator;
        this.astHelper = astHelper;
    }

    @Override
    public MethodDeclaration generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        AST ast = compilationUnitModificationDomain.getAst();

        importPopulator.addImport(compilationUnitModificationDomain, method.getSourceType().getAsString());
        importPopulator.addImport(compilationUnitModificationDomain, method.getDestinationType().getAsString());

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(astHelper.convertType(ast, method.getSourceType()));
        methodParameterDeclaration.setName(ast.newSimpleName(method.getParameterName()));

        MethodDeclaration newMethod = createMethod(method, ast, methodParameterDeclaration);

        Block body = createBody(generationRequest, method, ast);

        newMethod.setBody(body);
        return newMethod;
    }

    private MethodDeclaration createMethod(ConverterMethodCodeGenerationRequest method, AST ast, SingleVariableDeclaration methodParameterDeclaration) {
        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName(method.getName()));
        newMethod.setReturnType2(astHelper.convertType(ast, method.getDestinationType()));
        newMethod.parameters().add(methodParameterDeclaration);
        return newMethod;
    }

    private Block createBody(ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method, AST ast) {
        Block body = ast.newBlock();

        SourceDestinationType sourceDestination = new SourceDestinationType(method.getSourceType(), method.getDestinationType());
        MethodInvocation converterInvocation = callableMethodGenerator.generateMethodCall(ast, generationRequest, method, sourceDestination);
        converterInvocation.arguments().add(ast.newSimpleName("data"));

        VariableDeclarationFragment lambdaParameter = ast.newVariableDeclarationFragment();
        lambdaParameter.setName(ast.newSimpleName("data"));

        LambdaExpression lambdaExpression = ast.newLambdaExpression();
        lambdaExpression.setParentheses(false);
        lambdaExpression.setBody(converterInvocation);
        lambdaExpression.parameters().add(lambdaParameter);

        MethodInvocation mapMethodInvocation = ast.newMethodInvocation();
        mapMethodInvocation.setName(ast.newSimpleName("map"));
        mapMethodInvocation.setExpression(ast.newSimpleName(method.getParameterName()));
        mapMethodInvocation.arguments().add(lambdaExpression);

        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(mapMethodInvocation);
        body.statements().add(returnStatement);
        return body;
    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return converterMethodType.equals(ConverterMethodType.OPTIONAL_CONVERTER);
    }

}
