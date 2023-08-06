package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Get methods declared by this AbstractTypeDeclaration.
 * @author helospark
 */
public class MethodExtractor {

    public static MethodDeclaration[] getMethods(AbstractTypeDeclaration mainType) {
        if (mainType.getClass() == TypeDeclaration.class) {
            return ((TypeDeclaration) mainType).getMethods();
        } else if (IsRecordTypePredicate.isRecordDeclaration(mainType)) {
            return RecordDeclarationWrapper.of(mainType).getMethods();
        } else {
            return new MethodDeclaration[0];
        }
    }

}
