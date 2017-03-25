package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.ICompilationUnit;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;

/**
 * Initializes stateful beans.
 * @deprecated There should not be stateful beans
 * @author helospark
 */
@Deprecated
public class StatefulBeanHandler {
    private PreferenceStoreProvider preferenceStoreProvider;
    private WorkingCopyManagerWrapper workingCopyManagerWrapper;

    public StatefulBeanHandler(PreferenceStoreProvider preferenceStoreProvider, WorkingCopyManagerWrapper workingCopyManagerWrapper) {
        this.preferenceStoreProvider = preferenceStoreProvider;
        this.workingCopyManagerWrapper = workingCopyManagerWrapper;
    }

    public void initializeState(ExecutionEvent executionEvent) {
        ICompilationUnit iCompilationUnit = workingCopyManagerWrapper.getCurrentCompilationUnit(executionEvent);
        preferenceStoreProvider.setJavaProject(iCompilationUnit.getJavaProject());
    }

    public void clearState() {
        preferenceStoreProvider.clearState();
    }
}
