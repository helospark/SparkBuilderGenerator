package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;

public class StagedBuilderInterfaceTypeDefinitionCreatorFragment {
    private JavadocAdder javadocAdder;

    public StagedBuilderInterfaceTypeDefinitionCreatorFragment(JavadocAdder javadocAdder) {
        this.javadocAdder = javadocAdder;
    }

    public TypeDeclaration createStageBuilderInterface(AST ast, String interfaceName) {
        TypeDeclaration addedInterface = ast.newTypeDeclaration();
        addedInterface.setInterface(true);
        addedInterface.setName(ast.newSimpleName(interfaceName));
        addedInterface.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        javadocAdder.addJavadocForStagedInterface(ast, interfaceName, addedInterface);
        return addedInterface;
    }
}
