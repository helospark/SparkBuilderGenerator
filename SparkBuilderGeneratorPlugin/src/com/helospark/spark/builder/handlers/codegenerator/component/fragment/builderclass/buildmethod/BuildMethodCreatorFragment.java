package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;

public class BuildMethodCreatorFragment {
    private BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment;
    private BuildMethodBodyCreator buildMethodBodyCreator;
    private JavadocAdder javadocAdder;

    public BuildMethodCreatorFragment(BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment,
            BuildMethodBodyCreator buildMethodBodyCreator, JavadocAdder javadocAdder) {
        this.buildMethodDeclarationCreatorFragment = buildMethodDeclarationCreatorFragment;
        this.buildMethodBodyCreator = buildMethodBodyCreator;
        this.javadocAdder = javadocAdder;
    }

    public MethodDeclaration addBuildMethodToBuilder(AST ast, TypeDeclaration originalType) {
        Block block = buildMethodBodyCreator.createBody(ast, originalType);
        MethodDeclaration method = buildMethodDeclarationCreatorFragment.createMethod(ast, originalType);
        method.setBody(block);
        return method;
    }

}
