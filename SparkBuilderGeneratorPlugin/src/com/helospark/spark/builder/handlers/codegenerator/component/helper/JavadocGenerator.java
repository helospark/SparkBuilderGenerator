package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;

/**
 * Generates Javadoc from template.
 * 
 * @author helospark
 */
public class JavadocGenerator {

    @SuppressWarnings("unchecked")
    public Javadoc generateJavadoc(AST ast, String mainTemplate, Map<String, String> tags) {
        Javadoc javadoc = ast.newJavadoc();
        for (String line : mainTemplate.split("\n")) {
            TextElement textElement = ast.newTextElement();
            textElement.setText(line);
            TagElement tagElement = ast.newTagElement();
            tagElement.fragments().add(textElement);
            javadoc.tags().add(tagElement);
        }

        for (Map.Entry<String, String> entity : tags.entrySet()) {
            TextElement tagTextElement = ast.newTextElement();
            tagTextElement.setText(entity.getValue());

            TagElement tagElement = ast.newTagElement();
            tagElement.setTagName(entity.getKey());
            tagElement.fragments().add(tagTextElement);
            javadoc.tags().add(tagElement);
        }
        return javadoc;
    }
}
