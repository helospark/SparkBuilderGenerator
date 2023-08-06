package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.builderprocessor.GlobalBuilderPostProcessor;
import com.helospark.spark.builder.handlers.codegenerator.component.DefaultConstructorAppender;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderStaticBuilderCreatorMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Generates staged builder to the given compilation unit.
 *
 * @author helospark
 */
public class StagedBuilderCompilationUnitGenerator {
    private StagedBuilderClassCreator stagedBuilderClassCreator;
    private PrivateInitializingConstructorCreator privateConstructorPopulator;
    private DefaultConstructorAppender defaultConstructorAppender;
    private StagedBuilderStaticBuilderCreatorMethodCreator stagedBuilderStaticBuilderCreatorMethodCreator;
    private ImportPopulator importPopulator;
    private StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment;
    private BuilderRemover builderRemover;
    private GlobalBuilderPostProcessor globalBuilderPostProcessor;

    public StagedBuilderCompilationUnitGenerator(StagedBuilderClassCreator stagedBuilderClassCreator,
            PrivateInitializingConstructorCreator privateInitializingConstructorCreator,
            StagedBuilderStaticBuilderCreatorMethodCreator stagedBuilderStaticBuilderCreatorMethodCreator,
            ImportPopulator importPopulator,
            StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment,
            BuilderRemover builderRemover,
            GlobalBuilderPostProcessor globalBuilderPostProcessor,
            DefaultConstructorAppender defaultConstructorAppender) {
        this.stagedBuilderClassCreator = stagedBuilderClassCreator;
        this.privateConstructorPopulator = privateInitializingConstructorCreator;
        this.stagedBuilderStaticBuilderCreatorMethodCreator = stagedBuilderStaticBuilderCreatorMethodCreator;
        this.importPopulator = importPopulator;
        this.stagedBuilderInterfaceCreatorFragment = stagedBuilderInterfaceCreatorFragment;
        this.builderRemover = builderRemover;
        this.globalBuilderPostProcessor = globalBuilderPostProcessor;
        this.defaultConstructorAppender = defaultConstructorAppender;
    }

    public void generateBuilder(CompilationUnitModificationDomain modificationDomain, List<StagedBuilderProperties> stagedBuilderStages) {
        AST ast = modificationDomain.getAst();
        AbstractTypeDeclaration originalType = modificationDomain.getOriginalType();
        ListRewrite listRewrite = modificationDomain.getListRewrite();

        builderRemover.removeExistingBuilderWhenNeeded(modificationDomain);

        // TODO: eventually have a better design to avoid nulls here
        List<AbstractTypeDeclaration> stageInterfaces = createStageInterfaces(modificationDomain, stagedBuilderStages);
        TypeDeclaration builderType = stagedBuilderClassCreator.createBuilderClass(modificationDomain, stagedBuilderStages, stageInterfaces);

        defaultConstructorAppender.addDefaultConstructorIfNeeded(modificationDomain, collectAllFieldsFromAllStages(stagedBuilderStages));
        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, collectAllFieldsFromAllStages(stagedBuilderStages));
        stagedBuilderStaticBuilderCreatorMethodCreator.addBuilderMethodToCompilationUnit(modificationDomain, builderType, stagedBuilderStages);

        stageInterfaces.stream().forEach(stageInterface -> listRewrite.insertLast(stageInterface, null));
        listRewrite.insertLast(builderType, null);

        globalBuilderPostProcessor.postProcessBuilder(modificationDomain, builderType);

        importPopulator.populateImports(modificationDomain);
    }

    private List<BuilderField> collectAllFieldsFromAllStages(List<StagedBuilderProperties> stagedBuilderStages) {
        return stagedBuilderStages.stream()
                .flatMap(stage -> stage.getNamedVariableDeclarationField().stream())
                .collect(Collectors.toList());
    }

    private List<AbstractTypeDeclaration> createStageInterfaces(CompilationUnitModificationDomain modificationDomain,
            List<StagedBuilderProperties> stagedBuilderProperties) {
        return stagedBuilderProperties.stream()
                .map(stagedBuilderFieldDomain -> stagedBuilderInterfaceCreatorFragment.createInterfaceFor(modificationDomain, stagedBuilderFieldDomain))
                .collect(Collectors.toList());
    }

}
