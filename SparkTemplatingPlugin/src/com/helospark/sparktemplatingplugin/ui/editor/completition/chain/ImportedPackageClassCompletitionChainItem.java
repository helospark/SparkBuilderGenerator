package com.helospark.sparktemplatingplugin.ui.editor.completition.chain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.helospark.sparktemplatingplugin.support.ImplicitImportList;
import com.helospark.sparktemplatingplugin.support.classpath.ClassInClasspathLocator;
import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public class ImportedPackageClassCompletitionChainItem implements CompletitionChain {
	private ClassInClasspathLocator classInClasspathLocator;

	public ImportedPackageClassCompletitionChainItem(ClassInClasspathLocator classInClasspathLocator) {
		this.classInClasspathLocator = classInClasspathLocator;
	}

	@Override
	public List<CompletitionProposalResponse> compute(CompletitionProposalRequest request) {
		if (!request.getClazz().isPresent()) {
			return classInClasspathLocator.getAllClassesInPackages(ImplicitImportList.IMPLICIT_IMPORT_LIST)
					.stream()
					.filter(clazz -> classNameStartsWithExpression(request, clazz))
					.map(clazz -> createResponseFor(clazz))
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList(); // classes cannot be added as part of an expression
		}
	}

	private boolean classNameStartsWithExpression(CompletitionProposalRequest request, Class<?> clazz) {
		return clazz.getSimpleName().toLowerCase().startsWith(request.getExpression().toLowerCase());
	}

	private CompletitionProposalResponse createResponseFor(Class<?> clazz) {
		return CompletitionProposalResponse.builder()
				.withAutocompleString(clazz.getSimpleName())
				.withDescription(clazz.getName())
				.withDisplayName(clazz.getSimpleName())
				.withType(clazz)
				.build();
	}

}
