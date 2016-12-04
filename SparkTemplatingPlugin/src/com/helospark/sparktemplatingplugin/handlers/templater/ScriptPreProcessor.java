package com.helospark.sparktemplatingplugin.handlers.templater;

import java.util.Arrays;
import java.util.List;

public class ScriptPreProcessor {
    private static final String SCRIPT_INDICATOR = "#";

    public String preprocessScript(String script) {
        StringBuilder result = new StringBuilder();
        List<String> lines = Arrays.asList(script.split("\n"));
        for (String line : lines) {
            if (isLineScript(line)) {
                result.append(processScriptLine(line));
            } else {
                result.append(createTemplatedStatement(line));
            }
        }
        return result.toString();
    }

    private boolean isLineScript(String line) {
        return line.trim().startsWith(SCRIPT_INDICATOR);
    }

    private String processScriptLine(String line) {
        String removeScriptIndicator = line.replaceFirst(SCRIPT_INDICATOR, "");
        return removeScriptIndicator + "\n";
    }

    private String createTemplatedStatement(String line) {
        return TemplatingResult.SCRIPT_NAME + ".append(\"" + escapeSpecialCharacters(line) + "\\n" + "\");\n";
    }

    private String escapeSpecialCharacters(String line) {
        return line.replaceAll("\"", "\\\\\"");
    }
}
