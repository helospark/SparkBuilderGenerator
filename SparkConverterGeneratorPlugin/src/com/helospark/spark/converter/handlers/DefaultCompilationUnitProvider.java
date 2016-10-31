package com.helospark.spark.converter.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Gets the compilation units to be displayed on the converter dialog.
 * 
 * @author helospark
 */
public class DefaultCompilationUnitProvider {

    public List<ICompilationUnit> getSelectedCompilationUnits(ExecutionEvent event, IWorkbenchWindow window) {
        try {
            return getCurrentlySelectedClassesInternal(event, window);
        } catch (JavaModelException e) {
            System.out.println("Exception occurred while trying to get selection. Silently ignored " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<ICompilationUnit> getCurrentlySelectedClassesInternal(ExecutionEvent event, IWorkbenchWindow window) throws JavaModelException {
        List<ICompilationUnit> result = new ArrayList<>();

        result.addAll(getSelectedClassesFromPackageExplorer(window));

        if (result.isEmpty()) {
            Optional<ICompilationUnit> currentlyOpenedClass = getCurrentlyOpenedClass(event);
            if (currentlyOpenedClass.isPresent()) {
                result.add(currentlyOpenedClass.get());
            }
        }
        return result;
    }

    private Optional<ICompilationUnit> getCurrentlyOpenedClass(ExecutionEvent event) throws JavaModelException {
        return Optional.ofNullable(getCurrentCompilationUnitOrNull(event));
    }

    private List<ICompilationUnit> getSelectedClassesFromPackageExplorer(IWorkbenchWindow window) throws JavaModelException {
        List<ICompilationUnit> compilationUnitTypes = new ArrayList<>();
        ISelectionService service = window.getActivePage().getWorkbenchWindow()
                .getSelectionService();
        IStructuredSelection structured = (IStructuredSelection) service
                .getSelection("org.eclipse.jdt.ui.PackageExplorer");
        if (structured != null) {
            for (Object selection : structured.toList()) {
                if (selection instanceof CompilationUnit) {
                    compilationUnitTypes.add((ICompilationUnit) selection);
                }
            }
        }
        return compilationUnitTypes;
    }

    private ICompilationUnit getCurrentCompilationUnitOrNull(ExecutionEvent event) {
        IEditorPart editor = HandlerUtil.getActiveEditor(event);
        String activePartId = HandlerUtil.getActivePartId(event);
        String JAVA_TYPE = "org.eclipse.jdt.ui.CompilationUnitEditor";
        if (JAVA_TYPE.equals(activePartId)) {
            IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
            return manager.getWorkingCopy(editor.getEditorInput());
        }
        return null;
    }
}
