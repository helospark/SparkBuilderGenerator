package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.Optional;

import javax.annotation.Generated;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderFieldDomain {
	private List<NamedVariableDeclarationField> namedVariableDeclarationField;
	private String interfaceName;
	private boolean isBuildStage;
	private Optional<StagedBuilderFieldDomain> nextStage = Optional.empty();

	@Generated("SparkTools")
	private StagedBuilderFieldDomain(Builder builder) {
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

	public Optional<StagedBuilderFieldDomain> getNextStage() {
		return nextStage;
	}

	public void setNextStage(StagedBuilderFieldDomain nextStage) {
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

		public StagedBuilderFieldDomain build() {
			return new StagedBuilderFieldDomain(this);
		}
	}

}
