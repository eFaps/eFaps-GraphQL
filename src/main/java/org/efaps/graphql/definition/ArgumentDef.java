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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.efaps.graphql.providers.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = ArgumentDef.Builder.class)
public class ArgumentDef
{

    private final String name;
    private final String whereStmt;
    private final String key;

    private final FieldType fieldType;

    private ArgumentDef(final Builder builder)
    {
        name = builder.name;
        fieldType = builder.fieldType;
        whereStmt = builder.whereStmt;
        key = builder.key;
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

    public String getKey()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
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
        private String key;

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

        public Builder withKey(final String key)
        {
            this.key = key;
            return this;
        }

        public ArgumentDef build()
        {
            return new ArgumentDef(this);
        }
    }
}
