# Spark Builder Generator
Eclipse plugin to generate builders.

You can find it on [Eclipse marketplace here](https://marketplace.eclipse.org/content/spark-builder-generator#group-details)

## Builder generator

Features:
 - Generates a builder with custom name patterns
 - Can generate staged builder
 - Capable of regenerating the builder
 - Compatible with most version of Eclipse
 - Highly configurable, check the plugin's preferences page
 - Capable of generate builder methods for visible fields in superclass
 - Can generate a builder that can initialize a builder based on an already existing instance
 - Encourages and supports null-safe programming practices
 - Open source (with very permissible MIT license)

## Contribution

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

Please check [CONTRIBUTING.md](CONTRIBUTING.md)

## Configuration

Most configuration is on the preferences page: Preferences -> Java -> Spark builder generator

| Configuration | Description | Default | Patterns |
| --- | --- | --- | --- |
| Create builder method pattern | Public static method for generating a new builder. | builder | `[className]` |
| Create Builder class name pattern | Public static class inside the domain object. | Builder | `[className]` |
| Build method name pattern | Public method inside builder to create domain object. | build | `[className]` |
| Builder's method name pattern | Public methods inside the domain object to set each field. | with[FieldName] | `[FieldName]`, `[fieldName]` |
| Generate Javadoc on builder class | Generate Javadoc on builder class. Generated text is not configurable at the moment. | false | - |
| Generate Javadoc on builder method | Generate Javadoc on `builder()` method. Generated text is not configurable at the moment. | false | - |
| Generate Javadoc on each builder's method | Generate Javadoc on `with...()` methods. Generated text is not configurable at the moment. | false | - |
| Add Nonnull to parameters | Add @Nonnull annotation to method parameters. | true | - |
| Add Nonnull to returns | Add @Nonnull annotation to returns. | false | - |
| Initialize optional fields to Optional.empty() value | For all Optionals, adds Optional.empty() initial value in builder, see issue #20 | true | - |
| Initialize collections to empty collections | For Java built-in collection interfaces initialize using java.util.Collection.empty* static methods, see issue #23 | true | - |
| Override previous builder | Whether to automatically override previous builder, or always create new. See @Generated section below. | true | - |
| Add @Generated annotation | Whether to add @Generated annotation to generated Builder class, builder method and constructor. The value is `SparkTools` and not configurable. | true | - |
| Add @Generated annotation on generated interfaces | Whether to add @Generated annotation to generated staged builder interfaces, builder method and constructor. The value is `SparkTools` and not configurable. | true | - |
| Add @Generated annotation on generated interfaces | Whether to add @Generated annotation to generated staged builder interfaces, builder method and constructor. The value is `SparkTools` and not configurable. |
| Generate Javadoc on stage interface for staged builder | Generate Javadoc on staged builder interfaces. Generated text is not configurable at the moment. | false | - |
| Skip static builder() method for staged builder | If set to true, the plugin will not create the static builder(), instead it will create a static method for the first with method. So instead of calling it like `Mail.builder().from(...).to(...)...` you can initialize it like `Mail.from(...).to(...)...` | false | - |
| Last stage interface name for staged builder | Name of the last stage's interface, that contains the build() method | IBuildStage | - |
| Stage interface pattern for staged builder | Pattern of the stage interface names | I[FieldName]Stage | `[fieldName]`, `[FieldName]` |
| Remove prefix and suffix (set in Preferences->Java->Code style)\nfrom builder names | Whether to remove prefixes and suffixes set in code style from builder method names | true | - |
| Include visible fields from superclass | Whether to also include visible fields from super class. Field is visible if it's public, protected. It's also visible if it has a default scope and it's in the same package. Private fields are never visible, therefore not set. | true | - |
| Prefer to use empty superclass constructor | If the superclass has a visible empty constructor use that one, otherwise use the constructor with the most parameters. This settings only take into account if `Include parameters as fields from superclass constructor` is set. | true | - |
| Include parameters as fields from superclass constructor | If set the superclass' constructor parameters will be used to added as fields to the builder and `super(...)` will be called in the constructor. If not set it could cause compilation errors in the generated code. Note: parameters are matched by arguments and not fields in the superclass. | true | - |
| Include fields with setter from superclass | If the superclass has setters, include those in the builder. | false | - |
| Always generate builder to first (top level) class | Always generate builder to first (top level) class, this is the mode used before 0.0.9, in this case it's enough if you execute the command anywhere in the file, the builder will always be generated to the first class (usually the only public class). | false | - |
| Show dialog to filter which fields are included in the builder | Whether to show a field selection dialog when generating a regular builder | false | - |
| Add method to create a builder based on an instance | Creates a method like `public static Builder builderFrom(Clazz instance)` to initialize the state of the builder based on an already existing instance | false | - |
| Pattern of static builder method that copies the given domain object | Name of the above method | builderFrom | `[className]` |
| Add Jackson deserialize (@JsonDeserialize, @JsonPOJOBuilder) annotation | Add Jackson deserialize annotations: @JsonDeserialize, @JsonPOJOBuilder annotation | false | - |
| Create public default constructor (ex. for JPA) | If enabled it generates a public empty default constructor. Generation will be ignored if there is already one, or super(...) call is necessary | false | - |
| Create public builder constructor with mandatory fields | If enabled then for regular builder empty `builder()` method will be omitted and a public constructor will be created for the builder, with mandatory parameters (by default none), which can be selected via the dialog | false | - | 
| Keep custom methods in builder | If enabled then any manually added methods in the builder class will be kept when regenerating the builder. | true | - |
| Use new jakarta.annotation package | If enabled jakarta.annotation will be used for Generated and Nonnull annotations (if an import exists already, plugin will use that) | false | - |

### @Generated annotatation

 - Generated annotation helps the plugin to determine what is safe to remove. If it is turned of the plugin makes a best guess which methods might have been generated by it, but might accidentally remove other methods as well.

## Release notes:

 - Pre 0.0.5
    Base builder generator plugin
 - 0.0.5
   Add option to remove prefix and postfix from builder method name
 - 0.0.6
   Add staged builder option
 - 0.0.7
   Option to include visible fields from superclass
 - 0.0.8
   Regression bug was observed in 0.0.7 under some Eclipse configurations. IllegalArgumentException occured while trying to parse java.lang.Object class.
 - 0.0.9
   Generate builder to the class under the cursor (in case of nested classes, or multiple classes in a single file)
   Improvements to Builder class removing logic
 - 0.0.10
   Added the option to select which fields are generated in the builder
   Fixed a small bug that deleted the previous builder when pressing the cancel button on the staging builder generator dialog
   Added MIT license file to plugin installation license
 - 0.0.11
   Initialize Optional value to Optional.empty() to follow nullsafe programming practices
   Initialize Collections with empty collections via java.util.Collections class
   Various bugfixes related to overriding previous builder
   Logging improvement
 - 0.0.12
   Generate @param tag to the builder's "with" methods
 - 0.0.13
   Add fields from superclass' constructor to the builder
 - 0.0.14
   Add the ability to create a builder based on an already existing instance
   Dialog UX update
 - 0.0.15
   Add builder fields for private fields in superclass that have setters.
 - 0.0.16
   Generate Jackson deserialize annotations to the builder
 - 0.0.17
   Generate public default constructor
 - 0.0.18
   Fixed dialog issue when combined with DevStyle Darkest Dark plugin, also made dialogs more responsive and updated icon
 - 0.0.19
   Fixed ClassCastException when Java file includes an enum as the first type.
   JsonPOJOBuilder annotation is not generated when the default builder names is not overridden
 - 0.0.20
   Fixed regression issue caused by wrong target definition on compiling
 - 0.0.21
   Option to generate public constructor for regular builder that includes mandatory fields
 - 0.0.22
   Fixing incorrect field naming with underscore prefix
 - 0.0.23
   Add option to keep custom methods in the builderclass, when the builder is regenerated
 - 0.0.24
   Add option to initialize builder fields with default value from the class' field assignment
 - 0.0.25
   Fix default value initialization based on superclass' field assignment
 - 0.0.26
   Fix copy constructor when superclass field is not accessible from builder
 - 0.0.27
   Add option to use jakarta.annotation package instead of the old javax.annotation
 - 0.0.28
   Add support for record types

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
				* @param firstField field to set
				* @return builder
				*/
				@Nonnull
				public Builder withFirstField(@Nonnull Integer firstField) {
					this.firstField = firstField;
					return this;
				}

				/**
				* Builder method for secondField parameter.
				* @param secondField field to set
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


## Staged builder generated code example

	public class Mail {
		private String from;
		private String to;
		private String subject;
		private String content;
		private String mimeType; // optional
	
		@Generated("SparkTools")
		private Mail(Builder builder) {
			this.from = builder.from;
			this.to = builder.to;
			this.subject = builder.subject;
			this.content = builder.content;
			this.mimeType = builder.mimeType;
		}
	
		/**
		 * Creates builder to build {@link Mail}.
		 * 
		 * @return created builder
		 */
		@Generated("SparkTools")
		public static IToStage builder() {
			return new Builder();
		}
	
		@Generated("SparkTools")
		public interface IToStage {
			public IFromStage withTo(String to);
		}
	
		@Generated("SparkTools")
		public interface IFromStage {
			public ISubjectStage withFrom(String from);
		}
	
		@Generated("SparkTools")
		public interface ISubjectStage {
			public IContentStage withSubject(String subject);
		}
	
		@Generated("SparkTools")
		public interface IContentStage {
			public IBuildStage withContent(String content);
		}
	
		@Generated("SparkTools")
		public interface IBuildStage {
			public IBuildStage withMimeType(String mimeType);
	
			public Mail build();
		}
	
		/**
		 * Builder to build {@link Mail}.
		 */
		@Generated("SparkTools")
		public static final class Builder implements IToStage, IFromStage, ISubjectStage, IContentStage, IBuildStage {
			private String to;
			private String from;
			private String subject;
			private String content;
			private String mimeType;
	
			private Builder() {
			}
	
			@Override
			public IFromStage withTo(String to) {
				this.to = to;
				return this;
			}
	
			@Override
			public ISubjectStage withFrom(String from) {
				this.from = from;
				return this;
			}
	
			@Override
			public IContentStage withSubject(String subject) {
				this.subject = subject;
				return this;
			}
	
			@Override
			public IBuildStage withContent(String content) {
				this.content = content;
				return this;
			}
	
			@Override
			public IBuildStage withMimeType(String mimeType) {
				this.mimeType = mimeType;
				return this;
			}
	
			@Override
			public Mail build() {
				return new Mail(this);
			}
		}
	
	}


