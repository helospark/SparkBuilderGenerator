package com.helospark.spark.converter.handlers.service.emitter;

import java.util.List;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;

public class CodeEmitter {
    private ConverterClassGenerator converterClassGenerator;
    private ClassTypeAppender classTypeAppender;
    private ModifiableCompilationUnitCreator modifiableCompilationUnitCreator;
    private PackageRootFinder packageRootFinder;
    private MethodsEmitter methodsEmitter;

    public void emitCode(ConverterInputParameters converterInputParameters, List<ConverterTypeCodeGenerationRequest> collectedConverters) {
        collectedConverters.stream()
                .forEach(generationRequest -> generateConverter(converterInputParameters, generationRequest));
    }

    private void generateConverter(ConverterInputParameters converterInputParameters, ConverterTypeCodeGenerationRequest generationRequest) {
        try {
            CompilationUnitModificationDomain compilationUnitModificationDomain = createCompilationUnit(converterInputParameters, generationRequest);
            TypeDeclaration newType = createConverterClass(generationRequest.getClassName(), compilationUnitModificationDomain);

            addMethods(newType, generationRequest);

            addTypeToCompilationUnit(compilationUnitModificationDomain, newType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addMethods(TypeDeclaration newType, ConverterTypeCodeGenerationRequest generationRequest) {
        addConstructor(newType, generationRequest);
        addConvertMethods(newType, generationRequest);
    }

    private void addConstructor(TypeDeclaration newType, ConverterTypeCodeGenerationRequest generationRequest) {
    }

    private void addConvertMethods(TypeDeclaration newType, ConverterTypeCodeGenerationRequest generationRequest) {
        generationRequest.getMethods()
                .stream()
                .forEach(method -> methodsEmitter.addMethod(newType, generationRequest, method));
    }

    private CompilationUnitModificationDomain createCompilationUnit(ConverterInputParameters converterInputParameters, ConverterTypeCodeGenerationRequest generationRequest) {
        IPackageFragmentRoot srcPackage = packageRootFinder.findSrcPackageFragmentRoot(converterInputParameters.getJavaProject());
        CompilationUnitModificationDomain compilationUnitModificationDomain = modifiableCompilationUnitCreator.create(srcPackage, generationRequest.getPackageName(),
                generationRequest.getClassName());
        return compilationUnitModificationDomain;
    }

    private void addTypeToCompilationUnit(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration newType)
            throws JavaModelException, BadLocationException {
        classTypeAppender.addType(compilationUnitModificationDomain, newType);
    }

    private TypeDeclaration createConverterClass(String className, CompilationUnitModificationDomain compilationUnitModificationDomain) {
        return converterClassGenerator.createConverter(compilationUnitModificationDomain, className);
    }
}
