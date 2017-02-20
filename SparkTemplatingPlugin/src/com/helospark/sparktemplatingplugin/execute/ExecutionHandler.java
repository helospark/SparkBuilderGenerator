package com.helospark.sparktemplatingplugin.execute;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.execute.templater.StatefulBean;
import com.helospark.sparktemplatingplugin.execute.templater.Templater;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;
import com.helospark.sparktemplatingplugin.ui.dialog.TemplateBrowseDialog;

public class ExecutionHandler extends AbstractHandler {
    private ScriptRepository scriptRepository;
    private Templater templater;
    private List<StatefulBean> statefulBeans;

    public ExecutionHandler() {
        this.scriptRepository = DiContainer.getDependency(ScriptRepository.class);
        this.templater = DiContainer.getDependency(Templater.class);
        this.statefulBeans = DiContainer.getDependencyList(StatefulBean.class);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            TemplateBrowseDialog templateBrowseDialog = new TemplateBrowseDialog(shell, scriptRepository, "Select an template to run");
            templateBrowseDialog.setTitle("Browse for execution");
            templateBrowseDialog.open();
            ScriptEntity result = (ScriptEntity) templateBrowseDialog.getFirstResult();

            if (result != null) {
                templater.template(event, result.getScript());
            }
            statefulBeans.stream().forEach(bean -> bean.resetState());
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
