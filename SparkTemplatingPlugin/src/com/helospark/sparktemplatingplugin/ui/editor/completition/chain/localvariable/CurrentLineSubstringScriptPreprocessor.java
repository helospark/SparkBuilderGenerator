package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable;

public class CurrentLineSubstringScriptPreprocessor {

    public String process(String input, int currentPosition) {
        int previousLineIndex = input.lastIndexOf("\n", currentPosition);
        if (previousLineIndex == -1) {
            return "";
        } else {
            return input.substring(0, previousLineIndex);
        }
    }
}
