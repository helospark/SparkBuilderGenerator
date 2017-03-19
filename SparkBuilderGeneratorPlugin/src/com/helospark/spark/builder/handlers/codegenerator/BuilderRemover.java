package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.OVERRIDE_PREVIOUS_BUILDER;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.ErrorHandlerHook;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderAstRemover;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Removes the builder if exists.
 *
 * @author helospark
 */
public class BuilderRemover {
    private PreferencesManager preferencesManager;
    private ErrorHandlerHook errorHandlerHook;
    private BuilderAstRemover builderAstRemover;

    public BuilderRemover(PreferencesManager preferencesManager, ErrorHandlerHook errorHandlerHook, BuilderAstRemover builderAstRemover) {
        this.preferencesManager = preferencesManager;
        this.errorHandlerHook = errorHandlerHook;
        this.builderAstRemover = builderAstRemover;
    }

    @SuppressWarnings("unchecked")
    public void removeExistingBuilderWhenNeeded(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        if (preferencesManager.getPreferenceValue(OVERRIDE_PREVIOUS_BUILDER)) {
            try {
                builderAstRemover.removeBuilder(rewriter, compilationUnit);
            } catch (RuntimeException e) {
                errorHandlerHook.onPreviousBuilderRemoveFailure(e);
            }
        }
    }

}
