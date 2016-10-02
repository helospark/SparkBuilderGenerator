package com.helospark.spark.converter.handlers.dialog.uicomponent;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Button;

public class JavaProjectSelectorStrategy {
    private ClassSelector source;
    private ClassSelector destination;

    private Button sourceProjectButton;
    private Button destionationProjectButton;

    public JavaProjectSelectorStrategy(ClassSelector source, ClassSelector destination, Button sourceProjectButton, Button destionationProjectButton) {
        this.source = source;
        this.destination = destination;
        this.sourceProjectButton = sourceProjectButton;
        this.destionationProjectButton = destionationProjectButton;
    }

    public IJavaProject selectProject() {
        return sourceProjectButton.getSelection() ? source.getJavaProject() : destination.getJavaProject();
    }
}
