package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Checks if the given AbstractTypeDeclaration is a RecordDeclaration.
 * @author helospark
 */
public class IsRecordTypePredicate {
    private static final String RECORD_DECLARATION_FQN = "org.eclipse.jdt.core.dom.RecordDeclaration";

    public static boolean isRecordDeclaration(AbstractTypeDeclaration abstractTypeDeclaration) {
        return abstractTypeDeclaration.getClass().getCanonicalName().equals(RECORD_DECLARATION_FQN);
    }

}
