package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ClassNameToVariableNameConverter;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.codegenerator.impl.helper.CallableMethodGenerator;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;

public class CollectionConvertMethodEmitter implements MethodEmitter {
    private ImportPopulator importPopulator;
    private ClassNameToVariableNameConverter classNameToVariableNameConverter;
    private CallableMethodGenerator callableMethodGenerator;

    public CollectionConvertMethodEmitter(ImportPopulator importPopulator, ClassNameToVariableNameConverter classNameToVariableNameConverter,
            CallableMethodGenerator callableMethodGenerator) {
        this.importPopulator = importPopulator;
        this.classNameToVariableNameConverter = classNameToVariableNameConverter;
        this.callableMethodGenerator = callableMethodGenerator;
    }

    @Override
    public MethodDeclaration generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest methodRequest) {
        AST ast = compilationUnitModificationDomain.getAst();

        String sourceTypeName = importPopulator.addImport(compilationUnitModificationDomain, methodRequest.getSourceType().getAsString());
        String destinationTypeName = importPopulator.addImport(compilationUnitModificationDomain, methodRequest.getDestinationType().getAsString());

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        SimpleType sourceType = ast.newSimpleType(ast.newName(sourceTypeName));
        methodParameterDeclaration.setType(sourceType);
        methodParameterDeclaration.setName(ast.newSimpleName(methodRequest.getParameterName()));

        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName(methodRequest.getName()));
        newMethod.setReturnType2(ast.newSimpleType(ast.newName(destinationTypeName)));
        newMethod.parameters().add(methodParameterDeclaration);
        Block body = ast.newBlock();

        if (useForLoop()) {
            VariableDeclarationStatement resultVariableList = createResultListVariable(ast, methodRequest);
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
        String sourceElementType = method.getSourceType().getTemplates().get(0).getType().getElementName();

        SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
        svd.setType(ast.newSimpleType(ast.newName(sourceElementType)));
        svd.setName(ast.newSimpleName(classNameToVariableNameConverter.convert(sourceElementType)));

        EnhancedForStatement forLoop = ast.newEnhancedForStatement();
        forLoop.setParameter(svd);
        forLoop.setExpression(ast.newSimpleName(method.getParameterName()));
        forLoop.setBody(createBody(ast, compilationUnitModificationDomain, generationRequest, method));
        return forLoop;
    }

    private VariableDeclarationStatement createResultListVariable(AST ast, ConverterMethodCodeGenerationRequest method) {
        VariableDeclarationFragment singleVariableDeclaration = ast.newVariableDeclarationFragment();
        singleVariableDeclaration.setName(ast.newSimpleName("result"));
        singleVariableDeclaration.setInitializer(ast.newNullLiteral());

        VariableDeclarationStatement resultListVariableDeclaration = ast.newVariableDeclarationStatement(singleVariableDeclaration);
        resultListVariableDeclaration.setType(ast.newSimpleType(ast.newName(method.getDestinationType().getAsString())));

        return resultListVariableDeclaration;
    }

    private Statement createBody(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        Block newBlock = ast.newBlock();

        SourceDestinationType sourceDestination = new SourceDestinationType(method.getSourceType(), method.getDestinationType());
        MethodInvocation converterInvocation = callableMethodGenerator.generateMethodCall(ast, generationRequest, method, sourceDestination);

        MethodInvocation resultAddInvocation = ast.newMethodInvocation();
        resultAddInvocation.setExpression(ast.newName("result"));
        resultAddInvocation.setName(ast.newSimpleName("add"));
        resultAddInvocation.arguments().add(converterInvocation);

        newBlock.statements().add(resultAddInvocation);
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
