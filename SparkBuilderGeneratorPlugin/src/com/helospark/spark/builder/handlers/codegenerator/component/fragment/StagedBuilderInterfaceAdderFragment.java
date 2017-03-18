package com.helospark.spark.builder.handlers.codegenerator.component.fragment;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderInterfaceAdderFragment {
    private CamelCaseConverter camelCaseConverter;
    private StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment;

    public StagedBuilderInterfaceAdderFragment(CamelCaseConverter camelCaseConverter) {
        this.camelCaseConverter = camelCaseConverter;
    }

    public void createAndAddInterfaceFor(AST ast, TypeDeclaration builderType, NamedVariableDeclarationField namedVariableDeclarationField) {
        TypeDeclaration addedInterface = ast.newTypeDeclaration();
        addedInterface.setInterface(true);
        addedInterface.setName(ast.newSimpleName(composeNameFrom(namedVariableDeclarationField)));
        addedInterface.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        MethodDeclaration withMethodDeclaration = stagedBuilderMethodDefiniationCreatorFragment.createNewWithMethod(ast, namedVariableDeclarationField);
        addedInterface.bodyDeclarations().add(withMethodDeclaration);

        builderType.superInterfaceTypes().add(addedInterface);
    }

    private String composeNameFrom(NamedVariableDeclarationField namedVariableDeclarationField) {
        return "I" + camelCaseConverter.toUpperCamelCase(namedVariableDeclarationField.getOriginalFieldName()) + "Stage";
    }

}
