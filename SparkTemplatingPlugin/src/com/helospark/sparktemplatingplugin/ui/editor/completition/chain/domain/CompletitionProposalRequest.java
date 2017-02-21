package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain;

import java.util.Optional;

import javax.annotation.Generated;

import org.eclipse.jface.text.IDocument;

public class CompletitionProposalRequest {
    private IDocument document;
    private String expression;
    private int completitionOffset;
    private Optional<Class<?>> clazz;

    @Generated("SparkTools")
    private CompletitionProposalRequest(Builder builder) {
        this.document = builder.document;
        this.expression = builder.expression;
        this.completitionOffset = builder.completitionOffset;
        this.clazz = builder.clazz;
    }

    public CompletitionProposalRequest(IDocument document, String expression, int completitionOffset) {
        this.document = document;
        this.expression = expression;
        this.completitionOffset = completitionOffset;
    }

    public IDocument getDocument() {
        return document;
    }

    public String getExpression() {
        return expression;
    }

    public int getCompletitionOffset() {
        return completitionOffset;
    }

    public Optional<Class<?>> getClazz() {
        return clazz;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private IDocument document;
        private String expression;
        private int completitionOffset;
        private Optional<Class<?>> clazz;

        private Builder() {
        }

        public Builder withDocument(IDocument document) {
            this.document = document;
            return this;
        }

        public Builder withExpression(String expression) {
            this.expression = expression;
            return this;
        }

        public Builder withCompletitionOffset(int completitionOffset) {
            this.completitionOffset = completitionOffset;
            return this;
        }

        public Builder withClazz(Optional<Class<?>> clazz) {
            this.clazz = clazz;
            return this;
        }

        public CompletitionProposalRequest build() {
            return new CompletitionProposalRequest(this);
        }
    }

}
