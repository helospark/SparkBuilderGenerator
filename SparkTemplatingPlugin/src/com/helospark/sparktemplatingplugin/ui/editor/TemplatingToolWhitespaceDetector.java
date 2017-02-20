package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class TemplatingToolWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
