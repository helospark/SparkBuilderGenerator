package com.helospark.sparktemplatingplugin.ui.editor.cache;

import com.helospark.sparktemplatingplugin.support.ImplicitImportList;
import com.helospark.sparktemplatingplugin.support.classpath.ClassInClasspathLocator;
import com.helospark.sparktemplatingplugin.support.classpath.ClasspathScanCacheInitializationJob;

public class EditorCacheInitializer {
	ClassInClasspathLocator classInClasspathLocator;

	public EditorCacheInitializer(ClassInClasspathLocator classInClasspathLocator) {
		this.classInClasspathLocator = classInClasspathLocator;
	}

	public void initializeCaches() {
		initializeContentAssistCaches();
	}

	private void initializeContentAssistCaches() {
		ClasspathScanCacheInitializationJob classpathScanCacheInitializationJob = new ClasspathScanCacheInitializationJob(classInClasspathLocator,
				ImplicitImportList.IMPLICIT_IMPORT_LIST);
		classpathScanCacheInitializationJob.schedule();
	}
}
