package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldExtractor;

/**
 * Insert constructor after fields, but before methods.
 * @author helospark
 */
public class ConstructorInsertionFragment {
    public void insertMethodToFirstPlace(AbstractTypeDeclaration originalType, ListRewrite listRewrite, MethodDeclaration constructor) {
        FieldDeclaration[] fields = FieldExtractor.getFields(originalType);
        if (fields == null || fields.length == 0) {
            listRewrite.insertFirst(constructor, null);
        } else {
            listRewrite.insertAfter(constructor, fields[fields.length - 1], null);
        }
    }
}
