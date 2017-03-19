package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderFieldOrderProvider {
	private StagedBuilderInterfaceNameProvider stagedBuilderInterfaceNameProvider;

	public StagedBuilderFieldOrderProvider(StagedBuilderInterfaceNameProvider stagedBuilderInterfaceNameProvider) {
		this.stagedBuilderInterfaceNameProvider = stagedBuilderInterfaceNameProvider;
	}

	public List<StagedBuilderFieldDomain> build(List<NamedVariableDeclarationField> namedVariableDeclarations) {
		List<StagedBuilderFieldDomain> result = new ArrayList<>();
		// MANDATORY params
		for (int i = 0; i < namedVariableDeclarations.size(); ++i) {
			NamedVariableDeclarationField namedVariableDeclarationField = namedVariableDeclarations.get(i);
			String interfaceName = stagedBuilderInterfaceNameProvider.composeNameFrom(namedVariableDeclarationField);
			result.add(StagedBuilderFieldDomain.builder()
					.withInterfaceName(interfaceName)
					.withIsBuildStage(false)
					.withNamedVariableDeclarationField(Collections.singletonList(namedVariableDeclarationField))
					.build());
		}
		for (int i = 0; i < result.size() - 1; ++i) {
			result.get(i).setNextStage(result.get(i + 1));
		}
		// Optional params
		StagedBuilderFieldDomain buildStage = StagedBuilderFieldDomain.builder()
				.withInterfaceName("IBuildStage")
				.withIsBuildStage(true)
				.withNamedVariableDeclarationField(Collections.emptyList())
				.build();
		result.get(result.size() - 1).setNextStage(buildStage);

		result.add(buildStage);

		return result;
	}
}
