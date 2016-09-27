# SparkTools
Useful bundle of Eclipse plugins

## Builder generator

Features:
  - Generates a builder
  - Capable of regenerating the builder
  - Can generate Javadoc and add @NonNull where needed
  - Eclipse 3 compatible (does not use e4)

The generated file look like the following:

    public class Clazz {
      private String asd;
      private Integer bsd;
      private Character cas;

      private Clazz(Builder builder) {
        this.asd = builder.asd;
        this.bsd = builder.bsd;
        this.cas = builder.cas;
      }

  
      /**
       * Creates builder to build {@link Clazz}.
       * @return created builder
       */
      public static Builder builder() {
        return new Builder();
      }


      /**
       * Builder to build {@link Clazz}.
       */
      public static class Builder {
        private String asd;
        private Integer bsd;
        private Character cas;

        private Builder() {
        }

        @NonNull()
        public Builder withAsd(@NonNull() String asd) {
          this.asd = asd;
          return this;
        }

        @NonNull()
        public Builder withBsd(@NonNull() Integer bsd) {
          this.bsd = bsd;
          return this;
        }

        @NonNull()
        public Builder withCas(@NonNull() Character cas) {
          this.cas = cas;
          return this;
        }

        @NonNull()
        public Clazz build() {
          return new Clazz(this);
        }
      }
    }

