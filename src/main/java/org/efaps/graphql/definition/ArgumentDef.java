package org.efaps.graphql.definition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ArgumentDef.Builder.class)
public class ArgumentDef
{

    private final String name;
    private final String whereStmt;

    private ArgumentDef(final Builder builder)
    {
        name = builder.name;
        whereStmt = builder.whereStmt;
    }

    public String getName()
    {
        return name;
    }

    public String getWhereStmt()
    {
        return whereStmt;
    }

    /**
     * Creates builder to build {@link ArgumentDef}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link ArgumentDef}.
     */
    public static final class Builder
    {

        private String name;
        private String whereStmt;

        private Builder()
        {
        }

        public Builder withName(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder withWhereStmt(final String whereStmt)
        {
            this.whereStmt = whereStmt;
            return this;
        }

        public ArgumentDef build()
        {
            return new ArgumentDef(this);
        }
    }
}
