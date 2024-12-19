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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.efaps.db.Context;
import org.efaps.util.EFapsException;

import graphql.Scalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLType;
import graphql.schema.GraphQLTypeReference;

public abstract class AbstractProvider
{

    protected static Map<Long, CompanyCache> CACHE = new HashMap<>();

    protected GraphQLOutputType evalOutputType(final FieldType _fieldType,
                                               final String _objectName)
    {
        final GraphQLOutputType ret = switch (_fieldType) {
            case BIGDECIMAL -> ExtendedScalars.GraphQLBigDecimal;
            case OBJECT -> GraphQLTypeReference.typeRef(_objectName);
            case OBJECTLIST -> GraphQLList.list(GraphQLTypeReference.typeRef(_objectName));
            case BOOLEAN -> Scalars.GraphQLBoolean;
            case INT -> Scalars.GraphQLInt;
            case LONG -> ExtendedScalars.GraphQLLong;
            case DATE -> ExtendedScalars.Date;
            case DATETIME -> ExtendedScalars.DateTime;
            case STRING -> Scalars.GraphQLString;
            default -> Scalars.GraphQLString;
        };
        return ret;
    }

    protected GraphQLInputType evalInputType(final FieldType fieldType)
    {
        return evalInputType(fieldType, null);
    }

    protected GraphQLInputType evalInputType(final FieldType _fieldType,
                                             final String objectName)
    {
        final GraphQLInputType ret = switch (_fieldType) {
            case BIGDECIMAL -> ExtendedScalars.GraphQLBigDecimal;
            case BOOLEAN -> Scalars.GraphQLBoolean;
            case INT -> Scalars.GraphQLInt;
            case LONG -> ExtendedScalars.GraphQLLong;
            case DATE -> ExtendedScalars.Date;
            case DATETIME -> ExtendedScalars.DateTime;
            case OBJECT -> GraphQLTypeReference.typeRef(objectName);
            case OBJECTLIST -> GraphQLList.list(GraphQLTypeReference.typeRef(objectName));
            case ENUM -> GraphQLTypeReference.typeRef(objectName);
            case STRING -> Scalars.GraphQLString;
            default -> Scalars.GraphQLString;
        };
        return ret;
    }

    protected static CompanyCache getCache()
        throws EFapsException
    {
        final var companyId = Context.getThreadContext().getCompany().getId();
        if (!CACHE.containsKey(companyId)) {
            CACHE.put(companyId, new CompanyCache());
        }
        return CACHE.get(companyId);
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static class CompanyCache
    {
        Set<GraphQLType> types = new HashSet<>();
        Map<String, Object> values = new HashMap<>();
        List<GraphQLFieldDefinition> mutationFields = new ArrayList<>();
        Object mutationContext;
        List<GraphQLFieldDefinition> entryPointFields = new ArrayList<>();
        Object entryPointContext;
    }
}
