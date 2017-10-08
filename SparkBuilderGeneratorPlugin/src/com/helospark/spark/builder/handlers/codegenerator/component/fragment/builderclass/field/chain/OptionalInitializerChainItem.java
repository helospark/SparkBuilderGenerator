package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY;
import static com.helospark.spark.builder.preferences.StaticPreferences.EMPTY_OPTIONAL_CREATOR_STATIC_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.OPTIONAL_CLASS_NAME;
import static com.helospark.spark.builder.preferences.StaticPreferences.OPTIONAL_FULLY_QUALIFIED_NAME;
import static java.util.Collections.singletonList;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.StaticMethodInvocationFragment;
import com.helospark.spark.builder.preferences.PreferencesManager;
import com.helospark.spark.builder.preferences.StaticPreferences;

public class OptionalInitializerChainItem implements FieldDeclarationPostProcessorChainItem {
    private StaticMethodInvocationFragment staticMethodInvocationFragment;
    private PreferencesManager preferencesManager;

    public OptionalInitializerChainItem(StaticMethodInvocationFragment staticMethodInvocationFragment,
            PreferencesManager preferencesManager) {
        this.staticMethodInvocationFragment = staticMethodInvocationFragment;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public boolean doesSupport(String fullyQualifiedName) {
        return fullyQualifiedName.equals(OPTIONAL_FULLY_QUALIFIED_NAME) &&
                preferencesManager.getPreferenceValue(INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY);
    }

    @Override
    public Expression createExpression(AST ast, String fullyQualifiedName) {
        return staticMethodInvocationFragment.createStaticMethodInvocation(ast, OPTIONAL_CLASS_NAME, EMPTY_OPTIONAL_CREATOR_STATIC_METHOD);
    }

    @Override
    public List<String> getImport(String fullyQualifiedName) {
        return singletonList(StaticPreferences.OPTIONAL_FULLY_QUALIFIED_NAME);
    }

}
