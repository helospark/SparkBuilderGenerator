package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Add imports.
 * 
 * @author helospark
 */
public class ImportPopulator {
    private PreferencesManager preferencesManager;

    public ImportPopulator(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public void populateImports(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        ListRewrite importRewrite = rewriter.getListRewrite(compilationUnit, CompilationUnit.IMPORTS_PROPERTY);

        if (shouldAddNonnullAnnotation()) {
            ImportDeclaration importDeclaration = ast.newImportDeclaration();
            importDeclaration.setName(ast.newName("javax.annotation.Nonnull"));
            importRewrite.insertLast(importDeclaration, null);
        }
        if (shouldAddGeneratedAnnotation()) {
            ImportDeclaration importDeclaration = ast.newImportDeclaration();
            importDeclaration.setName(ast.newName("javax.annotation.Generated"));
            importRewrite.insertLast(importDeclaration, null);
        }
    }

    private boolean shouldAddNonnullAnnotation() {
        return preferencesManager.getPreferenceValue(ADD_NONNULL_ON_PARAMETERS)
                || preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN);
    }

    private boolean shouldAddGeneratedAnnotation() {
        return preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION);
    }
}
