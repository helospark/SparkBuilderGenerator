package com.helospark.spark.builder.handlers.codegenerator.domain;

import org.eclipse.jdt.core.dom.FieldDeclaration;

public class NamedVariableDeclarationField {
	private FieldDeclaration fieldDeclaration;
	private String fieldName;
	
	public NamedVariableDeclarationField(FieldDeclaration fieldDeclaration, String fieldName) {
		this.fieldDeclaration = fieldDeclaration;
		this.fieldName = fieldName;
	}

	public FieldDeclaration getFieldDeclaration() {
		return fieldDeclaration;
	}

	public String getFieldName() {
		return fieldName;
	}

	

	
	
}
