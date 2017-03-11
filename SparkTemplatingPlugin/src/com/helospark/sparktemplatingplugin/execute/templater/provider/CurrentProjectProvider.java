package com.helospark.sparktemplatingplugin.execute.templater.provider;

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

import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;

public class CurrentProjectProvider implements ScriptExposedProvider {

    public static final String EXPOSED_NAME = "currentProject";
    private CompilationUnitProvider compilationUnitProvider;

    public CurrentProjectProvider(CompilationUnitProvider compilationUnitProvider) {
        this.compilationUnitProvider = compilationUnitProvider;
    }

    public IProject provideCurrentProject(ExecutionEvent executionEvent) {
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
        } else {
            SttCompilationUnit compilationUnit = (SttCompilationUnit) compilationUnitProvider.provide(executionEvent);
            project = compilationUnit.getRaw().getJavaProject().getProject();
        }
        return project;
    }

    @Override
    public Object provide(ExecutionEvent executionEvent) {
        return provideCurrentProject(executionEvent);
    }

    @Override
    public Class<?> getExposedObjectType() {
        return IProject.class;
    }

    @Override
    public String getExposedName() {
        return EXPOSED_NAME;
    }
}
