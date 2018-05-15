package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.builderclass;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDERS_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.preferences.PreferencesManager;
import com.helospark.spark.builder.preferences.StaticPreferences;

public class JsonPOJOBuilderAdderFragment {
    private static final String WITH_METHOD_PREFIX_DEFAULT_VALUE = "with";
    private static final String BUILD_METHOD_NAME_DEFAULT_VALUE = "build";
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
        Annotation result;

        String buildMethodName = preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN);
        String prefixName = getPrefixName();

        if (buildMethodName.equals(BUILD_METHOD_NAME_DEFAULT_VALUE) && prefixName.equals(WITH_METHOD_PREFIX_DEFAULT_VALUE)) {
            result = ast.newMarkerAnnotation();
        } else {
            NormalAnnotation ann = ast.newNormalAnnotation();

            MemberValuePair buildMethodNameAttribute = ast.newMemberValuePair();
            buildMethodNameAttribute.setName(ast.newSimpleName("buildMethodName"));
            buildMethodNameAttribute.setValue(createStringLitereal(ast, buildMethodName));

            MemberValuePair withPrefixAttribute = ast.newMemberValuePair();
            withPrefixAttribute.setName(ast.newSimpleName("withPrefix"));
            withPrefixAttribute.setValue(createStringLitereal(ast, prefixName));

            ann.values().add(buildMethodNameAttribute);
            ann.values().add(withPrefixAttribute);

            result = ann;
        }
        result.setTypeName(ast.newSimpleName(StaticPreferences.JSON_POJO_BUILDER_CLASS_NAME));

        return result;
    }

    private Expression createStringLitereal(AST ast, String literalValue) {
        StringLiteral stringLiteral = ast.newStringLiteral();
        stringLiteral.setLiteralValue(literalValue);
        return stringLiteral;
    }

    private String getPrefixName() {
        String value = preferencesManager.getPreferenceValue(BUILDERS_METHOD_NAME_PATTERN);

        Matcher result = PREFIX_PATTERN.matcher(value);

        if (result.matches()) {
            return result.group(1);
        }

        return "";
    }

}
