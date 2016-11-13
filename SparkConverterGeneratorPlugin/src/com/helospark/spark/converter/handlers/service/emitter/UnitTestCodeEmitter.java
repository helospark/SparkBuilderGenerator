package com.helospark.spark.converter.handlers.service.emitter;

import java.util.List;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.emitter.helper.ClassTypeAppender;
import com.helospark.spark.converter.handlers.service.emitter.helper.ModifiableCompilationUnitCreator;
import com.helospark.spark.converter.handlers.service.emitter.helper.PackageRootFinder;
import com.helospark.spark.converter.handlers.service.emitter.helper.TypeDeclarationGenerator;

public class UnitTestCodeEmitter {
    private TypeDeclarationGenerator typeDeclarationGenerator;
    private ClassTypeAppender classTypeAppender;
    private ModifiableCompilationUnitCreator modifiableCompilationUnitCreator;
    private PackageRootFinder packageRootFinder;

    public UnitTestCodeEmitter(TypeDeclarationGenerator typeDeclarationGenerator, ClassTypeAppender classTypeAppender,
            ModifiableCompilationUnitCreator modifiableCompilationUnitCreator, PackageRootFinder packageRootFinder) {
        this.typeDeclarationGenerator = typeDeclarationGenerator;
        this.classTypeAppender = classTypeAppender;
        this.modifiableCompilationUnitCreator = modifiableCompilationUnitCreator;
        this.packageRootFinder = packageRootFinder;
    }

    public void emitUnitTest(ConverterInputParameters converterInputParameters, List<ConverterTypeCodeGenerationRequest> collectedConverters) {
        collectedConverters.stream()
                .forEach(converter -> emitUnitTestForConverter(converterInputParameters, converter));
    }

    private void emitUnitTestForConverter(ConverterInputParameters converterInputParameters, ConverterTypeCodeGenerationRequest converter) {
        try {
            CompilationUnitModificationDomain compilationUnit = createCompilationUnit(converterInputParameters, converter);
            TypeDeclaration unitTestType = createUnitTestClass(converter.getClassName() + "Test", compilationUnit);

            converter.getMethods()
                    .stream()
                    .forEach(method -> addTestMethods(unitTestType, compilationUnit, method));

            addTypeToCompilationUnit(compilationUnit, unitTestType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addTestMethods(TypeDeclaration unitTestType, CompilationUnitModificationDomain compilationUnit, ConverterMethodCodeGenerationRequest method) {

    }

    private CompilationUnitModificationDomain createCompilationUnit(ConverterInputParameters converterInputParameters, ConverterTypeCodeGenerationRequest generationRequest) {
        IPackageFragmentRoot testRootFolder = packageRootFinder.findTestPackageFragmentRoot(converterInputParameters.getJavaProject());
        CompilationUnitModificationDomain compilationUnit = modifiableCompilationUnitCreator.create(testRootFolder, generationRequest.getPackageName(),
                generationRequest.getClassName() + "Test");
        return compilationUnit;
    }

    private void addTypeToCompilationUnit(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration newType)
            throws JavaModelException, BadLocationException {
        classTypeAppender.addType(compilationUnitModificationDomain, newType);
    }

    private TypeDeclaration createUnitTestClass(String className, CompilationUnitModificationDomain compilationUnitModificationDomain) {
        return typeDeclarationGenerator.createConverter(compilationUnitModificationDomain, className);
    }
}
