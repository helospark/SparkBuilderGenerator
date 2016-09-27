package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Extracts fields which can be added to the builder.
 * 
 * @author helospark
 */
public class ApplicableFieldExtractor {

	public List<NamedVariableDeclarationField> filterApplicableFields(FieldDeclaration[] fields) {
		List<NamedVariableDeclarationField> namedVariableDeclarations = new ArrayList<>();
		for (FieldDeclaration field : fields) {
			Object o = field.fragments().get(0);
			if (o instanceof VariableDeclarationFragment) {
				String fieldName = ((VariableDeclarationFragment) o).getName().toString();
				namedVariableDeclarations.add(new NamedVariableDeclarationField(field, fieldName));
			}
		}
		return namedVariableDeclarations;
	}
}
