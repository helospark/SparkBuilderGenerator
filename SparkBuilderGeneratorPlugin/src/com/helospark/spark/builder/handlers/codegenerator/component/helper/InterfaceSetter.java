package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Sets the given interface to the given type as a superinterface.
 * @author helospark
 */
public class InterfaceSetter {

    public void setInterface(AST ast, AbstractTypeDeclaration type, AbstractTypeDeclaration interfaceToAdd) {
        SimpleType interfaceType = ast.newSimpleType(ast.newSimpleName(interfaceToAdd.getName().getFullyQualifiedName()));

        if (type.getClass() == TypeDeclaration.class) {
            ((TypeDeclaration) type).superInterfaceTypes().add(interfaceType);
        } else if (IsRecordTypePredicate.isRecordDeclaration(type)) {
            RecordDeclarationWrapper.of(type).superInterfaceTypes().add(interfaceType);
        }
    }
}
