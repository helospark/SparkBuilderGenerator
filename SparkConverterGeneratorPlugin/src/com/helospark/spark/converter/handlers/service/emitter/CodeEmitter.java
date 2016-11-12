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
    private ConverterConstructorEmitter converterConstructorEmitter;
    private ConverterFieldEmitter converterFieldEmitter;
    private ImportAppender importAppender;

    public CodeEmitter(ConverterClassGenerator converterClassGenerator, ClassTypeAppender classTypeAppender, ModifiableCompilationUnitCreator modifiableCompilationUnitCreator,
            PackageRootFinder packageRootFinder, MethodsEmitter methodsEmitter, ConverterConstructorEmitter converterConstructorEmitter,
            ConverterFieldEmitter converterFieldEmitter, ImportAppender importAppender) {
        this.converterClassGenerator = converterClassGenerator;
        this.classTypeAppender = classTypeAppender;
        this.modifiableCompilationUnitCreator = modifiableCompilationUnitCreator;
        this.packageRootFinder = packageRootFinder;
        this.methodsEmitter = methodsEmitter;
        this.converterConstructorEmitter = converterConstructorEmitter;
        this.converterFieldEmitter = converterFieldEmitter;
        this.importAppender = importAppender;
    }

    public void emitCode(ConverterInputParameters converterInputParameters, List<ConverterTypeCodeGenerationRequest> collectedConverters) {
        collectedConverters.stream()
                .forEach(generationRequest -> generateConverter(converterInputParameters, generationRequest));
    }

    private void generateConverter(ConverterInputParameters converterInputParameters, ConverterTypeCodeGenerationRequest generationRequest) {
        try {
            CompilationUnitModificationDomain converterCompilationUnit = createCompilationUnit(converterInputParameters, generationRequest);
            TypeDeclaration converterClassType = createConverterClass(generationRequest.getClassName(), converterCompilationUnit);

            addDependencies(converterClassType, converterCompilationUnit, generationRequest);
            addConstructor(converterClassType, converterCompilationUnit, generationRequest);
            addConvertMethods(converterClassType, converterCompilationUnit, generationRequest);

            addTypeToCompilationUnit(converterCompilationUnit, converterClassType);
            addImports(converterCompilationUnit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addDependencies(TypeDeclaration newType, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        if (generationRequest.getDependencies().size() > 0) {
            converterFieldEmitter.addFields(newType, compilationUnit, generationRequest);
        }
    }

    private void addConstructor(TypeDeclaration type, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        if (generationRequest.getDependencies().size() > 0) {
            converterConstructorEmitter.addConstructor(type, compilationUnit, generationRequest);
        }
    }

    private void addConvertMethods(TypeDeclaration type, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        generationRequest.getMethods()
                .stream()
                .forEach(method -> methodsEmitter.addMethod(type, compilationUnit, generationRequest, method));
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

    private void addImports(CompilationUnitModificationDomain converterCompilationUnit) {
        importAppender.addImportsToAst(converterCompilationUnit);
    }

    private TypeDeclaration createConverterClass(String className, CompilationUnitModificationDomain compilationUnitModificationDomain) {
        return converterClassGenerator.createConverter(compilationUnitModificationDomain, className);
    }
}
