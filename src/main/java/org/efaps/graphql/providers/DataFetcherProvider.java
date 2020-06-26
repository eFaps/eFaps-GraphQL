
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.efaps.admin.program.esjp.EFapsClassLoader;
import org.efaps.db.Instance;
import org.efaps.eql.EQL;
import org.efaps.graphql.GraphQLServlet;
import org.efaps.graphql.ci.CIGraphQL;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLCodeRegistry.Builder;

public class DataFetcherProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DataFetcherProvider.class);

    public void addDataFetchers(final Builder _registryBldr, final GraphQLContext.Builder _contextBldr)
        throws EFapsException
    {
        final var eval = EQL.builder()
                        .print()
                        .query(CIGraphQL.DataFetcher)
                        .select()
                        .attribute(CIGraphQL.DataFetcher.ClassName)
                        .linkfrom(CIGraphQL.DataFetcher2FieldDefinitionAbstract.FromLink)
                        .linkto(CIGraphQL.DataFetcher2FieldDefinitionAbstract.ToLink)
                        .instance()
                        .first().as("ObjectName")
                        .evaluate();
        while (eval.next()) {
            final String className = eval.get(CIGraphQL.DataFetcher.ClassName);
            final Instance fieldInstance = eval.get("ObjectName");
            String typeName = null;
            String fieldName = null;
            if (fieldInstance != null && fieldInstance.isValid()) {
                if (fieldInstance.getType().equals(CIGraphQL.EntryPointFieldDefinition.getType())) {
                    fieldName = EQL.builder()
                                .print(fieldInstance)
                                .attribute(CIGraphQL.EntryPointFieldDefinition.Name)
                            .evaluate()
                        .get(CIGraphQL.EntryPointFieldDefinition.Name);
                    typeName = GraphQLServlet.QUERYNAME;
                } else {
                    final var fieldEval = EQL.builder()
                                    .print(fieldInstance)
                                    .attribute(CIGraphQL.FieldDefinition.Name)
                                        .linkfrom(CIGraphQL.ObjectType2FieldDefinition.ToLink)
                                        .linkto(CIGraphQL.ObjectType2FieldDefinition.FromLink)
                                        .attribute(CIGraphQL.ObjectType.Name)
                                        .first().as("ObjectName")
                                    .evaluate();
                    typeName = fieldEval.get("ObjectName");
                    fieldName = fieldEval.get(CIGraphQL.FieldDefinition.Name);
                }
            }
            if (className != null && typeName != null && fieldName != null) {
                try {
                    final var clazz = Class.forName(className, false, EFapsClassLoader.getInstance());
                    final var dataFetcher = (DataFetcher<?>) clazz.getConstructor().newInstance();

                    final var propertyEval = EQL.builder().print()
                        .query(CIGraphQL.Property)
                        .where()
                        .attribute(CIGraphQL.Property.ParentElementLink).eq(eval.inst())
                        .select()
                        .attribute(CIGraphQL.Property.Key, CIGraphQL.Property.Value)
                        .evaluate();
                    final var properties =new HashMap<String, String>();
                    while (propertyEval.next()) {
                        properties.put(propertyEval.get(CIGraphQL.Property.Key),
                                        propertyEval.get(CIGraphQL.Property.Value));
                    }
                    if (!properties.isEmpty()) {
                        _contextBldr.of(contextKey(typeName, fieldName), properties);
                    }
                    _registryBldr.dataFetcher(FieldCoordinates.coordinates(typeName, fieldName), dataFetcher);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                                | SecurityException e) {
                    LOG.error("Catched {}", e);
                }
            }
        }
    }

    public static String contextKey(final String _typeName, final String _fieldName)
    {
        return "datafetcher-" + _typeName + "-" + _fieldName;
    }
}
