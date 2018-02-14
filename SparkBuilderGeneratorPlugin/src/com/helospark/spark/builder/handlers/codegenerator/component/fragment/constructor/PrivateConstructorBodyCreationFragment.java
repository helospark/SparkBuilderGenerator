package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ClassFieldSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;

/**
 * Creates the body of the private constructor that initializes the class.
 * Generated code is something like:
 * <pre>
 * {
 *   super(superField, superField2);
 *   this.firstField = builder.firstField;
 *   this.secondField = builder.secondField;
 * }
 * </pre>
 * @author helospark
 */
public class PrivateConstructorBodyCreationFragment {
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private FieldSetterAdderFragment fieldSetterAdderFragment;
    private BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment;

    public PrivateConstructorBodyCreationFragment(TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter, FieldSetterAdderFragment fieldSetterAdderFragment,
            BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment) {
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.fieldSetterAdderFragment = fieldSetterAdderFragment;
        this.builderFieldAccessCreatorFragment = builderFieldAccessCreatorFragment;
    }

    public Block createBody(AST ast, TypeDeclaration builderType, List<BuilderField> builderFields) {
        Block body = ast.newBlock();
        populateBodyWithSuperConstructorCall(ast, builderType, body, getFieldsOfClass(builderFields, ConstructorParameterSetterBuilderField.class));
        fieldSetterAdderFragment.populateBodyWithFieldSetCalls(ast, typeDeclarationToVariableNameConverter.convert(builderType), body,
                getFieldsOfClass(builderFields, ClassFieldSetterBuilderField.class));
        return body;
    }

    private <T extends BuilderField> List<T> getFieldsOfClass(List<BuilderField> builderFields, Class<T> classToGet) {
        return builderFields.stream()
                .filter(field -> field.getClass().equals(classToGet))
                .map(field -> classToGet.cast(field))
                .collect(Collectors.toList());
    }

    private void populateBodyWithSuperConstructorCall(AST ast, TypeDeclaration builderType, Block body, List<ConstructorParameterSetterBuilderField> builderFields) {
        SuperConstructorInvocation superInvocation = ast.newSuperConstructorInvocation();
        builderFields.stream()
                .sorted((first, second) -> first.getIndex().compareTo(second.getIndex()))
                .forEach(constructorParameter -> addConstructorParameter(ast, builderType, superInvocation, constructorParameter));

        if (!builderFields.isEmpty()) {
            body.statements().add(superInvocation);
        }
    }

    private void addConstructorParameter(AST ast, TypeDeclaration builderType, SuperConstructorInvocation superInvocation,
            ConstructorParameterSetterBuilderField constructorParameter) {
        FieldAccess fieldAccess = builderFieldAccessCreatorFragment.createBuilderFieldAccess(ast, typeDeclarationToVariableNameConverter.convert(builderType),
                constructorParameter);
        superInvocation.arguments().add(fieldAccess);
    }
}
