package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.RegularBuilderCopyMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.RegularBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Generates the builder class.
 *
 * @author helospark
 */
public class RegularBuilderClassCreator {
    private PrivateConstructorAdderFragment privateConstructorAdderFragment;
    private EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment;
    private BuildMethodCreatorFragment buildMethodCreatorFragment;
    private BuilderFieldAdderFragment builderFieldAdderFragment;
    private RegularBuilderWithMethodAdderFragment regularBuilderWithMethodAdderFragment;
    private RegularBuilderCopyMethodAdderFragment regularBuilderCopyMethodAdderFragment;
    private JavadocAdder javadocAdder;

    public RegularBuilderClassCreator(PrivateConstructorAdderFragment privateConstructorAdderFragment, EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment,
            BuildMethodCreatorFragment buildMethodCreatorFragment, BuilderFieldAdderFragment builderFieldAdderFragment,
            RegularBuilderWithMethodAdderFragment regularBuilderWithMethodAdderFragment, JavadocAdder javadocAdder,
            RegularBuilderCopyMethodAdderFragment regularBuilderCopyMethodAdderFragment) {
        this.privateConstructorAdderFragment = privateConstructorAdderFragment;
        this.emptyBuilderClassGeneratorFragment = emptyBuilderClassGeneratorFragment;
        this.buildMethodCreatorFragment = buildMethodCreatorFragment;
        this.builderFieldAdderFragment = builderFieldAdderFragment;
        this.regularBuilderWithMethodAdderFragment = regularBuilderWithMethodAdderFragment;
        this.regularBuilderCopyMethodAdderFragment = regularBuilderCopyMethodAdderFragment;
        this.javadocAdder = javadocAdder;
    }

    public TypeDeclaration createBuilderClass(AST ast, TypeDeclaration originalType, List<BuilderField> builderFields) {
        TypeDeclaration builderType = emptyBuilderClassGeneratorFragment.createBuilderClass(ast, originalType);
        privateConstructorAdderFragment.addEmptyPrivateConstructor(ast, builderType);
        regularBuilderCopyMethodAdderFragment.addCopyMethodIfNeeded(ast, builderType, originalType, builderFields);
        for (BuilderField builderField : builderFields) {
            builderFieldAdderFragment.addFieldToBuilder(ast, builderType, builderField);
            regularBuilderWithMethodAdderFragment.addWithMethodToBuilder(ast, builderType, builderField);
        }
        MethodDeclaration method = buildMethodCreatorFragment.addBuildMethodToBuilder(ast, originalType);
        javadocAdder.addJavadocForBuildMethod(ast, method);
        builderType.bodyDeclarations().add(method);
        return builderType;
    }

}
