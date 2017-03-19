package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class PrivateConstructorAdderFragment {

    public void addEmptyPrivateConstructor(AST ast, TypeDeclaration builderType) {
        MethodDeclaration privateConstructorMethod = ast.newMethodDeclaration();
        privateConstructorMethod.setBody(ast.newBlock());
        privateConstructorMethod.setConstructor(true);
        privateConstructorMethod.setName(ast.newSimpleName(builderType.getName().toString()));
        privateConstructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        builderType.bodyDeclarations().add(privateConstructorMethod);
    }
}
