package com.helospark.sparktemplatingplugin.ui.dialog;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class TemplateBrowseDialogListLabelProvider implements ILabelProvider {

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void addListener(ILabelProviderListener listener) {

    }

    @Override
    public String getText(Object element) {
        if (element instanceof ScriptEntity) {
            return ((ScriptEntity) element).getCommandName();
        }
        return "#ERROR#";
    }

    @Override
    public Image getImage(Object element) {
        return null;
    }
}
