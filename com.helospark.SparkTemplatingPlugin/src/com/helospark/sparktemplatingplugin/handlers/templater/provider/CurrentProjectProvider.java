package com.helospark.sparktemplatingplugin.handlers.templater.provider;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import com.helospark.sparktemplatingplugin.handlers.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.handlers.templater.domain.ScriptExposedPair;

public class CurrentProjectProvider implements ScriptExposedProvider {

    public IProject provideCurrentProject() {
        ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();

        ISelection selection = selectionService.getSelection();

        IProject project = null;
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();

            if (element instanceof IResource) {
                project = ((IResource) element).getProject();
            } else if (element instanceof PackageFragmentRootContainer) {
                IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
                project = jProject.getProject();
            } else if (element instanceof IJavaElement) {
                IJavaProject jProject = ((IJavaElement) element).getJavaProject();
                project = jProject.getProject();
            }
        }
        return project;
    }

    @Override
    public ScriptExposedPair provide(ExecutionEvent executionEvent) {
        return new ScriptExposedPair("currentProject", provideCurrentProject());
    }
}
