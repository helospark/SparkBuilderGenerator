package com.helospark.sparktemplatingplugin.scriptimport.job;

import java.util.ArrayList;
import java.util.List;

public class ImportJobResult {
    List<String> skippedCommands = new ArrayList<>();

    public ImportJobResult(List<String> skippedCommands) {
        this.skippedCommands = skippedCommands;
    }

    public List<String> getSkippedCommands() {
        return skippedCommands;
    }

}
