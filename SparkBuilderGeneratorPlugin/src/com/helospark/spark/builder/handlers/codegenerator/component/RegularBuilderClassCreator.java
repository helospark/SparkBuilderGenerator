package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PublicConstructorWithMandatoryFieldsAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.RegularBuilderCopyInstanceConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.RegularBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

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
    private RegularBuilderCopyInstanceConstructorAdderFragment privateCopyInstanceConstructorAdderFragment;
    private JavadocAdder javadocAdder;
    private PublicConstructorWithMandatoryFieldsAdderFragment publicConstructorWithMandatoryFieldsAdderFragment;

    public RegularBuilderClassCreator(PrivateConstructorAdderFragment privateConstructorAdderFragment, EmptyBuilderClassGeneratorFragment emptyBuilderClassGeneratorFragment,
            BuildMethodCreatorFragment buildMethodCreatorFragment, BuilderFieldAdderFragment builderFieldAdderFragment,
            RegularBuilderWithMethodAdderFragment regularBuilderWithMethodAdderFragment, JavadocAdder javadocAdder,
            RegularBuilderCopyInstanceConstructorAdderFragment regularBuilderCopyInstanceConstructorAdderFragment,
            PublicConstructorWithMandatoryFieldsAdderFragment publicConstructorWithMandatoryFieldsAdderFragment) {
        this.privateConstructorAdderFragment = privateConstructorAdderFragment;
        this.emptyBuilderClassGeneratorFragment = emptyBuilderClassGeneratorFragment;
        this.buildMethodCreatorFragment = buildMethodCreatorFragment;
        this.builderFieldAdderFragment = builderFieldAdderFragment;
        this.regularBuilderWithMethodAdderFragment = regularBuilderWithMethodAdderFragment;
        this.privateCopyInstanceConstructorAdderFragment = regularBuilderCopyInstanceConstructorAdderFragment;
        this.publicConstructorWithMandatoryFieldsAdderFragment = publicConstructorWithMandatoryFieldsAdderFragment;
        this.javadocAdder = javadocAdder;
    }

    public TypeDeclaration createBuilderClass(AST ast, TypeDeclaration originalType, RegularBuilderUserPreference preference) {
        List<BuilderField> builderFields = preference.getBuilderFields();

        TypeDeclaration builderType = emptyBuilderClassGeneratorFragment.createBuilderClass(ast, originalType);

        List<BuilderField> createWithMethodsFor;
        if (preference.isCreatePublicConstructorWithMandatoryFields()) {
            publicConstructorWithMandatoryFieldsAdderFragment.addPublicConstructor(ast, builderType, filterFields(builderFields, field -> field.isMandatory()));
            createWithMethodsFor = filterFields(builderFields, field -> !field.isMandatory());
        } else {
            privateConstructorAdderFragment.addEmptyPrivateConstructor(ast, builderType);
            createWithMethodsFor = builderFields;
        }
        privateCopyInstanceConstructorAdderFragment.addCopyConstructorIfNeeded(ast, builderType, originalType, preference);

        for (BuilderField builderField : builderFields) {
            builderFieldAdderFragment.addFieldToBuilder(ast, builderType, builderField);
        }

        for (BuilderField builderField : createWithMethodsFor) {
            regularBuilderWithMethodAdderFragment.addWithMethodToBuilder(ast, builderType, builderField);
        }
        MethodDeclaration method = buildMethodCreatorFragment.addBuildMethodToBuilder(ast, originalType);
        javadocAdder.addJavadocForBuildMethod(ast, method);
        builderType.bodyDeclarations().add(method);
        return builderType;
    }

    private List<BuilderField> filterFields(List<BuilderField> builderFields, Predicate<BuilderField> predicate) {
        return builderFields
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
