package com.helospark.spark.converter.handlers.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.core.IType;

public class TemplatedIType {
    private IType type;
    private List<TemplatedIType> templates = Collections.emptyList();

    public TemplatedIType(IType type, List<TemplatedIType> templates) {
        this.type = type;
        this.templates = Optional.ofNullable(templates).orElse(Collections.emptyList());
    }

    public TemplatedIType(IType type) {
        this.type = type;
    }

    public IType getType() {
        return type;
    }

    public List<TemplatedIType> getTemplates() {
        return templates;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof TemplatedIType)) {
            return false;
        }
        TemplatedIType castOther = (TemplatedIType) other;
        return Objects.equals(type, castOther.type) && Objects.equals(templates, castOther.templates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, templates);
    }

    public String getAsString() {
        String result = type.getFullyQualifiedName();
        if (templates.size() > 0) {
            result += "<";
            for (int i = 0; i < templates.size(); ++i) {
                result += templates.get(i).getAsString();
                if (i != templates.size() - 1) {
                    result += ",";
                }
            }
            result += ">";
        }
        return result;
    }

    @Override
    public String toString() {
        return getAsString();
    }

}
