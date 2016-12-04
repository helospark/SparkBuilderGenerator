package com.helospark.sparktemplatingplugin.handlers.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.handlers.templater.provider.CurrentProjectProvider;

public class Templater {
    private ScriptPreProcessor scriptPreProcessor;
    private ScriptInterpreter scriptInterpreter;
    private CurrentProjectProvider cpp = new CurrentProjectProvider();

    public Templater(ScriptPreProcessor scriptPreProcessor, ScriptInterpreter scriptInterpreter) {
        this.scriptPreProcessor = scriptPreProcessor;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void template(ExecutionEvent event) {
        String program = "public class TestClass {\n"
                + " System.out.println(\"Hello world\");\n"
                + " for (int i = 0; i < 10; ++i) {\n"
                + "      i*= 2;\n"
                + " }\n"
                + "}\n"
                + "# result.appendToNewFile(currentProject, \"src/NewClass.java\")";
        String preprocessedScript = scriptPreProcessor.preprocessScript(program);
        System.out.println("Preprocessed script: " + preprocessedScript);
        scriptInterpreter.interpret(event, preprocessedScript);
    }
}
