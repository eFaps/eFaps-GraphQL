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
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
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
