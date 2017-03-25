package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class WithMethodParameterCreatorFragment {
    private PreferencesManager preferencesManager;
    private MarkerAnnotationAttacher markerAnnotationAttacher;

    public WithMethodParameterCreatorFragment(PreferencesManager preferencesManager, MarkerAnnotationAttacher markerAnnotationAttacher) {
        this.preferencesManager = preferencesManager;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
    }

    public SingleVariableDeclaration createWithMethodParameter(AST ast, FieldDeclaration originalField, String fieldName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType((Type) ASTNode.copySubtree(ast, originalField.getType()));
        methodParameterDeclaration.setName(ast.newSimpleName(fieldName));
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS)) {
            markerAnnotationAttacher.attachNonNull(ast, methodParameterDeclaration);
        }
        return methodParameterDeclaration;
    }
}
