/*
 * Copyright 2003 - 2022 The eFaps Team
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
import java.util.HashSet;
import java.util.Set;

import org.efaps.eql.EQL;
import org.efaps.eql.builder.Selectables;
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
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;

public class TypeProvider
    extends AbstractProvider
{

    private static final Logger LOG = LoggerFactory.getLogger(TypeProvider.class);

    public Set<GraphQLType> getTypes(final GraphQLContext.Builder _contextBldr)
        throws EFapsException
    {
        final var ret = new HashSet<GraphQLType>();
        final var eval = EQL.builder().print()
                        .query(CIGraphQL.ObjectType)
                        .select()
                        .attribute(CIGraphQL.ObjectType.Name)
                        .evaluate();
        while (eval.next()) {
            final String name = eval.get(CIGraphQL.ObjectType.Name);
            LOG.info("ObjectDef: {}", name);
            final var objectDefBldr = ObjectDef.builder();
            objectDefBldr.withName(name)
                            .withOid(eval.inst().getOid());

            final var objectTypeBldr = GraphQLObjectType.newObject()
                            .name(name);

            final var fieldEval = EQL.builder().print()
                            .query(CIGraphQL.FieldDefinition)
                            .where()
                            .attribute(CIGraphQL.FieldDefinition.ID)
                            .in(EQL.builder()
                                            .nestedQuery(CIGraphQL.ObjectType2FieldDefinition)
                                            .where()
                                            .attribute(CIGraphQL.ObjectType2FieldDefinition.FromID).eq(eval.inst())
                                            .up()
                                            .selectable(Selectables
                                                            .attribute(CIGraphQL.ObjectType2FieldDefinition.ToID)))
                            .select()
                            .attribute(CIGraphQL.FieldDefinition.Name, CIGraphQL.FieldDefinition.FieldType,
                                            CIGraphQL.FieldDefinition.Select)
                            .linkfrom(CIGraphQL.FieldDefinition2ObjectType.FromLink)
                            .linkto(CIGraphQL.FieldDefinition2ObjectType.ToLink)
                            .attribute(CIGraphQL.ObjectType.Name)
                            .first().as("ObjectName")
                            .evaluate();

            final var fields = new HashMap<String, FieldDef>();
            while (fieldEval.next()) {
                final String fieldName = fieldEval.get(CIGraphQL.FieldDefinition.Name);
                final String fieldDescription = fieldEval.get(CIGraphQL.FieldDefinition.Description);
                final FieldType fieldType = fieldEval.get(CIGraphQL.FieldDefinition.FieldType);
                final String select = fieldEval.get(CIGraphQL.FieldDefinition.Select);
                final String objectName = fieldEval.get("ObjectName");
                LOG.info("    Field: {}", fieldName);

                final var arguments = new ArrayList<GraphQLArgument>();
                final var argumentEval = EQL.builder().print()
                                .query(CIGraphQL.Argument)
                                .where()
                                .attribute(CIGraphQL.Argument.FieldLink).eq(fieldEval.inst())
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
                    argumentDefs.add(ArgumentDef.builder()
                                    .withName(argumentName)
                                    .withFieldType(fieldType)
                                    .withWhereStmt(argumentWhereStmt)
                                    .build());
                    arguments.add(argument);
                }
                final var fieldDef = GraphQLFieldDefinition.newFieldDefinition()
                                .name(fieldName)
                                .description(fieldDescription)
                                .arguments(arguments)
                                .type(evalOutputType(fieldType, objectName))
                                .build();
                objectTypeBldr.field(fieldDef);

                LOG.debug("....{}", fieldDef);

                fields.put(fieldName, FieldDef.builder()
                                .withName(fieldName)
                                .withSelect(select)
                                .withArguments(argumentDefs)
                                .build());
            }
            ret.add(objectTypeBldr.build());
            _contextBldr.of(name, objectDefBldr.withFields(fields).build());
        }
        return ret;
    }

}
