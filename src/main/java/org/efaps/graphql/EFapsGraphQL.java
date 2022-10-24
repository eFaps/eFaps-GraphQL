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
package org.efaps.graphql;

import java.util.Set;
import java.util.function.Consumer;

import org.efaps.graphql.providers.DataFetcherProvider;
import org.efaps.graphql.providers.EntryPointProvider;
import org.efaps.graphql.providers.MutationProvider;
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
        final var schemaBldr = GraphQLSchema.newSchema();
        final var ctx = withContext(schemaBldr);

        final ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                        .query(_query)
                        .graphQLContext(ctx)
                        .build();
        final GraphQL build = GraphQL.newGraphQL(schemaBldr.build()).build();
        final ExecutionResult executionResult = build.execute(executionInput);
        return executionResult;
    }

    protected Consumer<GraphQLContext.Builder> withContext(final GraphQLSchema.Builder schemaBldr)
    {
        return contextBldr -> {
            try {
                final var dataFetcherProvider = new DataFetcherProvider();
                final var registryBldr = GraphQLCodeRegistry.newCodeRegistry();
                dataFetcherProvider.addDataFetchers(registryBldr, contextBldr);

                final var entryPointFields = new EntryPointProvider().getFields(contextBldr);
                final var queryType = GraphQLObjectType.newObject()
                                .name(GraphQLServlet.QUERYNAME)
                                .fields(entryPointFields)
                                .build();

                final var typeProvider = new TypeProvider();
                Set<GraphQLType> types = null;
                types = typeProvider.getTypes(contextBldr);
                schemaBldr.codeRegistry(registryBldr.build())
                    .query(queryType)
                    .additionalTypes(types);

                final var mutationFields = new MutationProvider().getFields(contextBldr);
                if (!mutationFields.isEmpty()) {
                    final var mutationType = GraphQLObjectType.newObject()
                                .name(GraphQLServlet.MUTATIONNAME)
                                .fields(mutationFields)
                                .build();
                    schemaBldr.mutation(mutationType);
                }

            } catch (final EFapsException e) {
                LOG.error("Catched {}", e);
                e.printStackTrace();
            }
        };
    }

}
