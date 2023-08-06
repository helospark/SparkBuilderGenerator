package com.helospark.spark.builder.handlers.codegenerator.builderprocessor;

import static com.helospark.spark.builder.preferences.StaticPreferences.JSON_DESERIALIZE_CLASS_NAME;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.RecordDeclarationWrapper;
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
        ListRewrite modifierRewrite = createModifierRewriter(compilationUnitModificationDomain, rewriter);

        NormalAnnotation annotation = createAnnotation(ast, compilationUnitModificationDomain, builderType);

        modifierRewrite.insertFirst(annotation, null);

        importRepository.addImport(StaticPreferences.JSON_DESERIALIZE_FULLY_QUALIFIED_NAME);
    }

    private ListRewrite createModifierRewriter(CompilationUnitModificationDomain compilationUnitModificationDomain, ASTRewrite rewriter) {
        AbstractTypeDeclaration type = compilationUnitModificationDomain.getOriginalType();
        if (type.getClass().equals(TypeDeclaration.class)) {
            return rewriter.getListRewrite(type, TypeDeclaration.MODIFIERS2_PROPERTY);
        } else {
            return rewriter.getListRewrite(type, RecordDeclarationWrapper.of(type).getModifiers2Property());
        }
    }

    private NormalAnnotation createAnnotation(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        TypeLiteral typeLiteral = createBuilderClassReferenceLiteral(ast, compilationUnitModificationDomain, builderType);

        NormalAnnotation jsonDeserializeAnnotation = ast.newNormalAnnotation();
        jsonDeserializeAnnotation.setTypeName(ast.newSimpleName(JSON_DESERIALIZE_CLASS_NAME));

        MemberValuePair builderAttribute = ast.newMemberValuePair();
        builderAttribute.setName(ast.newSimpleName("builder"));
        builderAttribute.setValue(typeLiteral);

        jsonDeserializeAnnotation.values().add(builderAttribute);

        return jsonDeserializeAnnotation;
    }

    private TypeLiteral createBuilderClassReferenceLiteral(AST ast, CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        String originalClassName = compilationUnitModificationDomain.getOriginalType().getName().toString();
        String builderClassName = builderType.getName().toString();

        QualifiedType qualifiedType = ast.newQualifiedType(ast.newSimpleType(ast.newSimpleName(originalClassName)), ast.newSimpleName(builderClassName));

        TypeLiteral typeLiteral = ast.newTypeLiteral();
        typeLiteral.setType(qualifiedType);
        return typeLiteral;
    }

}
