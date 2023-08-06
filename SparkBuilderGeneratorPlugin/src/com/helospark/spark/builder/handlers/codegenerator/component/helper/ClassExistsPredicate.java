package com.helospark.spark.builder.handlers.codegenerator.component.helper;

public class ClassExistsPredicate {

    public static boolean doesRecordTypeExists() {
        return classExists("org.eclipse.jdt.core.dom.RecordDeclaration");
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
