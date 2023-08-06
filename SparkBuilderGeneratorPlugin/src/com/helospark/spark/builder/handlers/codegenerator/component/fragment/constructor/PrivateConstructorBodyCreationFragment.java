package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ClassFieldSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.SuperSetterBasedBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ThisConstructorParameterSetterBuilderField;

/**
 * Creates the body of the private constructor that initializes the class.
 * Generated code is something like:
 * <pre>
 * {
 *   super(superField, superField2);
 *   this.firstField = builder.firstField;
 *   this.secondField = builder.secondField;
 *   setSetterField(builder.setterField);
 *   setSetterField2(builder.setterField2);
 * }
 * </pre>
 * @author helospark
 */
public class PrivateConstructorBodyCreationFragment {
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private FieldSetterAdderFragment fieldSetterAdderFragment;
    private BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment;
    private SuperFieldSetterMethodAdderFragment superFieldSetterMethodAdderFragment;

    public PrivateConstructorBodyCreationFragment(TypeDeclarationToVariableNameConverter AbstractTypeDeclarationToVariableNameConverter,
            FieldSetterAdderFragment fieldSetterAdderFragment,
            BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment, SuperFieldSetterMethodAdderFragment superFieldSetterMethodAdderFragment) {
        this.typeDeclarationToVariableNameConverter = AbstractTypeDeclarationToVariableNameConverter;
        this.fieldSetterAdderFragment = fieldSetterAdderFragment;
        this.builderFieldAccessCreatorFragment = builderFieldAccessCreatorFragment;
        this.superFieldSetterMethodAdderFragment = superFieldSetterMethodAdderFragment;
    }

    public Block createBody(AST ast, AbstractTypeDeclaration builderType, List<BuilderField> builderFields) {
        Block body = ast.newBlock();
        String builderName = typeDeclarationToVariableNameConverter.convert(builderType);

        populateBodyWithSuperConstructorCall(ast, builderType, body, getFieldsOfClass(builderFields, ConstructorParameterSetterBuilderField.class));
        populateBodyWithThisConstructorCall(ast, builderType, body, getFieldsOfClass(builderFields, ThisConstructorParameterSetterBuilderField.class));
        fieldSetterAdderFragment.populateBodyWithFieldSetCalls(ast, builderName, body,
                getFieldsOfClass(builderFields, ClassFieldSetterBuilderField.class));
        superFieldSetterMethodAdderFragment.populateBodyWithSuperSetterCalls(ast, builderName, body, getFieldsOfClass(builderFields, SuperSetterBasedBuilderField.class));

        return body;
    }

    private <T extends BuilderField> List<T> getFieldsOfClass(List<BuilderField> builderFields, Class<T> classToGet) {
        return builderFields.stream()
                .filter(field -> field.getClass().equals(classToGet))
                .map(field -> classToGet.cast(field))
                .collect(Collectors.toList());
    }

    private void populateBodyWithSuperConstructorCall(AST ast, AbstractTypeDeclaration builderType, Block body, List<ConstructorParameterSetterBuilderField> builderFields) {
        SuperConstructorInvocation superInvocation = ast.newSuperConstructorInvocation();
        builderFields.stream()
                .sorted((first, second) -> first.getIndex().compareTo(second.getIndex()))
                .forEach(constructorParameter -> addConstructorParameter(ast, builderType, superInvocation, constructorParameter));

        if (!builderFields.isEmpty()) {
            body.statements().add(superInvocation);
        }
    }

    private void populateBodyWithThisConstructorCall(AST ast, AbstractTypeDeclaration builderType, Block body, List<ThisConstructorParameterSetterBuilderField> builderFields) {
        ConstructorInvocation constructorInvocation = ast.newConstructorInvocation();
        builderFields.stream()
                .sorted((first, second) -> first.getIndex().compareTo(second.getIndex()))
                .forEach(constructorParameter -> addConstructorParameter(ast, builderType, constructorInvocation, constructorParameter));

        if (!builderFields.isEmpty()) {
            body.statements().add(constructorInvocation);
        }
    }

    private void addConstructorParameter(AST ast, AbstractTypeDeclaration builderType, SuperConstructorInvocation superInvocation,
            ConstructorParameterSetterBuilderField constructorParameter) {
        FieldAccess fieldAccess = builderFieldAccessCreatorFragment.createBuilderFieldAccess(ast, typeDeclarationToVariableNameConverter.convert(builderType),
                constructorParameter);
        superInvocation.arguments().add(fieldAccess);
    }

    private void addConstructorParameter(AST ast, AbstractTypeDeclaration builderType, ConstructorInvocation superInvocation,
            BuilderField constructorParameter) {
        FieldAccess fieldAccess = builderFieldAccessCreatorFragment.createBuilderFieldAccess(ast, typeDeclarationToVariableNameConverter.convert(builderType),
                constructorParameter);
        superInvocation.arguments().add(fieldAccess);
    }
}
