package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This method of extracting local variable is a HACK, but should be good
 * enough for most purposes.
 * Better solution would be to parse the AST of the script language and extract
 * these datas from there
 */
public class RegexMatchingLocalVariableExtractor {
    private static final String[] REGEXES = {
            "for \\((\\w+?)\\s+(\\w+?).*", // for loop variable
            "(\\w+?)\\s+(\\w+?)\\s*=.*", // local variable with assignment
            "(\\w+?)\\s+(\\w+?)\\s*;.*", // local variable without assignment
    };

    public Map<String, String> matchLocalVariableWithRegex(String source) {
        Map<String, String> result = new HashMap<>();
        for (String regex : REGEXES) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                String type = matcher.group(1);
                String name = matcher.group(2);
                result.put(name, type);
            }
        }
        return result;
    }
}
