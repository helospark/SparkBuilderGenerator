package com.helospark.sparktemplatingplugin.repository.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.helospark.sparktemplatingplugin.repository.ByteArrayConverterUtil;

public class ScriptEntity {
    private static final String SPARK_TEMPLATING_TOOL_ENCODING = "UTF-8";
    private Integer version = 1;
    private String commandName;
    private String script;

    @Deprecated
    public ScriptEntity(String commandName, String script) {
        this.commandName = commandName;
        this.script = script;
    }

    public ScriptEntity(String commandName, String script, int version) {
        this.commandName = commandName;
        this.script = script;
        this.version = version;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getScript() {
        return script;
    }

    public Integer getVersion() {
        return version;
    }

    public byte[] serializeToBytes() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] commandNameBytes = commandName.getBytes(SPARK_TEMPLATING_TOOL_ENCODING);
            byte[] scriptBytes = script.getBytes(SPARK_TEMPLATING_TOOL_ENCODING);
            byteStream.write(ByteArrayConverterUtil.intToByteArray(version));
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

            int version = ByteArrayConverterUtil.readBytes(byteStream);

            int commandLength = ByteArrayConverterUtil.readBytes(byteStream);

            byte[] commandBytes = new byte[commandLength];
            byteStream.read(commandBytes);
            String commandName = new String(commandBytes, SPARK_TEMPLATING_TOOL_ENCODING);

            int scriptLength = ByteArrayConverterUtil.readBytes(byteStream);

            byte[] scriptBytes = new byte[scriptLength];
            byteStream.read(scriptBytes);
            String scriptData = new String(scriptBytes, SPARK_TEMPLATING_TOOL_ENCODING);

            return new ScriptEntity(commandName, scriptData, version);
        } catch (Exception e) {
            throw new RuntimeException("Cannot serialize script", e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (commandName == null ? 0 : commandName.hashCode());
        result = prime * result + (script == null ? 0 : script.hashCode());
        result = prime * result + (version == null ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScriptEntity other = (ScriptEntity) obj;
        if (commandName == null) {
            if (other.commandName != null)
                return false;
        } else if (!commandName.equals(other.commandName))
            return false;
        if (script == null) {
            if (other.script != null)
                return false;
        } else if (!script.equals(other.script))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScriptEntity [version=" + version + ", commandName=" + commandName + ", script=" + script + "]";
    }

    public ScriptEntity newWithCommandName(String updatedCommandName) {
        return new ScriptEntity(updatedCommandName, script, version);
    }

    public boolean hasSameContentAs(ScriptEntity other) {
        return this.script.equals(other.getScript()) && version.equals(other.version);
    }

}
