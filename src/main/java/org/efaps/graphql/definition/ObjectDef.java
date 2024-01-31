/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
