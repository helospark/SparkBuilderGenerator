package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.STAGED;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderStaticBuilderCreatorMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertiesProvider;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates staged builder to the given compilation unit.
 *
 * @author helospark
 */
public class StagedBuilderCompilationUnitGenerator implements BuilderCompilationUnitGenerator {
    private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
    private StagedBuilderClassCreator stagedBuilderClassCreator;
    private PrivateInitializingConstructorCreator privateConstructorPopulator;
    private StagedBuilderStaticBuilderCreatorMethodCreator stagedBuilderStaticBuilderCreatorMethodCreator;
    private ImportPopulator importPopulator;
    private StagedBuilderStagePropertiesProvider stagedBuilderStagePropertiesProvider;
    private StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment;

    public StagedBuilderCompilationUnitGenerator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor,
            StagedBuilderClassCreator stagedBuilderClassCreator,
            PrivateInitializingConstructorCreator privateInitializingConstructorCreator,
            StagedBuilderStaticBuilderCreatorMethodCreator stagedBuilderStaticBuilderCreatorMethodCreator,
            ImportPopulator importPopulator,
            StagedBuilderStagePropertiesProvider stagedBuilderStagePropertiesProvider,
            StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment) {
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.stagedBuilderClassCreator = stagedBuilderClassCreator;
        this.privateConstructorPopulator = privateInitializingConstructorCreator;
        this.stagedBuilderStaticBuilderCreatorMethodCreator = stagedBuilderStaticBuilderCreatorMethodCreator;
        this.importPopulator = importPopulator;
        this.stagedBuilderStagePropertiesProvider = stagedBuilderStagePropertiesProvider;
        this.stagedBuilderInterfaceCreatorFragment = stagedBuilderInterfaceCreatorFragment;
    }

    @Override
    public void generateBuilder(CompilationUnitModificationDomain modificationDomain) {
        AST ast = modificationDomain.getAst();
        TypeDeclaration originalType = modificationDomain.getOriginalType();
        ListRewrite listRewrite = modificationDomain.getListRewrite();

        List<NamedVariableDeclarationField> fieldToIncludeInBuilder = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        List<StagedBuilderProperties> stagedBuilderStages = stagedBuilderStagePropertiesProvider.build(fieldToIncludeInBuilder);

        // TODO: eventually have a better design to avoid nulls here
        if (stagedBuilderStages != null) {
            List<TypeDeclaration> stageInterfaces = createStageInterfaces(modificationDomain, stagedBuilderStages);
            TypeDeclaration builderType = stagedBuilderClassCreator.createBuilderClass(modificationDomain, stagedBuilderStages, stageInterfaces);

            privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, fieldToIncludeInBuilder);
            stagedBuilderStaticBuilderCreatorMethodCreator.addBuilderMethodToCompilationUnit(modificationDomain, builderType, stagedBuilderStages);

            stageInterfaces.stream().forEach(stageInterface -> listRewrite.insertLast(stageInterface, null));
            listRewrite.insertLast(builderType, null);

            importPopulator.populateImports(modificationDomain);
        }
    }

    private List<TypeDeclaration> createStageInterfaces(CompilationUnitModificationDomain modificationDomain,
            List<StagedBuilderProperties> stagedBuilderProperties) {
        return stagedBuilderProperties.stream()
                .map(stagedBuilderFieldDomain -> stagedBuilderInterfaceCreatorFragment.createInterfaceFor(modificationDomain, stagedBuilderFieldDomain))
                .collect(Collectors.toList());
    }

    @Override
    public boolean canHandle(BuilderType builderType) {
        return STAGED.equals(builderType);
    }

}
