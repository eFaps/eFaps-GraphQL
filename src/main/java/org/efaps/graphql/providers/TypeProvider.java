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
package org.efaps.graphql.providers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.efaps.admin.EFapsSystemConfiguration;
import org.efaps.eql.EQL;
import org.efaps.eql.builder.Selectables;
import org.efaps.graphql.ci.CIGraphQL;
import org.efaps.graphql.definition.ArgumentDef;
import org.efaps.graphql.definition.FieldDef;
import org.efaps.graphql.definition.ObjectDef;
import org.efaps.graphql.util.Utils;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.GraphQLContext;
import graphql.language.BooleanValue;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLEnumValueDefinition;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;

public class TypeProvider
    extends AbstractProvider
{

    private static final Logger LOG = LoggerFactory.getLogger(TypeProvider.class);

    private static Set<GraphQLType> TYPECACHE = new HashSet<>();
    private static Map<String, Object> VALUECACHE = new HashMap<>();

    public Set<GraphQLType> getTypes(final GraphQLContext.Builder contextBldr)
        throws EFapsException
    {
        Set<GraphQLType> ret;
        if (EFapsSystemConfiguration.get().getAttributeValueAsBoolean(Utils.SYSCONF_SCHEMA_CACHE)) {
            if (TYPECACHE.isEmpty()) {
                readTypes(contextBldr, true);
            } else {
                LOG.info("Loading types from CACHE");
            }
            ret = TYPECACHE;
            for (final var entry : VALUECACHE.entrySet()) {
                contextBldr.of(entry.getKey(), entry.getValue());
            }
        } else {
            ret = readTypes(contextBldr, false);
        }
        return ret;
    }

    private Set<GraphQLType> readTypes(final GraphQLContext.Builder contextBldr,
                                       final boolean cacheing)
        throws EFapsException
    {
        final Set<GraphQLType> ret = new HashSet<>();
        final Map<String, Object> contextValues = new HashMap<>();
        ret.addAll(getObjectTypes(contextValues));
        ret.addAll(getInputTypes(contextValues));
        ret.addAll(getEnumTypes());
        for (final var entry : contextValues.entrySet()) {
            contextBldr.of(entry.getKey(), entry.getValue());
        }
        if (cacheing) {
            TYPECACHE.clear();
            TYPECACHE.addAll(ret);
            VALUECACHE.clear();
            VALUECACHE.putAll(contextValues);
        }
        return ret;
    }

    private Set<GraphQLType> getObjectTypes(final Map<String, Object> contextValues)
        throws EFapsException
    {
        final var ret = new HashSet<GraphQLType>();
        final var eval = EQL.builder().print()
                        .query(CIGraphQL.ObjectType)
                        .select()
                        .attribute(CIGraphQL.ObjectType.Name, CIGraphQL.ObjectType.Description)
                        .evaluate();
        while (eval.next()) {
            final String name = eval.get(CIGraphQL.ObjectType.Name);
            final String description = eval.get(CIGraphQL.ObjectType.Description);
            LOG.debug("ObjectDef: {}", name);
            final var objectDefBldr = ObjectDef.builder();
            objectDefBldr.withName(name)
                            .withOid(eval.inst().getOid());

            final var objectTypeBldr = GraphQLObjectType.newObject()
                            .name(name)
                            .description(description);

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
                LOG.debug("    Field: {}", fieldName);

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
            LOG.info("Read type: '{}' with {}", name, fields);
            ret.add(objectTypeBldr.build());
            contextValues.put(name, objectDefBldr.withFields(fields).build());
        }
        return ret;
    }

    private Set<GraphQLType> getInputTypes(final Map<String, Object> contextValues)
        throws EFapsException
    {
        final var ret = new HashSet<GraphQLType>();
        final var eval = EQL.builder().print()
                        .query(CIGraphQL.InputObjectType)
                        .select()
                        .attribute(CIGraphQL.InputObjectType.Name, CIGraphQL.InputObjectType.Description)
                        .evaluate();
        while (eval.next()) {
            final String name = eval.get(CIGraphQL.InputObjectType.Name);
            final String description = eval.get(CIGraphQL.InputObjectType.Description);
            LOG.debug("ObjectDef: {}", name);
            final var objectDefBldr = ObjectDef.builder();
            objectDefBldr.withName(name)
                            .withOid(eval.inst().getOid());

            final var objectTypeBldr = GraphQLInputObjectType.newInputObject()
                            .name(name)
                            .description(description);

            final var fieldEval = EQL.builder().print()
                            .query(CIGraphQL.InputFieldDefinition)
                            .where()
                            .attribute(CIGraphQL.InputFieldDefinition.ID)
                            .in(EQL.builder()
                                            .nestedQuery(CIGraphQL.ObjectType2FieldDefinition)
                                            .where()
                                            .attribute(CIGraphQL.ObjectType2FieldDefinition.FromID).eq(eval.inst())
                                            .up()
                                            .selectable(Selectables
                                                            .attribute(CIGraphQL.ObjectType2FieldDefinition.ToID)))
                            .select()
                            .attribute(CIGraphQL.InputFieldDefinition.Name, CIGraphQL.InputFieldDefinition.FieldType,
                                            CIGraphQL.InputFieldDefinition.Select,
                                            CIGraphQL.InputFieldDefinition.Required,
                                            CIGraphQL.InputFieldDefinition.DefaultValue)
                            .linkfrom(CIGraphQL.InputFieldDefinition2ObjectType.FromLink)
                            .linkto(CIGraphQL.InputFieldDefinition2ObjectType.ToLink)
                            .attribute(CIGraphQL.ObjectType.Name)
                            .first().as("ObjectName")
                            .evaluate();

            final var fields = new HashMap<String, FieldDef>();
            while (fieldEval.next()) {
                final String fieldName = fieldEval.get(CIGraphQL.InputFieldDefinition.Name);
                final String fieldDescription = fieldEval.get(CIGraphQL.InputFieldDefinition.Description);
                final FieldType fieldType = fieldEval.get(CIGraphQL.InputFieldDefinition.FieldType);
                final String select = fieldEval.get(CIGraphQL.InputFieldDefinition.Select);
                final String defaultValue = fieldEval.get(CIGraphQL.InputFieldDefinition.DefaultValue);
                final var required = BooleanUtils
                                .toBoolean(fieldEval.<Boolean>get(CIGraphQL.InputFieldDefinition.Required));
                final String objectName = fieldEval.get("ObjectName");
                LOG.debug("    Field: {}", fieldName);

                var inputType = evalInputType(fieldType, objectName);
                if (required) {
                    inputType = GraphQLNonNull.nonNull(inputType);
                }
                final var fieldDevBldr = GraphQLInputObjectField.newInputObjectField()
                                .name(fieldName)
                                .description(fieldDescription)
                                .type(inputType);
                if (StringUtils.isNoneEmpty(defaultValue)) {
                    switch (fieldType) {
                        case BOOLEAN:
                            fieldDevBldr.defaultValueLiteral(new BooleanValue(BooleanUtils.toBoolean(defaultValue)));
                            break;
                        case LONG:
                        case INT:
                            fieldDevBldr.defaultValueLiteral(
                                            new IntValue(BigInteger.valueOf(Long.valueOf(defaultValue))));
                            break;
                        default:
                            fieldDevBldr.defaultValueLiteral(new StringValue(defaultValue));
                    }
                }

                final var fieldDef = fieldDevBldr.build();

                objectTypeBldr.field(fieldDef);

                LOG.debug("....{}", fieldDef);

                fields.put(fieldName, FieldDef.builder()
                                .withName(fieldName)
                                .withSelect(select)
                                .build());
            }
            LOG.info("Read type: '{}' with {}", name, fields);
            ret.add(objectTypeBldr.build());
            contextValues.put(name, objectDefBldr.withFields(fields).build());
        }
        return ret;
    }

    private Set<GraphQLType> getEnumTypes()
        throws EFapsException
    {
        final var ret = new HashSet<GraphQLType>();
        final var eval = EQL.builder().print()
                        .query(CIGraphQL.EnumType)
                        .select()
                        .attribute(CIGraphQL.EnumType.Name, CIGraphQL.EnumType.Description)
                        .evaluate();
        while (eval.next()) {
            final String name = eval.get(CIGraphQL.EnumType.Name);
            final String description = eval.get(CIGraphQL.EnumType.Description);
            final var enumTypeBldr = GraphQLEnumType.newEnum()
                            .name(name)
                            .description(description);

            final var valueEval = EQL.builder().print()
                            .query(CIGraphQL.EnumValue)
                            .where()
                            .attribute(CIGraphQL.EnumValue.ID)
                            .in(EQL.builder()
                                            .nestedQuery(CIGraphQL.EnumType2EnumValue)
                                            .where()
                                            .attribute(CIGraphQL.EnumType2EnumValue.FromID).eq(eval.inst())
                                            .up()
                                            .selectable(Selectables
                                                            .attribute(CIGraphQL.EnumType2EnumValue.ToID)))
                            .select()
                            .attribute(CIGraphQL.EnumValue.Name, CIGraphQL.EnumValue.Description,
                                            CIGraphQL.EnumValue.Value)
                            .linkfrom(CIGraphQL.EnumType2EnumValue.FromLink)
                            .linkto(CIGraphQL.EnumType2EnumValue.ToLink)
                            .attribute(CIGraphQL.EnumValue.Name)
                            .first().as("ObjectName")
                            .evaluate();

            while (valueEval.next()) {
                final String valueName = valueEval.get(CIGraphQL.EnumValue.Name);
                final String valueDescription = valueEval.get(CIGraphQL.EnumValue.Description);
                final String valueValue = valueEval.get(CIGraphQL.EnumValue.Value);
                enumTypeBldr.value(GraphQLEnumValueDefinition.newEnumValueDefinition()
                                .name(valueName)
                                .description(valueDescription)
                                .value(valueValue)
                                .build());
            }
            ret.add(enumTypeBldr.build());
        }
        return ret;
    }

    public static void clearCache()
    {
        TYPECACHE.clear();
        VALUECACHE.clear();
    }
}
