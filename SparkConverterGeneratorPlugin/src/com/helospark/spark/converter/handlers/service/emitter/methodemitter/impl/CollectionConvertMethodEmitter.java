package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ClassNameToVariableNameConverter;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.helper.AstHelper;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.helper.CallableMethodGenerator;

public class CollectionConvertMethodEmitter implements MethodEmitter {
    private ImportPopulator importPopulator;
    private ClassNameToVariableNameConverter classNameToVariableNameConverter;
    private CallableMethodGenerator callableMethodGenerator;
    private AstHelper astHelper;

    public CollectionConvertMethodEmitter(ImportPopulator importPopulator, ClassNameToVariableNameConverter classNameToVariableNameConverter,
            CallableMethodGenerator callableMethodGenerator, AstHelper astHelper) {
        this.importPopulator = importPopulator;
        this.classNameToVariableNameConverter = classNameToVariableNameConverter;
        this.callableMethodGenerator = callableMethodGenerator;
        this.astHelper = astHelper;
    }

    @Override
    public MethodDeclaration generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest methodRequest) {
        AST ast = compilationUnitModificationDomain.getAst();

        importPopulator.addImport(compilationUnitModificationDomain, methodRequest.getSourceType().getAsString());
        importPopulator.addImport(compilationUnitModificationDomain, methodRequest.getDestinationType().getAsString());

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(astHelper.convertType(ast, methodRequest.getSourceType()));
        methodParameterDeclaration.setName(ast.newSimpleName(methodRequest.getParameterName()));

        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName(methodRequest.getName()));
        newMethod.setReturnType2(astHelper.convertType(ast, methodRequest.getDestinationType()));
        newMethod.parameters().add(methodParameterDeclaration);
        Block body = ast.newBlock();

        if (useForLoop()) {
            VariableDeclarationStatement resultVariableList = createResultListVariable(ast, compilationUnitModificationDomain, methodRequest);
            EnhancedForStatement forLoop = createForLoop(ast, compilationUnitModificationDomain, generationRequest, methodRequest);
            ReturnStatement returnStatement = createReturnStatement(ast);

            body.statements().add(resultVariableList);
            body.statements().add(forLoop);
            body.statements().add(returnStatement);
        }
        newMethod.setBody(body);
        return newMethod;
    }

    private ReturnStatement createReturnStatement(AST ast) {
        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(ast.newName("result"));
        return returnStatement;
    }

    private EnhancedForStatement createForLoop(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
        svd.setType(astHelper.convertType(ast, method.getSourceType().getTemplates().get(0)));
        svd.setName(ast.newSimpleName(classNameToVariableNameConverter.convert("iterator")));

        EnhancedForStatement forLoop = ast.newEnhancedForStatement();
        forLoop.setParameter(svd);
        forLoop.setExpression(ast.newSimpleName(method.getParameterName()));
        forLoop.setBody(createBody(ast, compilationUnitModificationDomain, generationRequest, method));
        return forLoop;
    }

    private VariableDeclarationStatement createResultListVariable(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain,
            ConverterMethodCodeGenerationRequest method) {
        VariableDeclarationFragment singleVariableDeclaration = ast.newVariableDeclarationFragment();
        singleVariableDeclaration.setName(ast.newSimpleName("result"));
        ClassInstanceCreation resultInitializer = ast.newClassInstanceCreation();
        importPopulator.addImport(compilationUnitModificationDomain, "java.util.ArrayList");
        resultInitializer.setType(ast.newParameterizedType(ast.newSimpleType(ast.newName("ArrayList"))));
        singleVariableDeclaration.setInitializer(resultInitializer);

        VariableDeclarationStatement resultListVariableDeclaration = ast.newVariableDeclarationStatement(singleVariableDeclaration);
        resultListVariableDeclaration.setType(astHelper.convertType(ast, method.getDestinationType()));

        return resultListVariableDeclaration;
    }

    private Statement createBody(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        Block newBlock = ast.newBlock();

        SourceDestinationType sourceDestination = new SourceDestinationType(method.getSourceType(), method.getDestinationType());
        MethodInvocation converterInvocation = callableMethodGenerator.generateMethodCall(ast, generationRequest, method, sourceDestination);
        converterInvocation.arguments().add(ast.newSimpleName("iterator"));

        MethodInvocation resultAddInvocation = ast.newMethodInvocation();
        resultAddInvocation.setExpression(ast.newName("result"));
        resultAddInvocation.setName(ast.newSimpleName("add"));
        resultAddInvocation.arguments().add(converterInvocation);

        newBlock.statements().add(ast.newExpressionStatement(resultAddInvocation));
        return newBlock;
    }

    private boolean useForLoop() {
        return true;
    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return converterMethodType.equals(ConverterMethodType.COLLECTION_CONVERTER);
    }

}
