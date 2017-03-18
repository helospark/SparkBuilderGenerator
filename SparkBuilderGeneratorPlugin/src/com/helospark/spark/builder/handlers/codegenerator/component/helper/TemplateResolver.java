package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.helospark.spark.builder.handlers.exception.PluginException;

/**
 * Resolves template to actual value.
 *
 * @author helospark
 * @author stackoverflow
 */
public class TemplateResolver {

    public String resolveTemplate(String fullTemplateString, Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("\\[(.+?)\\]");
        Matcher matcher = pattern.matcher(fullTemplateString);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String templateToReplace = matcher.group(1);
            String replacement = replacements.get(templateToReplace);
            builder.append(fullTemplateString.substring(i, matcher.start()));
            if (replacement == null) {
                throw new PluginException("Illegal template '" + templateToReplace + "' in context of " + fullTemplateString + ". Valid templates: " + replacements.toString());
            } else {
                builder.append(replacement);
            }
            i = matcher.end();
        }
        builder.append(fullTemplateString.substring(i, fullTemplateString.length()));
        return builder.toString();
    }
}
