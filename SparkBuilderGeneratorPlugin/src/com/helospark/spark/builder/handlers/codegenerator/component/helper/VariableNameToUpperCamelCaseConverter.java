package com.helospark.spark.builder.handlers.codegenerator.component.helper;

public class VariableNameToUpperCamelCaseConverter {

    public String convert(String variableName) {
        if (variableName == null || variableName.length() == 1) {
            return variableName;
        }
        return String.valueOf(Character.toUpperCase(variableName.charAt(0))) + variableName.substring(1);
    }
}
