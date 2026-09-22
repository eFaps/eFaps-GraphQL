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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.ExceptionWhileDataFetching;
import graphql.execution.SimpleDataFetcherExceptionHandler;

public class EFapsDataFetcherExceptionHandler
    extends SimpleDataFetcherExceptionHandler
{

    private static final Logger LOG = LoggerFactory.getLogger(EFapsGraphQL.class);

    @Override
    protected void logException(final ExceptionWhileDataFetching error,
                                final Throwable exception)
    {
        super.logException(error, exception);
        LOG.error("GraphQL - Exception while data fetching", exception);
    }
}
