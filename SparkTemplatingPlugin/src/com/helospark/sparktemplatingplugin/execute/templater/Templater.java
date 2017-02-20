package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentProjectProvider;

public class Templater {
    private ScriptPreProcessor scriptPreProcessor;
    private ScriptInterpreter scriptInterpreter;
    private CurrentProjectProvider cpp = new CurrentProjectProvider();

    public Templater(ScriptPreProcessor scriptPreProcessor, ScriptInterpreter scriptInterpreter) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void template(ExecutionEvent event, String program) {
        // String program = "#import
        // com.helospark.sparktemplatingplugin.execute.templater.*;\n"
        // + "public class TestClass {\n"
        // + " System.out.println(\"Hello world\");\n"
        // + " for (int i = 0; i < 10; ++i) {\n"
        // + " i*= 2;\n"
        // + " }\n"
        // + "}\n"
        // + "# TemplatingResult tr = result;\n"
        // + "# tr.appendToNewFile(currentProject, \"src/NewClass.java\");";
        String preprocessedScript = scriptPreProcessor.preprocessScript(program);
        System.out.println("Preprocessed script: \n" + preprocessedScript);
        scriptInterpreter.interpret(event, preprocessedScript);
    }
}
