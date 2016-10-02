# Spark builder generator plugin

## Configuration

Most configuration is on the preferences page: Preferences -> Java -> Spark builder generator

| Configuration | Description | Default | Patterns |
| --- | --- | --- | --- |
| Create builder method pattern | Public static method for generating a new builder. | builder | `[className]` |
| Create Builder class name pattern | Public static class inside the domain object. | Builder | `[className]` |
| Build method name pattern | Public method inside builder to create domain object. | build | `[className]` |
| Builder's method name pattern | Public methods inside the domain object to set each field. | with[FieldName] | `[FieldName]`, `[fieldName]` |
| Generate Javadoc on builder class | Generate Javadoc on builder class. Generated text is not configurable at the moment. | true | - |
| Generate Javadoc on builder method | Generate Javadoc on `builder()` method. Generated text is not configurable at the moment. | true | - |
| Generate Javadoc on each builder's method | Generate Javadoc on `with...()` methods. Generated text is not configurable at the moment. | true | - |
| Add Nonnull to parameters | Add @Nonnull annotation to method parameters. | true | - |
| Add Nonnull to returns | Add @Nonnull annotation to returns. | false | - |
| Override previous builder | Whether to automatically override previous builder, or always create new. | true | - |
| Add @Generated annotation | Whether to add @Generated annotation to generated Builder class, builder method and constructor. The value is `HeloTools` and not configurable. | true | - |


## The generated code looks like the following:


		public class Clazz {
			private Integer firstField;
			private Long secondField;
	
			@Generated("SparkTools")
			private Clazz(Builder builder) {
				this.firstField = builder.firstField;
				this.secondField = builder.secondField;
			}
			/**
			 * Creates builder to build {@link Clazz}.
			 * @return created builder
			 */
			@Generated("SparkTools")
			public static Builder builder() {
				return new Builder();
			}
			/**
			 * Builder to build {@link Clazz}.
			 */
			@Generated("SparkTools")
			public static class Builder {
				private Integer firstField;
				private Long secondField;

				private Builder() {
				}

				/**
				* Builder method for firstField parameter.
				* @return builder
				*/
				@Nonnull
				public Builder withFirstField(@Nonnull Integer firstField) {
					this.firstField = firstField;
					return this;
				}

				/**
				* Builder method for secondField parameter.
				* @return builder
				*/
				@Nonnull
				public Builder withSecondField(@Nonnull Long secondField) {
					this.secondField = secondField;
					return this;
				}

				/**
				* Builder method of the builder.
				* @return built class
				*/
				@Nonnull
				public Clazz build() {
					return new Clazz(this);
				}
			}
		}
 
