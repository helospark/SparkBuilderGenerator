package com.helospark.sparktemplatingplugin.repository.domain;

public class ExportedScriptData {
    private String format;
    private byte[] data;

    public ExportedScriptData(String format, byte[] data) {
        super();
        this.format = format;
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public byte[] getData() {
        return data;
    }

}
