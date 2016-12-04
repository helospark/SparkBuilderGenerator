package com.helospark.sparktemplatingplugin.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.handlers.HandlerUtil;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.handlers.templater.StatefulBean;
import com.helospark.sparktemplatingplugin.handlers.templater.Templater;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
    private static final String JAVA_TYPE = "org.eclipse.jdt.ui.CompilationUnitEditor";
    private Templater templater;
    private List<StatefulBean> statefulBeans;

    /**
     * The constructor.
     */
    public SampleHandler() {
        this(DiContainer.getDependency(Templater.class), DiContainer.getDependencyList(StatefulBean.class));
    }

    public SampleHandler(Templater templater, List<StatefulBean> statefulBeans) {
        this.templater = templater;
        this.statefulBeans = statefulBeans;
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // if (isCurrentPartJava(event)) {
        try {
            templater.template(event);
            statefulBeans.stream().forEach(bean -> bean.resetState());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        // }
        return null;
    }

    private boolean isCurrentPartJava(ExecutionEvent event) {
        String activePartId = HandlerUtil.getActivePartId(event);
        return JAVA_TYPE.equals(activePartId);
    }

    public CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null);
    }
}
