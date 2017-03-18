package com.helospark.spark.builder.handlers.codegenerator.component.fragment;

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

public class BuilderFieldAdderFragment {

    public void addFieldToBuilder(AST ast, TypeDeclaration builderType, NamedVariableDeclarationField namedVariableDeclarationField) {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(ast.newSimpleName(namedVariableDeclarationField.getOriginalFieldName()));
        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        fieldDeclaration.setType((Type) ASTNode.copySubtree(ast, namedVariableDeclarationField.getFieldDeclaration().getType()));
        builderType.bodyDeclarations().add(findLastFieldIndex(builderType), fieldDeclaration);
    }

    private int findLastFieldIndex(TypeDeclaration newType) {
        return ((List<BodyDeclaration>) newType.bodyDeclarations())
                .stream()
                .filter(element -> element instanceof FieldDeclaration)
                .collect(Collectors.toList())
                .size();
    }
}
