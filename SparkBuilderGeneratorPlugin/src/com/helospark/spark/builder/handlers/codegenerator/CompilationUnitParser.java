package com.helospark.spark.builder.handlers.codegenerator;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.JlsVersionProvider;

/**
 * Parses ICompilation unit to CompilationUnit.
 *
 * @author helospark
 */
public class CompilationUnitParser {

    public CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(JlsVersionProvider.getLatestJlsVersion());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        parser.setProject(unit.getJavaProject());
        parser.setUnitName(unit.getPath().toString());
        return (CompilationUnit) parser.createAST(null);
    }

    public CompilationUnit parse(IClassFile classFile) {
        ASTParser parser = ASTParser.newParser(JlsVersionProvider.getLatestJlsVersion());
        parser.setSource(classFile);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null);
    }

}
