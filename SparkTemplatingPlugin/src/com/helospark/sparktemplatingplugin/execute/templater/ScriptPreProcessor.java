package com.helospark.sparktemplatingplugin.execute.templater;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String result = TemplatingResult.SCRIPT_NAME + ".append(\"" + escapeSpecialCharacters(line) + "\\n" + "\");\n";
        Pattern p = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = p.matcher(result);
        if (matcher.find()) {
            String matched = matcher.group(1);
            result = matcher.replaceAll("\" + String.valueOf(" + matched + ") + \"");
        }
        // result = result.replaceAll("\\$\\{", "\" + String.valueOf(");
        // result = result.replaceAll("\\}", ") + \"");
        return result;
    }

    private String escapeSpecialCharacters(String line) {
        return line.replaceAll("\"", "\\\\\"");
    }
}
