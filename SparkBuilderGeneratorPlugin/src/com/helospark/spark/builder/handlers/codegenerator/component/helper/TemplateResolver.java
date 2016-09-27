package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves template to actual value.
 * 
 * @author helospark
 * @author stackoverflow
 */
public class TemplateResolver {

    public String resolveTemplate(String template, Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("\\[(.+?)\\]");
        Matcher matcher = pattern.matcher(template);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(template.substring(i, matcher.start()));
            if (replacement == null)
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(template.substring(i, template.length()));
        return builder.toString();
    }
}
