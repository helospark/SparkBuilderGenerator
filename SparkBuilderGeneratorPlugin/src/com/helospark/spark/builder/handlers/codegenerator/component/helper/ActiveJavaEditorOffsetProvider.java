package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Provides the offset of the cursor from the beginning of the document in the currently active Java editor.
 * @author helospark
 */
public class ActiveJavaEditorOffsetProvider {

    public int provideOffsetOfCurrentCursorPosition() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor instanceof JavaEditor) {
            JavaEditor javaEditor = (JavaEditor) activeEditor;
            ISelection selection = javaEditor.getSelectionProvider().getSelection();
            if (selection instanceof ITextSelection) {
                ITextSelection iTextSelection = (ITextSelection) selection;
                return iTextSelection.getOffset();
            }
        }
        return 0;
    }
}
