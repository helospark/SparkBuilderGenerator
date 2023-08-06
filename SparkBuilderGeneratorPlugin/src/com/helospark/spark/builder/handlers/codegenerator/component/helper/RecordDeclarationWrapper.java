package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Wraps RecordDeclaration, because I don't know how to handle optional API in OSGI.
 * 
 * TODO: find out how to handle OSGI optional API
 * @author helospark
 */
public class RecordDeclarationWrapper {
    AbstractTypeDeclaration abstractTypeDeclaration;

    public RecordDeclarationWrapper(AbstractTypeDeclaration abstractTypeDeclaration) {
        if (!IsRecordTypePredicate.isRecordDeclaration(abstractTypeDeclaration)) {
            throw new IllegalArgumentException("Wrong type");
        }
        this.abstractTypeDeclaration = abstractTypeDeclaration;
    }

    public MethodDeclaration[] getMethods() {
        return invokeMethod("getMethods", MethodDeclaration[].class);
    }

    public FieldDeclaration[] getFields() {
        return invokeMethod("getFields", FieldDeclaration[].class);
    }

    public List superInterfaceTypes() {
        return invokeMethod("superInterfaceTypes", List.class);
    }

    public List recordComponents() {
        return invokeMethod("recordComponents", List.class);
    }

    public ChildListPropertyDescriptor getBodyDeclarationProperty() {
        return getStaticField("BODY_DECLARATIONS_PROPERTY", ChildListPropertyDescriptor.class);
    }

    public ChildListPropertyDescriptor getModifiers2Property() {
        return getStaticField("MODIFIERS2_PROPERTY", ChildListPropertyDescriptor.class);
    }

    private <T> T invokeMethod(String name, Class<T> resultType) {
        try {
            return (T) abstractTypeDeclaration.getClass().getMethod(name).invoke(abstractTypeDeclaration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T getStaticField(String name, Class<T> type) {
        try {
            return (T) abstractTypeDeclaration.getClass().getField(name).get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RecordDeclarationWrapper of(AbstractTypeDeclaration typeDeclaration) {
        return new RecordDeclarationWrapper(typeDeclaration);
    }

}
