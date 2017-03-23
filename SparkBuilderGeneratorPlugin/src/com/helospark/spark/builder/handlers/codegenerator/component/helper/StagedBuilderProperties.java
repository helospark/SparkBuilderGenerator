package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.Optional;

import javax.annotation.Generated;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderProperties {
	private List<NamedVariableDeclarationField> namedVariableDeclarationField;
	private String interfaceName;
	private boolean isBuildStage;
	private Optional<StagedBuilderProperties> nextStage = Optional.empty();

	@Generated("SparkTools")
	private StagedBuilderProperties(Builder builder) {
		this.namedVariableDeclarationField = builder.namedVariableDeclarationField;
		this.interfaceName = builder.interfaceName;
		this.isBuildStage = builder.isBuildStage;
	}

	public List<NamedVariableDeclarationField> getNamedVariableDeclarationField() {
		return namedVariableDeclarationField;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public boolean isBuildStage() {
		return isBuildStage;
	}

	public Optional<StagedBuilderProperties> getNextStage() {
		return nextStage;
	}

	public void setNextStage(StagedBuilderProperties nextStage) {
		this.nextStage = Optional.of(nextStage);
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private List<NamedVariableDeclarationField> namedVariableDeclarationField;
		private String interfaceName;
		private boolean isBuildStage;

		private Builder() {
		}

		public Builder withNamedVariableDeclarationField(
				List<NamedVariableDeclarationField> namedVariableDeclarationField) {
			this.namedVariableDeclarationField = namedVariableDeclarationField;
			return this;
		}

		public Builder withInterfaceName(String interfaceName) {
			this.interfaceName = interfaceName;
			return this;
		}

		public Builder withIsBuildStage(boolean isBuildStage) {
			this.isBuildStage = isBuildStage;
			return this;
		}

		public StagedBuilderProperties build() {
			return new StagedBuilderProperties(this);
		}
	}

}
