package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.builderclass;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDERS_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.StaticPreferences.JSON_POJO_BUILDER_CLASS_NAME;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.preferences.PreferencesManager;
import com.helospark.spark.builder.preferences.StaticPreferences;

/**
 * Adds {@literal @}JsonPOJOBuilder annotation to the given builder type.
 * @author helospark
 */
public class JsonPOJOBuilderAdderFragment {
    private static final String DEFAULT_WITH_METHOD_ATTRIBUTE_VALUE = "with";
    private static final String DEFAULT_BUILDER_METHOD_NAME_ATTRIBUTE_VALUE = "build";
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(.*?)\\[.*");
    private PreferencesManager preferencesManager;
    private ImportRepository importRepository;

    public JsonPOJOBuilderAdderFragment(PreferencesManager preferencesManager, ImportRepository importRepository) {
        this.preferencesManager = preferencesManager;
        this.importRepository = importRepository;
    }

    public void addJsonPOJOBuilder(AST ast, TypeDeclaration builderType) {
        importRepository.addImport(StaticPreferences.JSON_POJO_BUILDER_FULLY_QUALIFIED_NAME);
        Annotation annotationToAdd = createJsonPojoBuilderAnnotation(ast);
        builderType.modifiers().add(0, annotationToAdd);
    }

    private Annotation createJsonPojoBuilderAnnotation(AST ast) {
        String buildMethodName = getBuilderMethodName();
        String withMethodPrefix = getWithMethodPrefix();

        Annotation result;
        if (buildMethodName.equals(DEFAULT_BUILDER_METHOD_NAME_ATTRIBUTE_VALUE) && withMethodPrefix.equals(DEFAULT_WITH_METHOD_ATTRIBUTE_VALUE)) {
            result = createEmptyJsonPojoBuilderAnnotation(ast);
        } else {
            result = createJsonPojoBuilderAnnotationWithAttributes(ast, buildMethodName, withMethodPrefix);
        }
        result.setTypeName(ast.newSimpleName(JSON_POJO_BUILDER_CLASS_NAME));

        return result;
    }

    private String getBuilderMethodName() {
        return preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN);
    }

    private String getWithMethodPrefix() {
        String builderMethodNamePattern = preferencesManager.getPreferenceValue(BUILDERS_METHOD_NAME_PATTERN);

        Matcher matcher = PREFIX_PATTERN.matcher(builderMethodNamePattern);

        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private MarkerAnnotation createEmptyJsonPojoBuilderAnnotation(AST ast) {
        return ast.newMarkerAnnotation();
    }

    private NormalAnnotation createJsonPojoBuilderAnnotationWithAttributes(AST ast, String buildMethodName, String withMethodPrefix) {
        NormalAnnotation annotation = ast.newNormalAnnotation();

        annotation.values().add(createAnnotationAttribute(ast, "buildMethodName", buildMethodName));
        annotation.values().add(createAnnotationAttribute(ast, "withPrefix", withMethodPrefix));

        return annotation;
    }

    private MemberValuePair createAnnotationAttribute(AST ast, String attributeName, String attributeValue) {
        MemberValuePair buildMethodNameAttribute = ast.newMemberValuePair();
        buildMethodNameAttribute.setName(ast.newSimpleName(attributeName));
        buildMethodNameAttribute.setValue(createStringLitereal(ast, attributeValue));
        return buildMethodNameAttribute;
    }

    private Expression createStringLitereal(AST ast, String literalValue) {
        StringLiteral stringLiteral = ast.newStringLiteral();
        stringLiteral.setLiteralValue(literalValue);
        return stringLiteral;
    }

}
