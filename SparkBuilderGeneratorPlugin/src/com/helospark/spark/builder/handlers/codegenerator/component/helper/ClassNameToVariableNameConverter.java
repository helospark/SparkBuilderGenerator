package com.helospark.spark.builder.handlers.codegenerator.component.helper;

public class ClassNameToVariableNameConverter {

    public String convert(String className) {
        if (className == null || className.length() <= 1) {
            return className;
        }
        return String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1);
    }
}
