package com.helospark.spark.builder.handlers.codegenerator;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class ActiveJavaEditorOffsetProvider {

    public int provideOffsetAtCurrentCursorPosition() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor instanceof JavaEditor) {
            JavaEditor javaEditor = (JavaEditor) activeEditor;
            ITextSelection sel = (ITextSelection) javaEditor.getSelectionProvider().getSelection();
            return sel.getOffset();
        }
        return 0;
    }
}
