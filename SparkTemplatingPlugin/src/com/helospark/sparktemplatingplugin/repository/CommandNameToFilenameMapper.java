package com.helospark.sparktemplatingplugin.repository;

public class CommandNameToFilenameMapper {

	public static final String SPARK_TEMPLATING_TOOL_FILE_EXTENSION = ".stt";

	public String mapToFilename(String commandName) {
		return commandName + SPARK_TEMPLATING_TOOL_FILE_EXTENSION;
	}

	public String mapToCommandName(String name) {
		int extensionIndex = name.lastIndexOf(SPARK_TEMPLATING_TOOL_FILE_EXTENSION);
		if (extensionIndex == -1) {
			throw new RuntimeException("Not a command");
		}
		return name.substring(0, extensionIndex);
	}
}
