package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Sets the given interface to the given type as a superinterface.
 * @author helospark
 */
public class InterfaceSetter {

    public void setInterface(AST ast, TypeDeclaration type, TypeDeclaration interfaceToAdd) {
        SimpleType interfaceType = ast.newSimpleType(ast.newSimpleName(interfaceToAdd.getName().getFullyQualifiedName()));
        type.superInterfaceTypes().add(interfaceType);
    }
}
