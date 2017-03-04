package com.helospark.sparktemplatingplugin.execute.templater;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.helospark.sparktemplatingplugin.support.ImplicitImportList;

public class ScriptPreProcessor {
    private static final String SCRIPT_INDICATOR = "#";

    public String preprocessScript(String script) {
        StringBuilder result = new StringBuilder();
        List<String> lines = Arrays.asList(script.split("\n"));
        for (int i = 0; i < lines.size() - 1; ++i) {
            lines.set(i, lines.get(i) + "\n");
        }
        for (String implicitImport : ImplicitImportList.IMPLICIT_IMPORT_LIST) {
            result.append("import " + implicitImport + ";\n");
        }
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
        return line.replaceFirst(SCRIPT_INDICATOR, "") + "\n";
    }

    private String createTemplatedStatement(String line) {
        line = line.replaceAll("\n", "\\\\n");
        String result = StringBufferBackedTemplatingResult.SCRIPT_NAME + ".append(\"" + escapeSpecialCharacters(line) + "\");\n";
        Pattern p = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = p.matcher(result);
        while (matcher.find()) {
            String matched = matcher.group(1);
            result = matcher.replaceFirst("\" + String.valueOf(" + matched + ") + \"");
            matcher = p.matcher(result);
        }
        // result = result.replaceAll("\\$\\{", "\" + String.valueOf(");
        // result = result.replaceAll("\\}", ") + \"");
        return result;
    }

    private String escapeSpecialCharacters(String line) {
        return line.replaceAll("\"", "\\\\\"");
    }
}
