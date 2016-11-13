package com.helospark.spark.converter.handlers.service.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertTypeFinder;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.helper.SignatureToTypeResolver;

public class ConvertableParametersGenerator {
    private SignatureToTypeResolver signatureToTypeResolver;
    private ConvertTypeFinder convertTypeFinder;

    public ConvertableParametersGenerator(SignatureToTypeResolver signatureToTypeResolver, ConvertTypeFinder convertTypeFinder) {
        this.signatureToTypeResolver = signatureToTypeResolver;
        this.convertTypeFinder = convertTypeFinder;
    }

    public ConvertableDomain extract(SourceDestinationType converterInputParameters) {
        List<ConvertableDomainParameter> convertableDomainParameterList = new ArrayList<>();

        boolean useBuilder = false;
        IMethod[] sourceMethods = getMethods(converterInputParameters.getSourceType().getType());
        List<IMethod> filteredSourceMethods = extractMethodsStartingWith(sourceMethods, "get", 0);

        List<IMethod> filteredDestinationMethods;
        if (hasBuilder(converterInputParameters.getDestinationType().getType())) {
            IType builderClass = extractBuilderClass(converterInputParameters.getDestinationType().getType());
            IMethod[] destinationMethods = getMethods(builderClass);
            filteredDestinationMethods = extractMethodsStartingWith(destinationMethods, "with", 1);
            useBuilder = true;
        } else {
            IMethod[] destinationMethods = getMethods(converterInputParameters.getDestinationType().getType());
            filteredDestinationMethods = extractMethodsStartingWith(destinationMethods, "set", 1);
            useBuilder = false;
        }

        for (IMethod sourceMethod : filteredSourceMethods) {
            String getterName = sourceMethod.getElementName();
            String fieldName = methodNameToFieldName(getterName);
            Optional<IMethod> method = findMethodForField(filteredDestinationMethods, fieldName);
            convertableDomainParameterList.add(createConvertableDomainParameter(Optional.of(sourceMethod), method, converterInputParameters.getSourceType()));
        }
        for (IMethod destinationMethod : filteredDestinationMethods) {
            Optional<IMethod> method = findMethodForField(filteredSourceMethods, methodNameToFieldName(destinationMethod.getElementName()));
            if (!method.isPresent()) {
                convertableDomainParameterList
                        .add(createConvertableDomainParameter(Optional.empty(), Optional.of(destinationMethod), converterInputParameters.getSourceType()));
            }
        }

        return ConvertableDomain.builder()
                .withUseBuilder(useBuilder)
                .withConvertableDomainParameters(convertableDomainParameterList)
                .withSourceObject(converterInputParameters.getSourceType())
                .withDestinationObject(converterInputParameters.getDestinationType())
                .build();
    }

    private IMethod[] getMethods(IType builderClass) {
        try {
            return builderClass.getMethods();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private IType extractBuilderClass(IType destinationType) {
        try {
            return Arrays.stream(destinationType.getTypes())
                    .filter(type -> type.getElementName().equals("Builder"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No builder class"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasBuilder(IType destinationType) {
        try {
            Optional<IMethod> builderMethod = Arrays.stream(getMethods(destinationType))
                    .filter(method -> method.getElementName().equals("builder"))
                    .findFirst();
            if (!builderMethod.isPresent()) {
                return false;
            }
            IType[] types = destinationType.getTypes();
            if (types.length == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ConvertableDomainParameter createConvertableDomainParameter(Optional<IMethod> sourceMethod, Optional<IMethod> destinationMethod,
            TemplatedIType referenceType) {
        try {
            Optional<TemplatedIType> destinationType = destinationMethod.map(m -> getFirstParameterType(m, referenceType.getType()));
            Optional<TemplatedIType> sourceType = sourceMethod.map(m -> getReturnType(m, referenceType.getType()));
            ConvertType convertType = convertTypeFinder.findConvertType(sourceType, destinationType);

            return ConvertableDomainParameter.builder()
                    .withDestinationMethodName(destinationMethod.map(m -> m.getElementName()).orElse(""))
                    .withDestinationParameterName(destinationMethod.map(m -> methodNameToFieldName(m.getElementName())).orElse(""))
                    .withDestinationType(destinationType.orElse(null))
                    .withSourceMethodName(sourceMethod.map(m -> m.getElementName()).orElse(""))
                    .withSourceParameterName(sourceMethod.map(m -> methodNameToFieldName(m.getElementName())).orElse(""))
                    .withSourceType(sourceType.orElse(null))
                    .withType(convertType)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TemplatedIType getFirstParameterType(IMethod m, IType referenceType) {
        try {
            ILocalVariable[] parameters = m.getParameters();
            if (parameters.length != 1) {
                throw new RuntimeException("Setters should only have one parameter. " + m.getElementName() + " has " + parameters.length);
            }
            String signature = parameters[0].getTypeSignature();
            return signatureToTypeResolver.getJavaTypeFromSignatureClassName(signature, referenceType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TemplatedIType getReturnType(IMethod method, IType reference) {
        try {
            String returnTypeSignature = method.getReturnType();
            return signatureToTypeResolver.getJavaTypeFromSignatureClassName(returnTypeSignature, reference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String methodNameToFieldName(String elementName) {
        int i = 0;
        for (i = 0; i < elementName.length(); ++i) {
            if (Character.isUpperCase(elementName.charAt(i))) {
                break;
            }
        }
        if (i >= elementName.length()) {
            return "";
        } else {
            return Character.toLowerCase(elementName.charAt(i)) + elementName.substring(i + 1);
        }
    }

    private Optional<IMethod> findMethodForField(List<IMethod> filteredDestinationMethods, String elementName) {
        return filteredDestinationMethods.stream()
                .filter(method -> methodNameToFieldName(method.getElementName()).equals(elementName))
                .findFirst();
    }

    private List<IMethod> extractMethodsStartingWith(IMethod[] sourceMethods, String starting, int parameterCount) {
        return Arrays.stream(sourceMethods)
                .filter(method -> method.getElementName().startsWith(starting))
                .filter(method -> getParameters(method).length == parameterCount)
                .collect(Collectors.toList());
    }

    private ILocalVariable[] getParameters(IMethod method) {
        try {
            return method.getParameters();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
