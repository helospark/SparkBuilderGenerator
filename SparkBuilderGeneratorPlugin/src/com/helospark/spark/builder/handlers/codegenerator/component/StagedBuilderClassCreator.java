package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher.OVERRIDE_ANNOTATION;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.InterfaceSetter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Generates the builder class.
 *
 * @author helospark
 */
public class StagedBuilderClassCreator {
    private PrivateConstructorAdderFragment privateConstructorAdderFragment;
    private EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment;
    private BuildMethodCreatorFragment buildMethodCreatorFragment;
    private BuilderFieldAdderFragment builderFieldAdderFragment;
    private StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment;
    private InterfaceSetter interfaceSetter;
    private MarkerAnnotationAttacher markerAnnotationAttacher;

    public StagedBuilderClassCreator(PrivateConstructorAdderFragment privateConstructorAdderFragment, EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment,
            BuildMethodCreatorFragment buildMethodCreatorFragment, BuilderFieldAdderFragment builderFieldAdderFragment,
            StagedBuilderWithMethodAdderFragment stagedBuilderWithMethodAdderFragment, InterfaceSetter interfaceSetter,
            MarkerAnnotationAttacher markerAnnotationAttacher) {
        this.privateConstructorAdderFragment = privateConstructorAdderFragment;
        this.emptyBuilderClassGeneratorFragment = emptyBuilderClassGeneratorFragment;
        this.buildMethodCreatorFragment = buildMethodCreatorFragment;
        this.builderFieldAdderFragment = builderFieldAdderFragment;
        this.stagedBuilderWithMethodAdderFragment = stagedBuilderWithMethodAdderFragment;
        this.interfaceSetter = interfaceSetter;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
    }

    public AbstractTypeDeclaration createBuilderClass(CompilationUnitModificationDomain modificationDomain,
            List<StagedBuilderProperties> stagedBuilderProperties, List<AbstractTypeDeclaration> stageInterfaces) {
        AST ast = modificationDomain.getAst();
        AbstractTypeDeclaration originalType = modificationDomain.getOriginalType();
        AbstractTypeDeclaration builderType = emptyBuilderClassGeneratorFragment.createBuilderClass(ast, originalType);

        privateConstructorAdderFragment.addEmptyPrivateConstructor(ast, builderType);

        // add all with method of all stages to the builder class
        for (StagedBuilderProperties stagedBuilderProperty : stagedBuilderProperties) {
            StagedBuilderProperties nextStage = stagedBuilderProperty.getNextStage().orElse(stagedBuilderProperty);
            for (BuilderField builderField : stagedBuilderProperty.getNamedVariableDeclarationField()) {
                builderFieldAdderFragment.addFieldToBuilder(ast, builderType, builderField);
                stagedBuilderWithMethodAdderFragment.addWithMethodToBuilder(ast, builderType,
                        builderField, nextStage);
            }
        }
        for (MethodDeclaration customMethod : modificationDomain.getSavedCustomMethodDeclarations()) {
            builderType.bodyDeclarations().add(customMethod);
        }
        MethodDeclaration method = buildMethodCreatorFragment.addBuildMethodToBuilder(ast, originalType);
        builderType.bodyDeclarations().add(method);
        markerAnnotationAttacher.attachAnnotation(ast, method, OVERRIDE_ANNOTATION);
        setSuperInterfaces(ast, builderType, stageInterfaces);
        return builderType;
    }

    private void setSuperInterfaces(AST ast, AbstractTypeDeclaration builderType, List<AbstractTypeDeclaration> stageInterfaces) {
        stageInterfaces.stream()
                .forEach(stageInterface -> interfaceSetter.setInterface(ast, builderType, stageInterface));

    }

}
