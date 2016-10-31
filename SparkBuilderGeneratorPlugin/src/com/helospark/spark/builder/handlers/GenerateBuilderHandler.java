package com.helospark.spark.builder.handlers;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.OVERRIDE_PREVIOUS_BUILDER;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.codegenerator.BuilderPatternCodeGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.exception.PluginException;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Handler for generating a builder.
 * 
 * @author helospark
 */
public class GenerateBuilderHandler extends AbstractHandler {
    private static final String JAVA_TYPE = "org.eclipse.jdt.ui.CompilationUnitEditor";
    private CompilationUnitParser compilationUnitParser;
    private BuilderPatternCodeGenerator builderGenerator;
    private BuilderRemover builderRemover;
    private PreferencesManager preferencesManager;
    private HandlerUtilWrapper handlerUtilWrapper;
    private WorkingCopyManagerWrapper workingCopyManagerWrapper;
    private CompilationUnitSourceSetter compilationUnitSourceSetter;
    private ErrorHandlerHook errorHandlerHook;

    /**
     * Fake dependency injection constructor.
     */
    public GenerateBuilderHandler() {
        this(DiContainer.getDependency(CompilationUnitParser.class),
                DiContainer.getDependency(BuilderPatternCodeGenerator.class),
                DiContainer.getDependency(BuilderRemover.class),
                DiContainer.getDependency(PreferencesManager.class),
                DiContainer.getDependency(ErrorHandlerHook.class),
                DiContainer.getDependency(HandlerUtilWrapper.class),
                DiContainer.getDependency(WorkingCopyManagerWrapper.class),
                DiContainer.getDependency(CompilationUnitSourceSetter.class));
    }

    public GenerateBuilderHandler(CompilationUnitParser compilationUnitParser, BuilderPatternCodeGenerator builderGenerator, BuilderRemover builderRemover,
            PreferencesManager preferencesManager, ErrorHandlerHook errorHandlerHook, HandlerUtilWrapper handlerUtilWrapper,
            WorkingCopyManagerWrapper workingCopyManagerWrapper, CompilationUnitSourceSetter compilationUnitSourceSetter) {
        this.compilationUnitParser = compilationUnitParser;
        this.builderGenerator = builderGenerator;
        this.builderRemover = builderRemover;
        this.preferencesManager = preferencesManager;
        this.handlerUtilWrapper = handlerUtilWrapper;
        this.workingCopyManagerWrapper = workingCopyManagerWrapper;
        this.compilationUnitSourceSetter = compilationUnitSourceSetter;
        this.errorHandlerHook = errorHandlerHook;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (isCurrentPartJava(event)) {
            ICompilationUnit iCompilationUnit = workingCopyManagerWrapper.getCurrentCompilationUnit(event);
            addBuilder(iCompilationUnit);
        }
        return null;
    }

    private boolean isCurrentPartJava(ExecutionEvent event) {
        String activePartId = handlerUtilWrapper.getActivePartId(event);
        return JAVA_TYPE.equals(activePartId);
    }

    private void addBuilder(ICompilationUnit iCompilationUnit) {
        try {
            CompilationUnit compilationUnit = compilationUnitParser.parse(iCompilationUnit);
            AST ast = compilationUnit.getAST();
            ASTRewrite rewriter = ASTRewrite.create(ast);

            if (preferencesManager.getPreferenceValue(OVERRIDE_PREVIOUS_BUILDER)) {
                try {
                    builderRemover.removeExistingBuilder(ast, rewriter, compilationUnit);
                } catch (RuntimeException e) {
                    errorHandlerHook.onPreviousBuilderRemoveFailure(e);
                }
            }
            builderGenerator.generateBuilder(ast, rewriter, compilationUnit);

            commitCodeChanges(iCompilationUnit, rewriter);
        } catch (PluginException e) {
            errorHandlerHook.onPluginException(e);
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // catch (Exception e) {
        // errorHandlerHook.onUnexpectedException(e);
        // }
    }

    private void commitCodeChanges(ICompilationUnit iCompilationUnit, ASTRewrite rewriter)
            throws JavaModelException, BadLocationException {
        Document document = new Document(iCompilationUnit.getSource());
        TextEdit edits = rewriter.rewriteAST(document, null);
        edits.apply(document);
        compilationUnitSourceSetter.setCompilationUnitSource(iCompilationUnit, document.get());
    }

}
