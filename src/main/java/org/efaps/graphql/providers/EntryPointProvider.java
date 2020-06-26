/*
 * Copyright 2003 - 2020 The eFaps Team
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
 *
 */
package org.efaps.graphql.providers;

import java.util.ArrayList;
import java.util.List;

import org.efaps.eql.EQL;
import org.efaps.graphql.ci.CIGraphQL;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.schema.GraphQLFieldDefinition;

public class EntryPointProvider extends AbstractProvider
{

    private static final Logger LOG = LoggerFactory.getLogger(EntryPointProvider.class);

    public List<GraphQLFieldDefinition> getFields()
        throws EFapsException
    {
        final var ret = new ArrayList<GraphQLFieldDefinition>();
        final var eval = EQL.builder().print()
                        .query(CIGraphQL.EntryPointFieldDefinition)
                        .select()
                        .attribute(CIGraphQL.EntryPointFieldDefinition.Name,
                                        CIGraphQL.EntryPointFieldDefinition.FieldType)
                        .linkfrom(CIGraphQL.EntryPointFieldDefinition2ObjectType.FromLink)
                            .linkto(CIGraphQL.EntryPointFieldDefinition2ObjectType.ToLink)
                            .attribute(CIGraphQL.ObjectType.Name)
                            .first().as("ObjectName")
                        .evaluate();
        while (eval.next()) {
            final String fieldName = eval.get(CIGraphQL.EntryPointFieldDefinition.Name);
            final FieldType fieldType = eval.get(CIGraphQL.EntryPointFieldDefinition.FieldType);
            final String objectName = eval.get("ObjectName");
            LOG.info("EntryPointField: {}", fieldName);
            ret.add(GraphQLFieldDefinition.newFieldDefinition()
                            .name(fieldName)
                            .type(evalOutputType(fieldType, objectName))
                            .build());
        }
        return ret;
    }
}
