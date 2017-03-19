package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class PrivateConstructorInsertionFragment {
    public void insertMethodToFirstPlace(TypeDeclaration originalType, ListRewrite listRewrite, MethodDeclaration privateConstructor) {
        FieldDeclaration[] fields = originalType.getFields();
        if (fields == null || fields.length == 0) {
            listRewrite.insertFirst(privateConstructor, null);
        } else {
            listRewrite.insertAfter(privateConstructor, fields[fields.length - 1], null);
        }
    }
}
