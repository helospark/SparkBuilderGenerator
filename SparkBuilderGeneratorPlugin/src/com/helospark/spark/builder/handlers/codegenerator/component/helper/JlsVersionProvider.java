package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;

public class JlsVersionProvider {

    public static int getLatestJlsVersion() {
        try {
            try {
                return (Integer) AST.class.getMethod("getJLSLatest").invoke(null);
            } catch (Throwable e) {
            }
            for (int i = 20; i >= 8; --i) {
                try {
                    int value = AST.class.getField("JLS" + i).getInt(null);
                    return value;
                } catch (Throwable e) {
                }
            }
        } catch (Exception e) {
        }

        return AST.JLS8;
    }
}
