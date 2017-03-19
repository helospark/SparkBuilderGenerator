package com.helospark.spark.builder.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.BuilderCompilationUnitGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;

/**
 * Generates a builder.
 *
 * @author helospark
 */
public class GenerateBuilderExecutorImpl implements GenerateBuilderExecutor {
	private CompilationUnitParser compilationUnitParser;
	private List<BuilderCompilationUnitGenerator> builderGenerators;
	private BuilderRemover builderRemover;
	private IsEventOnJavaFilePredicate isEventOnJavaFilePredicate;
	private WorkingCopyManagerWrapper workingCopyManagerWrapper;
	private CompilationUnitSourceSetter compilationUnitSourceSetter;

	public GenerateBuilderExecutorImpl(CompilationUnitParser compilationUnitParser,
			List<BuilderCompilationUnitGenerator> builderGenerators, BuilderRemover builderRemover,
			IsEventOnJavaFilePredicate isEventOnJavaFilePredicate,
			WorkingCopyManagerWrapper workingCopyManagerWrapper,
			CompilationUnitSourceSetter compilationUnitSourceSetter) {
		this.compilationUnitParser = compilationUnitParser;
		this.builderGenerators = builderGenerators;
		this.builderRemover = builderRemover;
		this.isEventOnJavaFilePredicate = isEventOnJavaFilePredicate;
		this.workingCopyManagerWrapper = workingCopyManagerWrapper;
		this.compilationUnitSourceSetter = compilationUnitSourceSetter;
	}

	@Override
	public void execute(ExecutionEvent event, BuilderType builderType) throws ExecutionException {
		if (isEventOnJavaFilePredicate.test(event)) {
			ICompilationUnit iCompilationUnit = workingCopyManagerWrapper.getCurrentCompilationUnit(event);
			addBuilder(iCompilationUnit, builderType);
		}
	}

	private void addBuilder(ICompilationUnit iCompilationUnit, BuilderType builderType) {
		CompilationUnit compilationUnit = compilationUnitParser.parse(iCompilationUnit);
		AST ast = compilationUnit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);

		builderRemover.removeExistingBuilderWhenNeeded(ast, rewriter, compilationUnit);
		builderGenerators.stream()
				.filter(builderGenerator -> builderGenerator.canHandle(builderType))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No builder generator can handle " + builderType))
				.generateBuilder(ast, rewriter, compilationUnit);

		compilationUnitSourceSetter.commitCodeChange(iCompilationUnit, rewriter);
	}

}
