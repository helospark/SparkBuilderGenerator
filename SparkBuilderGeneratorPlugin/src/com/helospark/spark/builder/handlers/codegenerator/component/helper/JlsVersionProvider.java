package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;

/**
 * Provides the latest JLS (java version) this Eclipse implementation supports.
 * 
 * Implementation note:
 * Unfortunately old version of Eclipse doesn't support getJLSLatest method on AST, therefore, we have to find out the latest
 * version via reflection in that case by checking if JLS20, JLS19... fields exists.
 * @author helospark
 */
public class JlsVersionProvider {

    public static int getLatestJlsVersion() {
        try {
            try {
                return (Integer) AST.class.getMethod("getJLSLatest").invoke(null);
            } catch (Throwable e) {
            }
            for (int jlsVersion = 20; jlsVersion >= 8; --jlsVersion) {
                try {
                    int value = AST.class.getField("JLS" + jlsVersion).getInt(null);
                    return value;
                } catch (Throwable e) {
                }
            }
        } catch (Exception e) {
        }

        return AST.JLS8;
    }
}
