/*******************************************************************************
 * Copyright (c) 2007, 2015 Spring IDE Developers
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Spring IDE Developers - initial API and implementation
 *******************************************************************************/
package com.helospark.spark.thirdparty;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

/**
 * Extracts the return type for a method. Adapted from:
 * https://github.com/spring-projects/spring-ide/blob/master/plugins/org.springframework.ide.eclipse.core/src/org/springframework/ide/eclipse/core/java/JdtUtils.java
 * 
 * @author Christian Dupuis
 * @author Martin Lippert
 * @author Leo Dos Santos
 * @author helospark
 * @since 2.0
 */
public class SignatureToTypeResolver {

    public IType getJavaTypeFromSignatureClassName(String className, IType contextType) {
        if (contextType == null || className == null) {
            return null;
        }
        try {
            return getJavaType(contextType.getJavaProject().getProject(), resolveClassNameBySignature(className, contextType));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the corresponding Java type for given full-qualified class name.
     * 
     * @param project
     *            the JDT project the class belongs to
     * @param className
     *            the full qualified class name of the requested Java type
     * @return the requested Java type or null if the class is not defined or
     *         the project is not accessible
     */
    private IType getJavaType(IProject project, String className) {
        IJavaProject javaProject = getJavaProject(project);
        if (className != null) {
            // For inner classes replace '$' by '.'
            int pos = className.lastIndexOf('$');
            if (pos > 0) {
                className = className.replace('$', '.');
            }
            try {
                IType type = null;
                // First look for the type in the Java project
                if (javaProject != null) {
                    // TODO CD not sure why we need
                    type = javaProject.findType(className, new NullProgressMonitor());
                    // type = javaProject.findType(className);
                    if (type != null) {
                        return type;
                    }
                }

                // Then look for the type in the referenced Java projects
                for (IProject refProject : project.getReferencedProjects()) {
                    IJavaProject refJavaProject = getJavaProject(refProject);
                    if (refJavaProject != null) {
                        type = refJavaProject.findType(className);
                        if (type != null) {
                            return type;
                        }
                    }
                }

                // fall back and try to locate the class using AJDT
                // TODO: uncomment this call
                // return getAjdtType(project, className);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private IJavaProject getJavaProject(IProject project) {
        if (project.isAccessible()) {
            try {
                if (project.hasNature(JavaCore.NATURE_ID)) {
                    return (IJavaProject) project.getNature(JavaCore.NATURE_ID);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String resolveClassName(String className, IType type) {
        if (className == null || type == null) {
            return className;
        }
        // replace binary $ inner class name syntax with . for source level
        className = className.replace('$', '.');
        String dotClassName = new StringBuilder().append('.').append(className).toString();

        IProject project = type.getJavaProject().getProject();

        try {
            // Special handling for some well-know classes
            if (className.startsWith("java.lang") && getJavaType(project, className) != null) {
                return className;
            }

            // Check if the class is imported
            if (!type.isBinary()) {

                // Strip className to first segment to support
                // ReflectionUtils.MethodCallback
                int ix = className.lastIndexOf('.');
                String firstClassNameSegment = className;
                if (ix > 0) {
                    firstClassNameSegment = className.substring(0, ix);
                }

                // Iterate the imports
                for (IImportDeclaration importDeclaration : type.getCompilationUnit().getImports()) {
                    String importName = importDeclaration.getElementName();
                    // Wildcard imports -> check if the package + className is a
                    // valid type
                    if (importDeclaration.isOnDemand()) {
                        String newClassName = new StringBuilder(importName.substring(0, importName.length() - 1))
                                .append(className).toString();
                        if (getJavaType(project, newClassName) != null) {
                            return newClassName;
                        }
                    }
                    // Concrete import matching .className at the end -> check
                    // if type exists
                    else if (importName.endsWith(dotClassName) && getJavaType(project, importName) != null) {
                        return importName;
                    }
                    // Check if className is multi segmented
                    // (ReflectionUtils.MethodCallback)
                    // -> check if the first segment
                    else if (!className.equals(firstClassNameSegment)) {
                        if (importName.endsWith(firstClassNameSegment)) {
                            String newClassName = new StringBuilder(importName.substring(0,
                                    importName.lastIndexOf('.') + 1)).append(className).toString();
                            if (getJavaType(project, newClassName) != null) {
                                return newClassName;
                            }
                        }
                    }
                }
            }

            // Check if the class is in the same package as the type
            String packageName = type.getPackageFragment().getElementName();
            String newClassName = new StringBuilder(packageName).append(dotClassName).toString();
            if (getJavaType(project, newClassName) != null) {
                return newClassName;
            }

            // Check if the className is sufficient (already fully-qualified)
            if (getJavaType(project, className) != null) {
                return className;
            }

            // Check if the class is coming from the java.lang
            newClassName = new StringBuilder("java.lang").append(dotClassName).toString();
            if (getJavaType(project, newClassName) != null) {
                return newClassName;
            }

            // Fall back to full blown resolution
            String[][] fullInter = type.resolveType(className);
            if (fullInter != null && fullInter.length > 0) {
                return fullInter[0][0] + "." + fullInter[0][1];
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
        }

        return className;
    }

    private String resolveClassNameBySignature(String className, IType type) {
        // in case the type is already resolved
        if (className != null && className.length() > 0 && className.charAt(0) == Signature.C_RESOLVED) {
            return Signature.toString(className).replace('$', '.');
        }
        // otherwise do the resolving
        else {
            className = Signature.toString(className).replace('$', '.');
            return resolveClassName(className, type);
        }
    }

}
