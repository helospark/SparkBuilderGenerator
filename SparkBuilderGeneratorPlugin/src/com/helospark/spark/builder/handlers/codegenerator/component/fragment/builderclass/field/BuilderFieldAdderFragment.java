package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Fragment to add private field to the builder. Field is added after the last field declaration.
 * Generated code is something like:
 * <pre>
 * private String firstField;
 * </pre>
 * @author helospark
 */
public class BuilderFieldAdderFragment {
    private FieldDeclarationPostProcessor fieldDeclarationPostProcessor;

    public BuilderFieldAdderFragment(FieldDeclarationPostProcessor fieldDeclarationPostProcessor) {
        this.fieldDeclarationPostProcessor = fieldDeclarationPostProcessor;
    }

    public void addFieldToBuilder(AST ast, TypeDeclaration builderType, BuilderField builderField) {
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(createFieldDeclarationFragment(ast, builderField));
        fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        fieldDeclaration.setType((Type) ASTNode.copySubtree(ast, builderField.getFieldType()));
        builderType.bodyDeclarations().add(findLastFieldIndex(builderType), fieldDeclaration);
    }

    private VariableDeclarationFragment createFieldDeclarationFragment(AST ast, BuilderField builderField) {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(builderField.getOriginalFieldName()));
        return fieldDeclarationPostProcessor.postProcessFragment(ast, builderField, variableDeclarationFragment);
    }

    private int findLastFieldIndex(AbstractTypeDeclaration newType) {
        return ((List<BodyDeclaration>) newType.bodyDeclarations())
                .stream()
                .filter(element -> element instanceof FieldDeclaration)
                .collect(Collectors.toList())
                .size();
    }
}
