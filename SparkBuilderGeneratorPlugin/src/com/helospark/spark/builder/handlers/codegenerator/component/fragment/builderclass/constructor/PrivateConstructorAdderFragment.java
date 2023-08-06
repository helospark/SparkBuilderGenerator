package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Fragment to add empty private constructor to the builder.
 * Generated code is something like:
 * <pre>
 * private Clazz() {
 * }
 * </pre>
 * @author helospark
 */
public class PrivateConstructorAdderFragment {

    public void addEmptyPrivateConstructor(AST ast, AbstractTypeDeclaration builderType) {
        MethodDeclaration privateConstructorMethod = ast.newMethodDeclaration();
        privateConstructorMethod.setBody(ast.newBlock());
        privateConstructorMethod.setConstructor(true);
        privateConstructorMethod.setName(ast.newSimpleName(builderType.getName().toString()));
        privateConstructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        builderType.bodyDeclarations().add(privateConstructorMethod);
    }
}
