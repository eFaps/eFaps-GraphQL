package org.efaps.graphql.definition;

import org.efaps.graphql.providers.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ArgumentDef.Builder.class)
public class ArgumentDef
{

    private final String name;
    private final String whereStmt;
    private final FieldType fieldType;

    private ArgumentDef(final Builder builder)
    {
        name = builder.name;
        fieldType = builder.fieldType;
        whereStmt = builder.whereStmt;
    }

    public String getName()
    {
        return name;
    }

    public FieldType getFieldType()
    {
        return fieldType;
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
        private FieldType fieldType;
        private Builder()
        {
        }

        public Builder withName(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder withFieldType(final FieldType fieldType)
        {
            this.fieldType = fieldType;
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
