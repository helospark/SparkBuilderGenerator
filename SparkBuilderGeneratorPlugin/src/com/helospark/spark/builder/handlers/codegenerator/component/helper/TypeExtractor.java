package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeExtractor {

    public static TypeDeclaration[] getTypes(AbstractTypeDeclaration mainType) {
        if (mainType.getClass() == TypeDeclaration.class) {
            return ((TypeDeclaration) mainType).getTypes();
        } else {
            return new TypeDeclaration[0];
        }
    }

}
