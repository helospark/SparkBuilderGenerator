package com.helospark.spark.builder.handlers;

import java.util.Arrays;

import com.helospark.spark.builder.NamedElement;

public enum BuilderType implements NamedElement {
	REGULAR("regularBuilder", "Regular builder"),
	STAGED("stagedBuilder", "Staged builder");

	private String id;
	private String displayName;

	BuilderType(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	public BuilderType getFromId(String id) {
		return Arrays.stream(values())
				.filter(e -> e.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Cannot find enum with value " + id));
	}

}
