package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Fragment to create the build() method's declaration.
 * Generated code is something like:
 * <pre>
 * public Clazz build();
 * </pre>
 * @author helospark
 */
public class BuildMethodDeclarationCreatorFragment {
    private static final String CLASS_NAME_REPLACEMENT_PATTERN = "className";
    private PreferencesManager preferencesManager;
    private MarkerAnnotationAttacher markerAnnotationAttacher;
    private TemplateResolver templateResolver;

    public BuildMethodDeclarationCreatorFragment(PreferencesManager preferencesManager,
            MarkerAnnotationAttacher markerAnnotationAttacher,
            TemplateResolver templateResolver) {
        this.preferencesManager = preferencesManager;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
        this.templateResolver = templateResolver;
    }

    public MethodDeclaration createMethod(AST ast, TypeDeclaration originalType) {
        MethodDeclaration method = ast.newMethodDeclaration();
        method.setName(ast.newSimpleName(getBuildMethodName(originalType)));
        method.setReturnType2(ast.newSimpleType(ast.newName(originalType.getName().toString())));

        if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
            markerAnnotationAttacher.attachNonNull(ast, method);
        }

        method.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        return method;
    }

    private String getBuildMethodName(TypeDeclaration originalType) {
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put(CLASS_NAME_REPLACEMENT_PATTERN, originalType.getName().toString());
        return templateResolver.resolveTemplate(preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN),
                replacementMap);
    }
}
