package org.efaps.graphql.ci;

import org.efaps.ci.CIAttribute;
import org.efaps.ci.CIType;

public final class CIGraphQL
{
    public static final _ElementAbstract ElementAbstract = new _ElementAbstract("c0a47097-5d69-4447-974f-dfaf023d219a");
    public static class _ElementAbstract extends CIType
    {
        protected _ElementAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute Company = new CIAttribute(this, "Company");
        public final CIAttribute Name = new CIAttribute(this, "Name");
        public final CIAttribute Description = new CIAttribute(this, "Description");
        public final CIAttribute Created = new CIAttribute(this, "Created");
        public final CIAttribute Creator = new CIAttribute(this, "Creator");
        public final CIAttribute Modified = new CIAttribute(this, "Modified");
        public final CIAttribute Modifier = new CIAttribute(this, "Modifier");
    }

    public static final _ObjectType ObjectType = new _ObjectType("3896c58e-0cd8-46b5-89e8-728520179efa");
    public static class _ObjectType extends _ElementAbstract
    {
        protected _ObjectType(final String _uuid)
        {
            super(_uuid);
        }
    }

    public static final _FieldDefinitionAbstract FieldDefinitionAbstract = new _FieldDefinitionAbstract("a7af940c-0669-449e-9025-d2c12695bf2c");
    public static class _FieldDefinitionAbstract extends _ElementAbstract
    {
        protected _FieldDefinitionAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FieldType = new CIAttribute(this, "FieldType");
    }

    public static final _FieldDefinition FieldDefinition = new _FieldDefinition("d8161807-22e2-4e5d-813d-680b6109b188");
    public static class _FieldDefinition extends _FieldDefinitionAbstract
    {
        protected _FieldDefinition(final String _uuid)
        {
            super(_uuid);
        }

    }

    public static final _EntryPointFieldDefinition EntryPointFieldDefinition = new _EntryPointFieldDefinition("7a5c0ee4-bd08-4a8d-a46e-b58b1a337b64");
    public static class _EntryPointFieldDefinition extends _FieldDefinitionAbstract
    {
        protected _EntryPointFieldDefinition(final String _uuid)
        {
            super(_uuid);
        }
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

    public static final _Object2ObjectAbstract Object2ObjectAbstract = new _Object2ObjectAbstract("302f2d7a-eba6-48f2-909b-c99973c55ba1");
    public static class _Object2ObjectAbstract extends CIType
    {
        protected _Object2ObjectAbstract(final String _uuid)
        {
            super(_uuid);
        }
        public final CIAttribute FromID = new CIAttribute(this, "FromID");
        public final CIAttribute ToID = new CIAttribute(this, "ToID");
        public final CIAttribute Created = new CIAttribute(this, "Created");
        public final CIAttribute Creator = new CIAttribute(this, "Creator");
        public final CIAttribute Modified = new CIAttribute(this, "Modified");
        public final CIAttribute Modifier = new CIAttribute(this, "Modifier");
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

}
