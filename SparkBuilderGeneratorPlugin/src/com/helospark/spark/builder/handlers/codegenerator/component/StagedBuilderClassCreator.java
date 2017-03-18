package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.BuilderMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.StagedBuilderInterfaceAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.StagedBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates the builder class.
 *
 * @author helospark
 */
public class StagedBuilderClassCreator {
    private PrivateConstructorAdderFragment privateConstructorAdderFragment;
    private EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment;
    private BuilderMethodAdderFragment builderMethodAdderFragment;
    private BuilderFieldAdderFragment builderFieldAdderFragment;

    private StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment;
    private StagedBuilderInterfaceAdderFragment stagedBuilderInterfaceAdderFragment;

    public StagedBuilderClassCreator(PrivateConstructorAdderFragment privateConstructorAdderFragment, EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment,
            BuilderMethodAdderFragment builderMethodAdderFragment, BuilderFieldAdderFragment builderFieldAdderFragment,
            StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment, StagedBuilderInterfaceAdderFragment stagedBuilderInterfaceAdderFragment) {
        this.privateConstructorAdderFragment = privateConstructorAdderFragment;
        this.emptyBuilderClassGeneratorFragment = emptyBuilderClassGeneratorFragment;
        this.builderMethodAdderFragment = builderMethodAdderFragment;
        this.builderFieldAdderFragment = builderFieldAdderFragment;
        this.stagedBuilderWithMethodAdderFragment = stagedBuilderWithMethodAdderFragment;
    }

    public TypeDeclaration createBuilderClass(AST ast, TypeDeclaration originalName, List<NamedVariableDeclarationField> namedVariableDeclarations) {
        TypeDeclaration builderType = emptyBuilderClassGeneratorFragment.createBuilderClass(ast, originalName);
        privateConstructorAdderFragment.addEmptyPrivateConstructor(ast, builderType);
        for (NamedVariableDeclarationField namedVariableDeclarationField : namedVariableDeclarations) {
            stagedBuilderInterfaceAdderFragment.createAndAddInterfaceFor(ast, builderType, namedVariableDeclarationField);
            builderFieldAdderFragment.addFieldToBuilder(ast, builderType, namedVariableDeclarationField);
            stagedBuilderWithMethodAdderFragment.addWithMethodToBuilder(ast, builderType, namedVariableDeclarationField);
        }
        builderMethodAdderFragment.addBuildMethodToBuilder(ast, originalName, builderType);
        return builderType;
    }

}
