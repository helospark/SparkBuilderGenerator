package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MarkerAnnotation;

import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class MarkerAnnotationBuilder {
    private ImportPopulator importPopulator;

    public MarkerAnnotationBuilder(ImportPopulator importPopulator) {
        this.importPopulator = importPopulator;
    }

    public Annotation buildAnnotation(CompilationUnitModificationDomain compilationUnit, String fullyQualifiedName) {
        AST ast = compilationUnit.getAst();
        String annotationName = importPopulator.addImport(compilationUnit, fullyQualifiedName);
        MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
        markerAnnotation.setTypeName(ast.newSimpleName(annotationName));
        return markerAnnotation;
    }
}
