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

import graphql.Scalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLTypeReference;

public abstract class AbstractProvider
{

    protected GraphQLOutputType evalOutputType(final FieldType _fieldType,
                                               final String _objectName)
    {
        GraphQLOutputType ret;
        switch (_fieldType) {
            case BIGDECIMAL:
                ret = ExtendedScalars.GraphQLBigDecimal;
                break;
            case OBJECT:
                ret = GraphQLTypeReference.typeRef(_objectName);
                break;
            case OBJECTLIST:
                ret = GraphQLList.list(GraphQLTypeReference.typeRef(_objectName));
                break;
            case BOOLEAN:
                ret = Scalars.GraphQLBoolean;
                break;
            case INT:
                ret = Scalars.GraphQLInt;
                break;
            case LONG:
                ret = ExtendedScalars.GraphQLLong;
                break;
            case DATE:
                ret = ExtendedScalars.Date;
                break;
            case DATETIME:
                ret = ExtendedScalars.DateTime;
                break;
            case STRING:
            default:
                ret = Scalars.GraphQLString;
                break;
        }
        return ret;
    }

    protected GraphQLInputType evalInputType(final FieldType fieldType)
    {
        return evalInputType(fieldType, null);
    }

    protected GraphQLInputType evalInputType(final FieldType _fieldType,
                                             final String objectName)
    {
        GraphQLInputType ret;
        switch (_fieldType) {
            case BIGDECIMAL:
                ret = ExtendedScalars.GraphQLBigDecimal;
                break;
            case BOOLEAN:
                ret = Scalars.GraphQLBoolean;
                break;
            case INT:
                ret = Scalars.GraphQLInt;
                break;
            case LONG:
                ret = ExtendedScalars.GraphQLLong;
                break;
            case DATE:
                ret = ExtendedScalars.Date;
                break;
            case DATETIME:
                ret = ExtendedScalars.DateTime;
                break;
            case OBJECT:
                ret = GraphQLTypeReference.typeRef(objectName);
                break;
            case OBJECTLIST:
                ret = GraphQLList.list(GraphQLTypeReference.typeRef(objectName));
                break;
            case ENUM:
                ret = GraphQLTypeReference.typeRef(objectName);
                break;
            case STRING:
            default:
                ret = Scalars.GraphQLString;
                break;
        }
        return ret;
    }
}
