package com.helospark.spark.builder.handlers;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.OVERRIDE_PREVIOUS_BUILDER;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.spark.builder.Activator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderPatternCodeGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
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

    /**
     * Fake dependency injection constructor.
     */
    public GenerateBuilderHandler() {
        this(Activator.getDependency(CompilationUnitParser.class),
                Activator.getDependency(BuilderPatternCodeGenerator.class),
                Activator.getDependency(BuilderRemover.class),
                Activator.getDependency(PreferencesManager.class));
    }

    public GenerateBuilderHandler(CompilationUnitParser compilationUnitParser, BuilderPatternCodeGenerator builderGenerator, BuilderRemover builderRemover,
            PreferencesManager preferencesManager) {
        this.compilationUnitParser = compilationUnitParser;
        this.builderGenerator = builderGenerator;
        this.builderRemover = builderRemover;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editor = HandlerUtil.getActiveEditor(event);

        String activePartId = HandlerUtil.getActivePartId(event);
        if (JAVA_TYPE.equals(activePartId)) {
            IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
            ICompilationUnit compilationUnit = manager.getWorkingCopy(editor.getEditorInput());
            addBuilder(compilationUnit);
        }
        return null;
    }

    private void addBuilder(ICompilationUnit iCompilationUnit) {
        try {
            CompilationUnit compilationUnit = compilationUnitParser.parse(iCompilationUnit);
            AST ast = compilationUnit.getAST();
            ASTRewrite rewriter = ASTRewrite.create(ast);

            if (preferencesManager.getPreferenceValue(OVERRIDE_PREVIOUS_BUILDER)) {
                builderRemover.removeExistingBuilder(ast, rewriter, compilationUnit);
            }
            builderGenerator.generateBuilder(ast, rewriter, compilationUnit);

            commitCodeChanges(iCompilationUnit, rewriter);
        } catch (CoreException | MalformedTreeException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void commitCodeChanges(ICompilationUnit iCompilationUnit, ASTRewrite rewriter)
            throws JavaModelException, BadLocationException {
        Document document = new Document(iCompilationUnit.getSource());
        TextEdit edits = rewriter.rewriteAST(document, null);
        edits.apply(document);
        iCompilationUnit.getBuffer().setContents(document.get());
    }

}
