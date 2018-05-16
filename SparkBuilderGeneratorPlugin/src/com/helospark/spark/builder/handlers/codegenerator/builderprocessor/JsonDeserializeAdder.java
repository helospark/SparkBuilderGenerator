package com.helospark.spark.builder.handlers.codegenerator.builderprocessor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.preferences.StaticPreferences;

/**
 * Adds {@literal @}JsonDeserialize annotation to the class where the builder is generated.
 * <p>
 * Note, that the same ListRewriter cannot be used to add the builder class and to change the modifier on the main type.
 * @author helospark
 */
public class JsonDeserializeAdder {
    private ImportRepository importRepository;

    public JsonDeserializeAdder(ImportRepository importRepository) {
        this.importRepository = importRepository;
    }

    public void addJsonDeserializeAnnotation(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        AST ast = compilationUnitModificationDomain.getAst();
        ASTRewrite rewriter = compilationUnitModificationDomain.getAstRewriter();
        ListRewrite modifierRewrite = rewriter.getListRewrite(compilationUnitModificationDomain.getOriginalType(), TypeDeclaration.MODIFIERS2_PROPERTY);

        String originalClassName = compilationUnitModificationDomain.getOriginalType().getName().toString();
        String builderClassName = builderType.getName().toString();
        TypeLiteral type = createBuilderClassReferenceLiteral(ast, originalClassName, builderClassName);

        NormalAnnotation annotation = createAnnotation(ast, type);

        modifierRewrite.insertFirst(annotation, null);

        importRepository.addImport(StaticPreferences.JSON_DESERIALIZE_FULLY_QUALIFIED_NAME);
    }

    private NormalAnnotation createAnnotation(AST ast, TypeLiteral type) {
        NormalAnnotation ann = ast.newNormalAnnotation();
        ann.setTypeName(ast.newSimpleName(StaticPreferences.JSON_DESERIALIZE_CLASS_NAME));

        MemberValuePair builderAttribute = ast.newMemberValuePair();
        builderAttribute.setName(ast.newSimpleName("builder"));
        builderAttribute.setValue(type);
        ann.values().add(builderAttribute);

        return ann;
    }

    private TypeLiteral createBuilderClassReferenceLiteral(AST ast, String originalClassName, String builderClassName) {
        QualifiedType qualifiedType = ast.newQualifiedType(ast.newSimpleType(ast.newSimpleName(originalClassName)), ast.newSimpleName(builderClassName));
        TypeLiteral typeLiteral = ast.newTypeLiteral();
        typeLiteral.setType(qualifiedType);
        return typeLiteral;
    }

}
