package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorBodyCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.ConstructorInsertionFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorMethodDefinitionCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Generates a private constructor that initializes fields.
 * Something like:
 * <pre>
 * private Clazz(Builder builder) {
 *   this.firstField = builder.firstField;
 *   this.secondField = builder.secondField;
 * }
 * </pre>
 * @author helospark
 */
public class PrivateInitializingConstructorCreator {
    private PrivateConstructorMethodDefinitionCreationFragment privateConstructorMethodDefinitionCreationFragment;
    private PrivateConstructorBodyCreationFragment privateConstructorBodyCreationFragment;
    private ConstructorInsertionFragment constructorInsertionFragment;

    public PrivateInitializingConstructorCreator(PrivateConstructorMethodDefinitionCreationFragment privateConstructorMethodDefinitionCreationFragment,
            PrivateConstructorBodyCreationFragment privateConstructorBodyCreationFragment, ConstructorInsertionFragment constructorInsertionFragment) {
        this.privateConstructorMethodDefinitionCreationFragment = privateConstructorMethodDefinitionCreationFragment;
        this.privateConstructorBodyCreationFragment = privateConstructorBodyCreationFragment;
        this.constructorInsertionFragment = constructorInsertionFragment;
    }

    public void addPrivateConstructorToCompilationUnit(AST ast, AbstractTypeDeclaration originalType, AbstractTypeDeclaration builderType, ListRewrite listRewrite,
            List<BuilderField> builderFields) {
        Block body = privateConstructorBodyCreationFragment.createBody(ast, builderType, builderFields);
        MethodDeclaration privateConstructorDefinition = privateConstructorMethodDefinitionCreationFragment.createPrivateConstructorDefinition(ast, originalType, builderType,
                builderFields);
        privateConstructorDefinition.setBody(body);
        constructorInsertionFragment.insertMethodToFirstPlace(originalType, listRewrite, privateConstructorDefinition);
    }

}
