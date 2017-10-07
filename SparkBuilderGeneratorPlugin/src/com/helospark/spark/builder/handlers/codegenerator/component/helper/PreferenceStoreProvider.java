package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Optional;

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;

import com.helospark.spark.builder.Activator;
import com.helospark.spark.builder.handlers.StatefulBean;

/**
 * Provides this plugin's necessary preference stores.
 *
 * @author helospark
 */
public class PreferenceStoreProvider implements StatefulBean {
    // TODO: stateful beans are evil. This state should be moved out and recieved via the provide method
    private Optional<IJavaProject> currentJavaProject = Optional.empty();

    public PreferenceStoreWrapper providePreferenceStore() {
        IPreferenceStore[] preferenceStores = new IPreferenceStore[2];
        preferenceStores[0] = Activator.getDefault().getPreferenceStore();

        ScopedPreferenceStore jdtCorePreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.jdt.core");
        if (currentJavaProject.isPresent()) { // if we are in a project add to search scope
            jdtCorePreferenceStore.setSearchContexts(new IScopeContext[] { new ProjectScope(currentJavaProject.get().getProject()), InstanceScope.INSTANCE });
        }
        preferenceStores[1] = jdtCorePreferenceStore;

        return new PreferenceStoreWrapper(new ChainedPreferenceStore(preferenceStores));
    }

    public void setJavaProject(IJavaProject javaProject) {
        currentJavaProject = Optional.ofNullable(javaProject);
    }

    public void clearState() {
        currentJavaProject = Optional.empty();
    }
}
