package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.TextEdit;

import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;

public class CodeFormatterService {
    private IProgressMonitor progressMonitor = new NullProgressMonitor();
    private CompilationUnitProvider compilationUnitProvider;

    // Stateful
    private ExecutionEvent event;

    public CodeFormatterService(CompilationUnitProvider compilationUnitProvider, ExecutionEvent event) {
        this.compilationUnitProvider = compilationUnitProvider;
        this.event = event;
    }

    public void formatCompilationUnit(SttCompilationUnit sttCompilationUnit) {
        try {
            ICompilationUnit unit = sttCompilationUnit.getRaw();
            CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
            ISourceRange range = unit.getSourceRange();
            TextEdit formatEdit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, unit.getSource(), range.getOffset(), range.getLength(), 0, null);
            unit.applyTextEdit(formatEdit, progressMonitor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to format source code", e);
        }
    }

    public void formatCurrentCompilationUnit() {
        formatCompilationUnit(compilationUnitProvider.provideCurrentICompiltionUnit(event));
    }
}
