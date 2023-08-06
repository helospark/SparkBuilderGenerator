package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

public class IsRecordTypePredicate {

    public static boolean isRecordDeclaration(AbstractTypeDeclaration AbstractTypeDeclaration) {
        return ClassExistsPredicate.doesRecordTypeExists() && AbstractTypeDeclaration.getClass().getCanonicalName().equals("org.eclipse.jdt.core.dom.RecordDeclaration");
    }

}
