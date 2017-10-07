package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

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

    public void addFieldToBuilder(AST ast, TypeDeclaration builderType, NamedVariableDeclarationField namedVariableDeclarationField) {
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(createFragment(ast, namedVariableDeclarationField));
        fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        fieldDeclaration.setType((Type) ASTNode.copySubtree(ast, namedVariableDeclarationField.getFieldDeclaration().getType()));
        builderType.bodyDeclarations().add(findLastFieldIndex(builderType), fieldDeclaration);
    }

    private VariableDeclarationFragment createFragment(AST ast, NamedVariableDeclarationField namedVariableDeclarationField) {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(namedVariableDeclarationField.getOriginalFieldName()));
        return fieldDeclarationPostProcessor.postProcess(ast, namedVariableDeclarationField.getFieldDeclaration(), variableDeclarationFragment);
    }

    private int findLastFieldIndex(TypeDeclaration newType) {
        return ((List<BodyDeclaration>) newType.bodyDeclarations())
                .stream()
                .filter(element -> element instanceof FieldDeclaration)
                .collect(Collectors.toList())
                .size();
    }
}
