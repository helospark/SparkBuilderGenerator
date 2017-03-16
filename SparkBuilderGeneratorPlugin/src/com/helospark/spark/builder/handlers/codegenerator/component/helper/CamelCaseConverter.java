package com.helospark.spark.builder.handlers.codegenerator.component.helper;

public class CamelCaseConverter {

    public String toUpperCamelCase(String variableName) {
        if (variableName == null || variableName.length() <= 1) {
            return variableName;
        }
        return String.valueOf(Character.toUpperCase(variableName.charAt(0))) + variableName.substring(1);
    }

    public String toLowerCamelCase(String variableName) {
        if (variableName == null || variableName.length() <= 1) {
            return variableName;
        }
        return String.valueOf(Character.toLowerCase(variableName.charAt(0))) + variableName.substring(1);
    }
}
