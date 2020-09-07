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
package org.efaps.graphql;

import java.util.Set;

import org.efaps.graphql.providers.DataFetcherProvider;
import org.efaps.graphql.providers.EntryPointProvider;
import org.efaps.graphql.providers.TypeProvider;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLContext;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

public class EFapsGraphQL
{
    private static final Logger LOG = LoggerFactory.getLogger(EFapsGraphQL.class);

    public ExecutionResult query(final String _query)
        throws EFapsException
    {
        final GraphQLContext.Builder contextBldr = GraphQLContext.newContext();
        final var registryBldr = GraphQLCodeRegistry.newCodeRegistry();
        final var dataFetcherProvider = new DataFetcherProvider();
        dataFetcherProvider.addDataFetchers(registryBldr, contextBldr);

        final var entryPointProvider = new EntryPointProvider();

        final var entryPointFields = entryPointProvider.getFields();

        final var queryType = GraphQLObjectType.newObject()
                        .name(GraphQLServlet.QUERYNAME)
                        .fields(entryPointFields)
                        .build();

        final var typeProvider = new TypeProvider();
        Set<GraphQLType> types = null;
        try {
            types = typeProvider.getTypes(contextBldr);
        } catch (final EFapsException e) {
            LOG.error("Catched", e);
        }

        final GraphQLSchema graphQLSchema = GraphQLSchema.newSchema()
                        .codeRegistry(registryBldr.build())
                        .query(queryType)
                        .additionalTypes(types)
                        .build();
        final GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        final ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                        .query(_query)
                        .context(contextBldr)
                        .build();

        final ExecutionResult executionResult = build.execute(executionInput);
        return executionResult;
    }
}
