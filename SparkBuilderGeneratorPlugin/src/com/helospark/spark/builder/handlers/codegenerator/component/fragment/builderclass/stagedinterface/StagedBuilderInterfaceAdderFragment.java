package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodDeclarationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StagedBuilderMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderFieldDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderInterfaceAdderFragment {
	private StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment;
	private BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment;

	public StagedBuilderInterfaceAdderFragment(StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment,
			BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment) {
		this.stagedBuilderMethodDefiniationCreatorFragment = stagedBuilderMethodDefiniationCreatorFragment;
		this.buildMethodDeclarationCreatorFragment = buildMethodDeclarationCreatorFragment;
	}

	public TypeDeclaration createInterfaceFor(CompilationUnitModificationDomain modificationDomain,
			StagedBuilderFieldDomain stagedBuilderFieldDomain, TypeDeclaration builderType) {
		String interfaceName = stagedBuilderFieldDomain.getInterfaceName();
		AST ast = modificationDomain.getAst();
		TypeDeclaration originalType = modificationDomain.getOriginalType();

		TypeDeclaration addedInterface = ast.newTypeDeclaration();
		addedInterface.setInterface(true);
		addedInterface.setName(ast.newSimpleName(interfaceName));
		addedInterface.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		for (NamedVariableDeclarationField namedVariableDeclarationField : stagedBuilderFieldDomain
				.getNamedVariableDeclarationField()) {
			StagedBuilderFieldDomain nextStage = getNextStage(stagedBuilderFieldDomain);

			MethodDeclaration withMethodDeclaration = stagedBuilderMethodDefiniationCreatorFragment.createNewWithMethod(
					ast,
					namedVariableDeclarationField,
					nextStage);
			addedInterface.bodyDeclarations().add(withMethodDeclaration);
		}
		if (stagedBuilderFieldDomain.isBuildStage()) {
			MethodDeclaration buildMethod = buildMethodDeclarationCreatorFragment.createMethod(ast,
					originalType);
			addedInterface.bodyDeclarations().add(buildMethod);
		}

		Type addedIntefaceType = ast.newSimpleType(ast.newSimpleName(interfaceName));

		builderType.superInterfaceTypes().add(addedIntefaceType);

		return addedInterface;
	}

	private StagedBuilderFieldDomain getNextStage(StagedBuilderFieldDomain stagedBuilderFieldDomain) {
		StagedBuilderFieldDomain nextStage = stagedBuilderFieldDomain.isBuildStage() ? stagedBuilderFieldDomain
				: stagedBuilderFieldDomain.getNextStage().get();
		return nextStage;
	}

}
