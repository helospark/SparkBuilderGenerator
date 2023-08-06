package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.KEEP_CUSTOM_METHODS_IN_BUILDER;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MethodExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPrivatePredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsStaticPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Removes the main builder class.
 * @author helospark
 */
public class BuilderClassRemover implements BuilderRemoverChainItem {
    private PluginLogger pluginLogger = new PluginLogger();
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;
    private IsPrivatePredicate isPrivatePredicate;
    private IsStaticPredicate isStaticPredicate;
    private PreferencesManager preferencesManager;

    public BuilderClassRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor,
            GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter,
            IsPrivatePredicate isPrivatePredicate, IsStaticPredicate isStaticPredicate,
            PreferencesManager preferencesManager) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
        this.isPrivatePredicate = isPrivatePredicate;
        this.isStaticPredicate = isStaticPredicate;
        this.preferencesManager = preferencesManager;
    }

    @Override
    public void remove(ASTRewrite rewriter, AbstractTypeDeclaration rootType, CompilationUnitModificationDomain modificationDomain) {
        List<AbstractTypeDeclaration> nestedTypes = getNestedTypes(rootType);
        List<AbstractTypeDeclaration> generatedAnnotationAnnotatedClasses = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(nestedTypes);

        if (generatedAnnotationAnnotatedClasses.size() > 0) {
            generatedAnnotationAnnotatedClasses.forEach(clazz -> saveCustomMethodsAndRemoveBuilder(rewriter, clazz, modificationDomain));
        } else {
            nestedTypes.stream()
                    .filter(this::isTypeLooksLikeABuilder)
                    .findFirst()
                    .ifPresent(nestedType -> saveCustomMethodsAndRemoveBuilder(rewriter, nestedType, modificationDomain));
        }

    }

    private void saveCustomMethodsAndRemoveBuilder(ASTRewrite rewriter, AbstractTypeDeclaration clazz, CompilationUnitModificationDomain modificationDomain) {
        if (preferencesManager.getPreferenceValue(KEEP_CUSTOM_METHODS_IN_BUILDER)) {
            saveCustomMethods(clazz, modificationDomain);
        }

        rewriter.remove(clazz, null);
    }

    private void saveCustomMethods(AbstractTypeDeclaration clazz, CompilationUnitModificationDomain modificationDomain) {
        try {
            for (MethodDeclaration method : MethodExtractor.getMethods(clazz)) {
                if (isLikelyACustomMethod(method, modificationDomain.getOriginalType(), modificationDomain.getAst())) {
                    modificationDomain.addSavedCustomMethodDeclaration((MethodDeclaration) ASTNode.copySubtree(modificationDomain.getAst(), method));
                }
            }
        } catch (Exception e) {
            pluginLogger.error("Unable to extracting custom methods", e);
        }
    }

    private boolean isLikelyACustomMethod(MethodDeclaration method, AbstractTypeDeclaration typeToCreate, AST ast) {
        if (method.isConstructor()) {
            return false;
        }
        if (method.parameters().size() == 0 && method.getReturnType2().toString().equals(typeToCreate.getName().toString())) {
            return false; // build() method
        }
        if (isPrivatePredicate.test(method)) {
            return true;
        }
        if (isStaticPredicate.test(method)) {
            return true;
        }
        if (method.parameters().size() != 1) {
            return true;
        }
        // By this time, only with{} methods can possibly be generated, however we cannot refer to the name as it is configurable
        // Basically let's check if the content of the method matches what a standard with method contains

        if (method.getBody() == null || method.getBody().statements().size() != 2) { // all with{} methods have an assignment and a return statement
            return true;
        }
        Statement firstStatement = (Statement) method.getBody().statements().get(0);
        Statement secondStatement = (Statement) method.getBody().statements().get(1);
        String parameterName = ((SingleVariableDeclaration) method.parameters().get(0)).getName().toString();
        if (!(secondStatement instanceof ReturnStatement && ((ReturnStatement) secondStatement).getExpression() instanceof ThisExpression)) {
            return true;
        }
        if (!(firstStatement instanceof ExpressionStatement && ((ExpressionStatement) firstStatement).getExpression() instanceof Assignment)) {
            return true;
        }

        Assignment firstAssignment = (Assignment) ((ExpressionStatement) firstStatement).getExpression();
        if (!(firstAssignment.getLeftHandSide() instanceof FieldAccess)) {
            return true;
        }
        if (!firstAssignment.getRightHandSide().toString().equals(parameterName)) {
            return true;
        }

        return false;
    }

    private boolean isTypeLooksLikeABuilder(AbstractTypeDeclaration nestedType) {
        if (TypeExtractor.getTypes(nestedType).length > 0) {
            return false;
        }
        if (MethodExtractor.getMethods(nestedType).length < 2) {
            return false;
        }
        if (getNumberOfEmptyPrivateConstructors(nestedType) != 1) {
            return false;
        }
        return true;
    }

    private int getNumberOfEmptyPrivateConstructors(AbstractTypeDeclaration nestedType) {
        return Arrays.stream(MethodExtractor.getMethods(nestedType))
                .filter(method -> method.isConstructor())
                .filter(method -> method.parameters().size() == 0)
                .filter(isPrivatePredicate)
                .collect(Collectors.toList())
                .size();
    }

    public List<AbstractTypeDeclaration> getNestedTypes(AbstractTypeDeclaration rootType) {
        return bodyDeclarationOfTypeExtractor.extractBodyDeclaration(rootType, TypeDeclaration.class)
                .stream()
                .filter(a -> a instanceof TypeDeclaration)
                .map(a -> (TypeDeclaration) a)
                .filter(type -> !type.isInterface())
                .collect(Collectors.toList());
    }
}