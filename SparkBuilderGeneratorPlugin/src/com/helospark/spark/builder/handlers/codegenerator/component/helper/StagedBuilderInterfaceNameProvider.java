package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderInterfaceNameProvider {
	private CamelCaseConverter camelCaseConverter;

	public StagedBuilderInterfaceNameProvider(CamelCaseConverter camelCaseConverter) {
		this.camelCaseConverter = camelCaseConverter;
	}

	public String composeNameFrom(NamedVariableDeclarationField namedVariableDeclarationField) {
		return "I" + camelCaseConverter.toUpperCamelCase(namedVariableDeclarationField.getOriginalFieldName())
				+ "Stage";
	}

}
