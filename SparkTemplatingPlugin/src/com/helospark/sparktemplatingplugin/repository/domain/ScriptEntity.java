package com.helospark.sparktemplatingplugin.repository.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.helospark.sparktemplatingplugin.repository.ByteArrayConverterUtil;

public class ScriptEntity {
    private static final String SPARK_TEMPLATING_TOOL_ENCODING = "UTF-8";
    private String commandName;
    private String script;

    public ScriptEntity(String commandName, String script) {
        this.commandName = commandName;
        this.script = script;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getScript() {
        return script;
    }

    public byte[] serializeToBytes() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] commandNameBytes = commandName.getBytes(SPARK_TEMPLATING_TOOL_ENCODING);
            byte[] scriptBytes = script.getBytes(SPARK_TEMPLATING_TOOL_ENCODING);
            byteStream.write(ByteArrayConverterUtil.intToByteArray(commandNameBytes.length));
            byteStream.write(commandNameBytes);
            byteStream.write(ByteArrayConverterUtil.intToByteArray(scriptBytes.length));
            byteStream.write(scriptBytes);
            return byteStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Cannot serialize script", e);
        }
    }

    public static ScriptEntity deserializeFromBytes(byte[] bytes) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

            int commandLength = ByteArrayConverterUtil.readBytes(byteStream);

            byte[] commandBytes = new byte[commandLength];
            byteStream.read(commandBytes);
            String commandName = new String(commandBytes, SPARK_TEMPLATING_TOOL_ENCODING);

            int scriptLength = ByteArrayConverterUtil.readBytes(byteStream);

            byte[] scriptBytes = new byte[scriptLength];
            byteStream.read(scriptBytes);
            String scriptData = new String(scriptBytes, SPARK_TEMPLATING_TOOL_ENCODING);

            return new ScriptEntity(commandName, scriptData);
        } catch (Exception e) {
            throw new RuntimeException("Cannot serialize script", e);
        }
    }

}
