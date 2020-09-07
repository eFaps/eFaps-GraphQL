package org.efaps.graphql.definition;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = FieldDef.Builder.class)
public class FieldDef
{

    private final String name;
    private final String select;
    private final List<ArgumentDef> arguments;

    private FieldDef(final Builder builder)
    {
        name = builder.name;
        select = builder.select;
        arguments = builder.arguments;
    }

    public String getName()
    {
        return name;
    }

    public String getSelect()
    {
        return select;
    }

    public List<ArgumentDef> getArguments()
    {
        return arguments;
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
        private List<ArgumentDef> arguments = Collections.emptyList();

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

        public Builder withArguments(final List<ArgumentDef> arguments)
        {
            this.arguments = arguments;
            return this;
        }

        public FieldDef build()
        {
            return new FieldDef(this);
        }
    }
}
