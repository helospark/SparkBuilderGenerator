package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.InterfaceSetter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates the builder class.
 *
 * @author helospark
 */
public class StagedBuilderClassCreator {
    private PrivateConstructorAdderFragment privateConstructorAdderFragment;
    private EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment;
    private BuildMethodAdderFragment buildMethodAdderFragment;
    private BuilderFieldAdderFragment builderFieldAdderFragment;
    private StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment;
    private InterfaceSetter interfaceSetter;

    public StagedBuilderClassCreator(PrivateConstructorAdderFragment privateConstructorAdderFragment, EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment,
            BuildMethodAdderFragment buildMethodAdderFragment, BuilderFieldAdderFragment builderFieldAdderFragment,
            StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment, InterfaceSetter interfaceSetter) {
        this.privateConstructorAdderFragment = privateConstructorAdderFragment;
        this.emptyBuilderClassGeneratorFragment = emptyBuilderClassGeneratorFragment;
        this.buildMethodAdderFragment = buildMethodAdderFragment;
        this.builderFieldAdderFragment = builderFieldAdderFragment;
        this.stagedBuilderWithMethodAdderFragment = stagedBuilderWithMethodAdderFragment;
        this.interfaceSetter = interfaceSetter;
    }

    public TypeDeclaration createBuilderClass(CompilationUnitModificationDomain modificationDomain,
            List<StagedBuilderProperties> stagedBuilderProperties, List<TypeDeclaration> stageInterfaces) {
        AST ast = modificationDomain.getAst();
        TypeDeclaration originalType = modificationDomain.getOriginalType();
        TypeDeclaration builderType = emptyBuilderClassGeneratorFragment.createBuilderClass(ast, originalType);

        privateConstructorAdderFragment.addEmptyPrivateConstructor(ast, builderType);

        // add all with method of all stages to the builder class
        for (StagedBuilderProperties stagedBuilderProperty : stagedBuilderProperties) {
            StagedBuilderProperties nextStage = stagedBuilderProperty.getNextStage().orElse(stagedBuilderProperty);
            for (NamedVariableDeclarationField namedVariableDeclarationField : stagedBuilderProperty.getNamedVariableDeclarationField()) {
                builderFieldAdderFragment.addFieldToBuilder(ast, builderType, namedVariableDeclarationField);
                stagedBuilderWithMethodAdderFragment.addWithMethodToBuilder(ast, builderType,
                        namedVariableDeclarationField, nextStage);
            }
        }
        buildMethodAdderFragment.addBuildMethodToBuilder(ast, originalType, builderType);
        setSuperInterfaces(ast, builderType, stageInterfaces);
        return builderType;
    }

    private void setSuperInterfaces(AST ast, TypeDeclaration builderType, List<TypeDeclaration> stageInterfaces) {
        stageInterfaces.stream()
                .forEach(stageInterface -> interfaceSetter.setInterface(ast, builderType, stageInterface));

    }

}
