package com.helospark.spark.builder.handlers;

import java.util.Arrays;

public enum BuilderType {
	REGULAR("regularBuilder"),
	STAGED("stagedBuilder");

	private String name;

	BuilderType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BuilderType getFromName(String fromName) {
		return Arrays.stream(values())
				.filter(e -> e.getName().equals(fromName))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Cannot find enum with value " + fromName));
	}

}
