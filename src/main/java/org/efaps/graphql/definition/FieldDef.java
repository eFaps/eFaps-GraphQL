package org.efaps.graphql.definition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = FieldDef.Builder.class)
public class FieldDef
{

    private final String name;
    private final String select;

    private FieldDef(final Builder builder)
    {
        name = builder.name;
        select = builder.select;
    }

    public String getName()
    {
        return name;
    }

    public String getSelect()
    {
        return select;
    }

    /**
     * Creates builder to build {@link FieldDef}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link FieldDef}.
     */
    public static final class Builder
    {

        private String name;
        private String select;

        private Builder()
        {
        }

        public Builder withName(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder withSelect(final String select)
        {
            this.select = select;
            return this;
        }

        public FieldDef build()
        {
            return new FieldDef(this);
        }
    }
}
