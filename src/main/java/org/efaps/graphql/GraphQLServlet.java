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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.efaps.graphql.providers.DataFetcherProvider;
import org.efaps.graphql.providers.EntryPointProvider;
import org.efaps.graphql.providers.TypeProvider;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLContext;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;

public class GraphQLServlet
    extends HttpServlet
{

    public static String QUERYNAME = "query";

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLServlet.class);



    @Override
    protected void doGet(final HttpServletRequest _req, final HttpServletResponse _resp)
    {
        LOG.info("GraphQLServlet");

        var queryStr = _req.getParameter("query");
        if (StringUtils.isEmpty(queryStr)) {
            queryStr = "{ __schema { types { name fields { name } } } }";
        }
        try {
            final var executionResult = query(queryStr);
            final var objectMapper = new ObjectMapper();
            final var object = executionResult.getData() == null ? executionResult.getErrors()
                            : executionResult.getData();
            final PrintWriter out = _resp.getWriter();
            _resp.setContentType("application/json");
            _resp.setCharacterEncoding("UTF-8");
            out.print(objectMapper.writeValueAsString(object));
            out.flush();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final EFapsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ExecutionResult query(final String _query) throws EFapsException
    {
        final GraphQLContext.Builder contextBldr = GraphQLContext.newContext();
        final var registryBldr = GraphQLCodeRegistry.newCodeRegistry();
        final var dataFetcherProvider = new DataFetcherProvider();
        dataFetcherProvider.addDataFetchers(registryBldr, contextBldr);

        final var entryPointProvider = new EntryPointProvider();

        final var entryPointFields = entryPointProvider.getFields();

        final var queryType = GraphQLObjectType.newObject()
                        .name(QUERYNAME)
                        .fields(entryPointFields)
                        .build();

        final var typeProvider = new TypeProvider();
        Set<GraphQLType> types = null;
        try {
           types = typeProvider.getTypes(contextBldr);
        } catch (final EFapsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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


    public DataFetcher<?> getProductsFetcherFactory() {
        return (emv) -> {
            final var selectionSet = emv.getSelectionSet();
            final var fields = selectionSet.getFields();
            LOG.info("{}", fields);
            final var map1 = new HashMap<String, Object>();
            map1.put("sku", "123.456");
            final var map2 = new HashMap<String, Object>();
            map2.put("sku", "333.456");
            return DataFetcherResult.newResult()
                            .data(Arrays.asList(map1, map2))
                            .build();
        };
    }

}
