package org.efaps.graphql;

import org.efaps.util.EFapsException;

public class GraphQLServletTest
{

    public void test() throws EFapsException {
        final var graphQL = new EFapsGraphQL();
        graphQL.query("{\n" +
                        "  __schema {\n" +
                        "    types {\n" +
                        "      name\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }


    public void test3() throws EFapsException {
        final var graphQL = new EFapsGraphQL();
        graphQL.query("{\n" +
                        "  __type(name: \"query\") {\n" +
                        "    name\n" +
                        "    fields {\n" +
                        "      name\n" +
                        "      type {\n" +
                        "        name\n" +
                        "        kind\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }


    public void test2() throws EFapsException {
        final var graphQL = new EFapsGraphQL();
        graphQL.query("{\n" +
                        "      products {\n" +
                        "        sku" +
                        "      }\n" +
                        "    }");
    }
}
