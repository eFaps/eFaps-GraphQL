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
package org.efaps.graphql.ci;

import org.efaps.ci.CIAttribute;
import org.efaps.ci.CIType;

public final class CIGraphQL
{
    public static final _AbstractObjectType AbstractObjectType = new _AbstractObjectType("40029306-cf2a-4ade-8d3c-ac3519953b62");
    public static class _AbstractObjectType extends _ElementAbstract
    {
        protected _AbstractObjectType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _Argument Argument = new _Argument("b01cd270-95ec-4280-ba9b-53e2f95e4e7e");
    public static class _Argument extends _ArgumentAbstract
    {
        protected _Argument(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute WhereStmt = new CIAttribute(this, "WhereStmt");
    }

    public static final _ArgumentAbstract ArgumentAbstract = new _ArgumentAbstract("a32705df-2c5c-41f8-a45c-f28acefefee0");
    public static class _ArgumentAbstract extends _ElementAbstract
    {
        protected _ArgumentAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute ArgumentType = new CIAttribute(this, "ArgumentType");
        public final CIAttribute FieldLink = new CIAttribute(this, "FieldLink");
        public final CIAttribute Required = new CIAttribute(this, "Required");
    }

    public static final _ArgumentAbstract2ObjectType ArgumentAbstract2ObjectType = new _ArgumentAbstract2ObjectType("191db8f8-a548-4d33-af94-8cf4703405f7");
    public static class _ArgumentAbstract2ObjectType extends _Object2ObjectAbstract
    {
        protected _ArgumentAbstract2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _DataFetcher DataFetcher = new _DataFetcher("701c7ea8-0213-4e18-8867-08e6861fb2fe");
    public static class _DataFetcher extends _ElementAbstract
    {
        protected _DataFetcher(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute ClassName = new CIAttribute(this, "ClassName");
    }

    public static final _DataFetcher2FieldDefinitionAbstract DataFetcher2FieldDefinitionAbstract = new _DataFetcher2FieldDefinitionAbstract("b37b691a-31b1-41d1-a6c0-508d68b365c1");
    public static class _DataFetcher2FieldDefinitionAbstract extends _Object2ObjectAbstract
    {
        protected _DataFetcher2FieldDefinitionAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _ElementAbstract ElementAbstract = new _ElementAbstract("c0a47097-5d69-4447-974f-dfaf023d219a");
    public static class _ElementAbstract extends CIType
    {
        protected _ElementAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Company = new CIAttribute(this, "Company");
        public final CIAttribute Created = new CIAttribute(this, "Created");
        public final CIAttribute Creator = new CIAttribute(this, "Creator");
        public final CIAttribute Description = new CIAttribute(this, "Description");
        public final CIAttribute Modified = new CIAttribute(this, "Modified");
        public final CIAttribute Modifier = new CIAttribute(this, "Modifier");
        public final CIAttribute Name = new CIAttribute(this, "Name");
    }

    public static final _EntryPointFieldDefinition EntryPointFieldDefinition = new _EntryPointFieldDefinition("7a5c0ee4-bd08-4a8d-a46e-b58b1a337b64");
    public static class _EntryPointFieldDefinition extends _FieldDefinitionAbstract
    {
        protected _EntryPointFieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _EntryPointFieldDefinition2ObjectType EntryPointFieldDefinition2ObjectType = new _EntryPointFieldDefinition2ObjectType("cee2bcb0-c6c0-48f4-8782-e4081360ca6a");
    public static class _EntryPointFieldDefinition2ObjectType extends _Object2ObjectAbstract
    {
        protected _EntryPointFieldDefinition2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _EnumType EnumType = new _EnumType("de9d0543-cb2b-4a31-9826-31a234ae41a0");
    public static class _EnumType extends _AbstractObjectType
    {
        protected _EnumType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _EnumType2EnumValue EnumType2EnumValue = new _EnumType2EnumValue("ae2e9e2a-c528-4864-b475-f5727c640ac6");
    public static class _EnumType2EnumValue extends _Object2ObjectAbstract
    {
        protected _EnumType2EnumValue(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _EnumValue EnumValue = new _EnumValue("dc94c8b7-2a9d-4fff-b3fa-ab6ea333c725");
    public static class _EnumValue extends _ElementAbstract
    {
        protected _EnumValue(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Value = new CIAttribute(this, "Value");
    }

    public static final _FieldDefinition FieldDefinition = new _FieldDefinition("d8161807-22e2-4e5d-813d-680b6109b188");
    public static class _FieldDefinition extends _FieldDefinitionAbstract
    {
        protected _FieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _FieldDefinition2ObjectType FieldDefinition2ObjectType = new _FieldDefinition2ObjectType("189f62d5-8add-4fb3-bf3a-1a936063d9d3");
    public static class _FieldDefinition2ObjectType extends _Object2ObjectAbstract
    {
        protected _FieldDefinition2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _FieldDefinitionAbstract FieldDefinitionAbstract = new _FieldDefinitionAbstract("a7af940c-0669-449e-9025-d2c12695bf2c");
    public static class _FieldDefinitionAbstract extends _ElementAbstract
    {
        protected _FieldDefinitionAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Boolean1 = new CIAttribute(this, "Boolean1");
        public final CIAttribute FieldType = new CIAttribute(this, "FieldType");
        public final CIAttribute Select = new CIAttribute(this, "Select");
        public final CIAttribute String2 = new CIAttribute(this, "String2");
    }

    public static final _InputFieldDefinition InputFieldDefinition = new _InputFieldDefinition("67d4e1f0-7132-4af3-be5d-1db5a4ea4802");
    public static class _InputFieldDefinition extends _FieldDefinitionAbstract
    {
        protected _InputFieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute DefaultValue = new CIAttribute(this, "DefaultValue");
        public final CIAttribute Required = new CIAttribute(this, "Required");
    }

    public static final _InputFieldDefinition2ObjectType InputFieldDefinition2ObjectType = new _InputFieldDefinition2ObjectType("c9053ab8-94b5-4347-9711-ff14003c846d");
    public static class _InputFieldDefinition2ObjectType extends _Object2ObjectAbstract
    {
        protected _InputFieldDefinition2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _InputObjectType InputObjectType = new _InputObjectType("9beda021-a941-4e38-b2e8-0f3b75e0c557");
    public static class _InputObjectType extends _AbstractObjectType
    {
        protected _InputObjectType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _MutationArgument MutationArgument = new _MutationArgument("c1004bc1-76f7-42ca-bc20-8a517abc17cf");
    public static class _MutationArgument extends _ArgumentAbstract
    {
        protected _MutationArgument(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Key = new CIAttribute(this, "Key");
    }

    public static final _MutationArgument2ObjectType MutationArgument2ObjectType = new _MutationArgument2ObjectType("f689356f-c0fe-48cf-a6af-8fc773fd7089");
    public static class _MutationArgument2ObjectType extends _ArgumentAbstract2ObjectType
    {
        protected _MutationArgument2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _MutationFieldDefinition MutationFieldDefinition = new _MutationFieldDefinition("fd5a6c8c-9e98-4c33-8c6b-52cac9a0773f");
    public static class _MutationFieldDefinition extends _FieldDefinitionAbstract
    {
        protected _MutationFieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _MutationFieldDefinition2ObjectType MutationFieldDefinition2ObjectType = new _MutationFieldDefinition2ObjectType("347e1427-cfdb-4ef3-9c3e-12b7eea6b9c1");
    public static class _MutationFieldDefinition2ObjectType extends _Object2ObjectAbstract
    {
        protected _MutationFieldDefinition2ObjectType(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _Object2ObjectAbstract Object2ObjectAbstract = new _Object2ObjectAbstract("302f2d7a-eba6-48f2-909b-c99973c55ba1");
    public static class _Object2ObjectAbstract extends CIType
    {
        protected _Object2ObjectAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Created = new CIAttribute(this, "Created");
        public final CIAttribute Creator = new CIAttribute(this, "Creator");
        public final CIAttribute FromID = new CIAttribute(this, "FromID");
        public final CIAttribute Modified = new CIAttribute(this, "Modified");
        public final CIAttribute Modifier = new CIAttribute(this, "Modifier");
        public final CIAttribute ToID = new CIAttribute(this, "ToID");
    }

    public static final _ObjectType ObjectType = new _ObjectType("3896c58e-0cd8-46b5-89e8-728520179efa");
    public static class _ObjectType extends _AbstractObjectType
    {
        protected _ObjectType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _ObjectType2FieldDefinition ObjectType2FieldDefinition = new _ObjectType2FieldDefinition("9ca06d69-e733-423a-8ff5-3683c3d90a55");
    public static class _ObjectType2FieldDefinition extends _Object2ObjectAbstract
    {
        protected _ObjectType2FieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromLink = new CIAttribute(this, "FromLink");
        public final CIAttribute ToLink = new CIAttribute(this, "ToLink");
    }

    public static final _Property Property = new _Property("34d52a49-180f-40e0-89e0-b82a5bd3ff3c");
    public static class _Property extends _ElementAbstract
    {
        protected _Property(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Key = new CIAttribute(this, "Key");
        public final CIAttribute ParentElementLink = new CIAttribute(this, "ParentElementLink");
        public final CIAttribute Value = new CIAttribute(this, "Value");
    }

}
