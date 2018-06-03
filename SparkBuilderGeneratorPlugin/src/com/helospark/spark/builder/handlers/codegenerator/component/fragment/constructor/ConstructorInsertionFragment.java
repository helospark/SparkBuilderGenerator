package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Insert constructor after fields, but before methods.
 * @author helospark
 */
public class ConstructorInsertionFragment {
    public void insertMethodToFirstPlace(TypeDeclaration originalType, ListRewrite listRewrite, MethodDeclaration constructor) {
        FieldDeclaration[] fields = originalType.getFields();
        if (fields == null || fields.length == 0) {
            listRewrite.insertFirst(constructor, null);
        } else {
            listRewrite.insertAfter(constructor, fields[fields.length - 1], null);
        }
    }
}
