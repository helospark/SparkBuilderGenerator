package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.REGULAR;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates the builder to the given compilation unit.
 *
 * @author helospark
 */
public class RegularBuilderCompilationUnitGenerator implements BuilderCompilationUnitGenerator {
	private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
	private RegularBuilderClassCreator regularBuilderClassCreator;
	private PrivateInitializingConstructorCreator privateConstructorPopulator;
	private RegularBuilderBuilderMethodCreator builderMethodPopulator;
	private ImportPopulator importPopulator;
	private BuilderOwnerClassFinder builderOwnerClassFinder;;

	public RegularBuilderCompilationUnitGenerator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor,
			RegularBuilderClassCreator regularBuilderClassCreator,
			PrivateInitializingConstructorCreator privateInitializingConstructorCreator,
			RegularBuilderBuilderMethodCreator regularBuilderBuilderMethodCreator,
			ImportPopulator importPopulator, BuilderOwnerClassFinder builderOwnerClassFinder) {
		this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
		this.regularBuilderClassCreator = regularBuilderClassCreator;
		this.privateConstructorPopulator = privateInitializingConstructorCreator;
		this.builderMethodPopulator = regularBuilderBuilderMethodCreator;
		this.importPopulator = importPopulator;
		this.builderOwnerClassFinder = builderOwnerClassFinder;
	}

	@Override
	public void generateBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
		CompilationUnitModificationDomain result = builderOwnerClassFinder.asd(compilationUnit, ast, rewriter);
		TypeDeclaration originalType = result.getOriginalType();
		ListRewrite listRewrite = result.getListRewrite();

		List<NamedVariableDeclarationField> namedVariableDeclarations = applicableBuilderFieldExtractor
				.findBuilderFields(originalType);
		TypeDeclaration builderType = regularBuilderClassCreator.createBuilderClass(ast, originalType,
				namedVariableDeclarations);
		privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite,
				namedVariableDeclarations);
		builderMethodPopulator.addBuilderMethodToCompilationUnit(ast, listRewrite, originalType, builderType);
		listRewrite.insertLast(builderType, null);
		importPopulator.populateImports(ast, rewriter, compilationUnit);
	}

	@Override
	public boolean canHandle(BuilderType builderType) {
		return REGULAR.equals(builderType);
	}

}
