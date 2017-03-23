package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.STAGED;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderFieldOrderProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
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
    private StagedBuilderBuilderMethodCreator stagedBuilderBuilderMethodCreator;
    private ImportPopulator importPopulator;
    private BuilderOwnerClassFinder builderOwnerClassFinder;
    private StagedBuilderFieldOrderProvider stagedBuilderFieldOrderProvider;
    private StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment;

    public StagedBuilderCompilationUnitGenerator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor,
            StagedBuilderClassCreator stagedBuilderClassCreator,
            PrivateInitializingConstructorCreator privateInitializingConstructorCreator,
            StagedBuilderBuilderMethodCreator stagedBuilderBuilderMethodCreator,
            ImportPopulator importPopulator, BuilderOwnerClassFinder builderOwnerClassFinder,
            StagedBuilderFieldOrderProvider stagedBuilderFieldOrderProvider,
            StagedBuilderInterfaceCreatorFragment stagedBuilderInterfaceCreatorFragment) {
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.stagedBuilderClassCreator = stagedBuilderClassCreator;
        this.privateConstructorPopulator = privateInitializingConstructorCreator;
        this.stagedBuilderBuilderMethodCreator = stagedBuilderBuilderMethodCreator;
        this.importPopulator = importPopulator;
        this.builderOwnerClassFinder = builderOwnerClassFinder;
        this.stagedBuilderFieldOrderProvider = stagedBuilderFieldOrderProvider;
        this.stagedBuilderInterfaceCreatorFragment = stagedBuilderInterfaceCreatorFragment;
    }

    @Override
    public void generateBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        CompilationUnitModificationDomain modificationDomain = builderOwnerClassFinder.asd(compilationUnit, ast,
                rewriter);
        TypeDeclaration originalType = modificationDomain.getOriginalType();
        ListRewrite listRewrite = modificationDomain.getListRewrite();

        List<NamedVariableDeclarationField> fieldToIncludeInBuilder = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        List<StagedBuilderProperties> stagedBuilderStages = stagedBuilderFieldOrderProvider.build(fieldToIncludeInBuilder);
        List<TypeDeclaration> stageInterfaces = createStageInterfaces(modificationDomain, stagedBuilderStages);
        TypeDeclaration builderType = stagedBuilderClassCreator.createBuilderClass(modificationDomain, stagedBuilderStages, stageInterfaces);

        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, fieldToIncludeInBuilder);
        stagedBuilderBuilderMethodCreator.addBuilderMethodToCompilationUnit(modificationDomain, builderType, stageInterfaces.get(0));

        stageInterfaces.stream().forEach(stageInterface -> listRewrite.insertLast(stageInterface, null));
        listRewrite.insertLast(builderType, null);

        importPopulator.populateImports(ast, rewriter, compilationUnit);
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
