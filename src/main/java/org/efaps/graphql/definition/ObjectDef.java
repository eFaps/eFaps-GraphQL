package org.efaps.graphql.definition;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ObjectDef.Builder.class)
public class ObjectDef
{

    private final String name;
    private final String oid;
    private final Map<String, FieldDef> fields;

    private ObjectDef(final Builder builder)
    {
        name = builder.name;
        oid = builder.oid;
        fields = builder.fields;
    }

    public String getName()
    {
        return name;
    }

    public String getOid()
    {
        return oid;
    }

    public Map<String, FieldDef> getFields()
    {
        return fields;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Creates builder to build {@link ObjectDef}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link ObjectDef}.
     */
    public static final class Builder
    {

        private String name;
        private String oid;
        private Map<String, FieldDef> fields = Collections.emptyMap();

        private Builder()
        {
        }

        public Builder withName(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder withOid(final String oid)
        {
            this.oid = oid;
            return this;
        }

        public Builder withFields(final Map<String, FieldDef> fields)
        {
            this.fields = fields;
            return this;
        }

        public ObjectDef build()
        {
            return new ObjectDef(this);
        }
    }
}
