package com.helospark.spark.converter.handlers.service;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;

/**
 * Generates the converter.
 * 
 * @author helospark
 */
public class ConverterGenerator {
    private ClassTypeAppender classTypeAppender;
    private ConverterClassGenerator converterClassGenerator;
    private ConverterConvertMethodGenerator converterConvertMethodGenerator;
    private ConvertableParametersGenerator convertableParametersGenerator;
    private PackageRootFinder packageRootFinder;
    private ModifiableCompilationUnitCreator modifiableCompilationUnitCreator;

    public ConverterGenerator(ClassTypeAppender classTypeAppender, ConverterClassGenerator converterClassGenerator, ConverterConvertMethodGenerator converterConvertMethodGenerator,
            ConvertableParametersGenerator convertableParametersGenerator, PackageRootFinder packageRootFinder, ModifiableCompilationUnitCreator modifiableCompilationUnitCreator) {
        this.classTypeAppender = classTypeAppender;
        this.converterClassGenerator = converterClassGenerator;
        this.converterConvertMethodGenerator = converterConvertMethodGenerator;
        this.convertableParametersGenerator = convertableParametersGenerator;
        this.packageRootFinder = packageRootFinder;
        this.modifiableCompilationUnitCreator = modifiableCompilationUnitCreator;
    }

    public void generate(ConverterInputParameters converterInputParameters) throws JavaModelException, BadLocationException {
        String className = createConverterName(converterInputParameters);

        CompilationUnitModificationDomain compilationUnitModificationDomain = createConverterCompilationUnit(converterInputParameters, className);
        TypeDeclaration newType = createConverterClass(className, compilationUnitModificationDomain);
        addConvertMethod(converterInputParameters, compilationUnitModificationDomain, newType);

        addTypeToCompilationUnit(compilationUnitModificationDomain, newType);
    }

    private CompilationUnitModificationDomain createConverterCompilationUnit(ConverterInputParameters converterInputParameters, String className) {
        IPackageFragmentRoot src = packageRootFinder.findSrcPackageFragmentRoot(converterInputParameters.getJavaProject());
        String packageName = converterInputParameters.getDestinationPackageFragment();
        CompilationUnitModificationDomain compilationUnitModificationDomain = modifiableCompilationUnitCreator.create(src, packageName, className);
        return compilationUnitModificationDomain;
    }

    private void addConvertMethod(ConverterInputParameters converterInputParameters, CompilationUnitModificationDomain compilationUnitModificationDomain,
            TypeDeclaration newType) {
        ConvertableDomain convertableDomainParameters = convertableParametersGenerator.extract(converterInputParameters);
        convertableDomainParameters.getConvertableDomainParameters().stream()
                .filter(parameter -> parameter.getType().equals(ConvertType.CONVERT))
                .forEach(parameter -> recursivelyCreateConverter(converterInputParameters, parameter));

        newType.bodyDeclarations().add(converterConvertMethodGenerator.generate(compilationUnitModificationDomain, convertableDomainParameters));
    }

    private TypeDeclaration createConverterClass(String className, CompilationUnitModificationDomain compilationUnitModificationDomain) {
        return converterClassGenerator.createConverter(compilationUnitModificationDomain, className);
    }

    private String createConverterName(ConverterInputParameters converterInputParameters) {
        return converterInputParameters.getDestinationType().getElementName() + "Converter";
    }

    private void recursivelyCreateConverter(ConverterInputParameters converterInputParameters, ConvertableDomainParameter parameter) {
        try {
            ConverterInputParameters recursiveParameters = ConverterInputParameters.builder()
                    .withDestinationPackageFragment(converterInputParameters.getDestinationPackageFragment())
                    .withSourceType(parameter.getSourceType())
                    .withDestinationType(parameter.getDestinationType())
                    .withJavaProject(converterInputParameters.getJavaProject())
                    .withRecursiveGeneration(converterInputParameters.getRecursiveGeneration())
                    .build();

            this.generate(recursiveParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addTypeToCompilationUnit(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration newType)
            throws JavaModelException, BadLocationException {
        classTypeAppender.addType(compilationUnitModificationDomain, newType);
    }

}
