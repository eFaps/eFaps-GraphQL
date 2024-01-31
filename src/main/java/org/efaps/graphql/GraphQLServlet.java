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
package org.efaps.graphql;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.efaps.util.EFapsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GraphQLServlet
    extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(GraphQLServlet.class);

    @Override
    protected void doGet(final HttpServletRequest _req, final HttpServletResponse _resp)
    {
        LOG.info("GET GraphQLServlet");
        final var queryStr = _req.getParameter("query");
        respond(queryStr, _resp);
    }

    @Override
    protected void doPost(final HttpServletRequest _req, final HttpServletResponse _resp)
        throws ServletException, IOException
    {
        LOG.info("POST GraphQLServlet");
        final var body = IOUtils.toString(_req.getReader());
        final var objectMapper = new ObjectMapper();
        final var map = objectMapper.readValue(body, Map.class);
        respond((String) map.get("query"), _resp);
    }

    protected void respond(final String _queryStr, final HttpServletResponse _resp)
    {
        String queryStr;
        if (StringUtils.isEmpty(_queryStr)) {
            queryStr = "{ __schema { types { name fields { name } } } }";
        } else {
            queryStr = _queryStr;
        }
        try {
            LOG.info("Query: {}", queryStr);
            final var executionResult = new EFapsGraphQL().query(queryStr);
            final var objectMapper = new ObjectMapper();
            final var object = executionResult.getData() == null ? executionResult.getErrors()
                            : executionResult.getData();
            final PrintWriter out = _resp.getWriter();
            _resp.setContentType("application/json");
            _resp.setCharacterEncoding("UTF-8");
            out.print(objectMapper.writeValueAsString(object));
            out.flush();
        } catch (final IOException | EFapsException e) {
            LOG.error("Catched", e);
        }
    }
}
