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
import java.util.HashMap;
import java.util.List;

import org.efaps.eql.EQL;
import org.efaps.graphql.GraphQLServlet;
import org.efaps.graphql.ci.CIGraphQL;
import org.efaps.graphql.definition.ArgumentDef;
import org.efaps.graphql.definition.FieldDef;
import org.efaps.graphql.definition.ObjectDef;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.GraphQLContext;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;

public class EntryPointProvider extends AbstractProvider
{

    private static final Logger LOG = LoggerFactory.getLogger(EntryPointProvider.class);

    public List<GraphQLFieldDefinition> getFields(final GraphQLContext.Builder _contextBldr)
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
        final var fields = new HashMap<String, FieldDef>();
        while (eval.next()) {
            final String fieldName = eval.get(CIGraphQL.EntryPointFieldDefinition.Name);
            final FieldType fieldType = eval.get(CIGraphQL.EntryPointFieldDefinition.FieldType);
            final String fieldDescription = eval.get(CIGraphQL.EntryPointFieldDefinition.Description);
            final String objectName = eval.get("ObjectName");
            LOG.info("EntryPointField: {}", fieldName);

            final var arguments = new ArrayList<GraphQLArgument>();
            final var argumentEval = EQL.builder().print()
                            .query(CIGraphQL.Argument)
                            .where()
                            .attribute(CIGraphQL.Argument.FieldLink).eq(eval.inst())
                            .select()
                            .attribute(CIGraphQL.Argument.Name, CIGraphQL.Argument.Description,
                                            CIGraphQL.Argument.ArgumentType, CIGraphQL.Argument.WhereStmt)
                            .evaluate();
            final var argumentDefs = new ArrayList<ArgumentDef>();
            while (argumentEval.next()) {
                final String argumentName = argumentEval.get(CIGraphQL.Argument.Name);
                final String argumentDesc = argumentEval.get(CIGraphQL.Argument.Description);
                final String argumentWhereStmt = argumentEval.get(CIGraphQL.Argument.WhereStmt);
                final FieldType argumentType = argumentEval.get(CIGraphQL.Argument.ArgumentType);
                final var argument = GraphQLArgument.newArgument()
                                .name(argumentName)
                                .description(argumentDesc)
                                .type(evalInputType(argumentType))
                                .build();
                arguments.add(argument);
                argumentDefs.add(ArgumentDef.builder()
                    .withName(argumentName)
                    .withWhereStmt(argumentWhereStmt)
                    .build());
            }
            final var fieldDef = GraphQLFieldDefinition.newFieldDefinition()
                .name(fieldName)
                .description(fieldDescription)
                .arguments(arguments)
                .type(evalOutputType(fieldType, objectName))
                .build();
            LOG.info("....{}", fieldDef);
            ret.add(fieldDef);
            fields.put(fieldName, FieldDef.builder()
                            .withName(fieldName)
                            .withArguments(argumentDefs)
                            .build());
        }
        final var objectDefBldr = ObjectDef.builder();
        _contextBldr.of(GraphQLServlet.QUERYNAME, objectDefBldr.withFields(fields).build());
        return ret;
    }
}
