package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Get fields declared by this AbstractTypeDeclaration.
 * @author helospark
 */
public class FieldExtractor {

    public static FieldDeclaration[] getFields(AbstractTypeDeclaration mainType) {
        if (mainType.getClass() == TypeDeclaration.class) {
            return ((TypeDeclaration) mainType).getFields();
        } else if (IsRecordTypePredicate.isRecordDeclaration(mainType)) {
            return RecordDeclarationWrapper.of(mainType).getFields();
        } else {
            return new FieldDeclaration[0];
        }
    }

}
