package odata.generator;

import com.mongodb.BasicDBObject;
import util.Node;
import util.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 23:43
 */
public class MongodbQueryGenerator {
    static final Map<String, Generator> generateMatchTable = new HashMap<>();
    BasicDBObject mongoDBQuery = new BasicDBObject();

    public MongodbQueryGenerator() {
        if (generateMatchTable.size() == 0) {
            rootLeft rootLeftGenerator = new rootLeft();
            leftRoot leftRootGenerator = new leftRoot();
            RightRootLeft RightRootLeftGenerator = new RightRootLeft();
            leftRootRight leftRootRightGenerator = new leftRootRight();
            simpleLeftRootRight simpleLeftRootRight = new simpleLeftRootRight();
            endsWith endWithGenerator = new endsWith();
            leftRootRightThreeParameters leftRootRightThreeParametersGenerator = new leftRootRightThreeParameters();
            generateMatchTable.put("ceiling", rootLeftGenerator);
            generateMatchTable.put("floor", rootLeftGenerator);
            generateMatchTable.put("round", rootLeftGenerator);
            generateMatchTable.put("not", rootLeftGenerator);
            generateMatchTable.put("trim", leftRootGenerator);
            generateMatchTable.put("tolower", leftRootGenerator);
            generateMatchTable.put("toupper", leftRootGenerator);
            generateMatchTable.put("length", leftRootGenerator);
            generateMatchTable.put("boolparen", rootLeftGenerator);
            generateMatchTable.put("paren", rootLeftGenerator);
            generateMatchTable.put("&&", simpleLeftRootRight);
            generateMatchTable.put("||", simpleLeftRootRight);
            generateMatchTable.put("+", simpleLeftRootRight);
            generateMatchTable.put("-", simpleLeftRootRight);
            generateMatchTable.put("/", simpleLeftRootRight);
            generateMatchTable.put("%", simpleLeftRootRight);
            generateMatchTable.put("*", simpleLeftRootRight);
            generateMatchTable.put("==", simpleLeftRootRight);
            generateMatchTable.put("!=", simpleLeftRootRight);
            generateMatchTable.put("<=", simpleLeftRootRight);
            generateMatchTable.put(">=", simpleLeftRootRight);
            generateMatchTable.put(">", simpleLeftRootRight);
            generateMatchTable.put("<", simpleLeftRootRight);
            generateMatchTable.put("startswith", leftRootRightGenerator);
            generateMatchTable.put("indexof", leftRootRightGenerator);
            generateMatchTable.put("substring2", leftRootRightGenerator);
            generateMatchTable.put("replace", leftRootRightGenerator);
            generateMatchTable.put("concat", leftRootRightGenerator);
            generateMatchTable.put("substringof", RightRootLeftGenerator);
            generateMatchTable.put("month", leftRootGenerator);
            generateMatchTable.put("year", leftRootGenerator);
            generateMatchTable.put("second", leftRootGenerator);
            generateMatchTable.put("minute", leftRootGenerator);
            generateMatchTable.put("hour", leftRootGenerator);
            generateMatchTable.put("day", leftRootGenerator);
            generateMatchTable.put("replace", leftRootRightThreeParametersGenerator);
            generateMatchTable.put("substring3", leftRootRightThreeParametersGenerator);
            generateMatchTable.put("endswith", endWithGenerator);
        }
    }

    class rootLeft extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public rootLeft() {
            replaceTable.put("ceiling", new String[]{"Math.ceil(", ")"});
            replaceTable.put("floor", new String[]{"Math.floor(", ")"});
            replaceTable.put("round", new String[]{"Math.round(", ")"});
            replaceTable.put("not", new String[]{"!(", ")"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder();

            if ("boolparen".equals(root.data.toString())
                    || "paren".equals(root.data.toString())) {
                queryString.append("(").append(generateMongoDBQuery(root.children.get(0)))
                        .append(") ");
            } else {
                queryString = new StringBuilder(parameters[0])
                        .append(generateMongoDBQuery(root.children.get(0)))
                        .append(parameters[1]).append(" ");
            }
            return queryString.toString();
        }
    }

    class leftRoot extends Generator {
        Map<String, String> replaceTable = new HashMap<>();

        public leftRoot() {
            replaceTable.put("length", ".length ");
            replaceTable.put("tolower", ".toLowerCase() ");
            replaceTable.put("toupper", ".toUpperCase() ");
            replaceTable.put("trim", ".trim() ");
            replaceTable.put("day", ".getDate() ");
            replaceTable.put("month", ".getMonth() + 1 ");
            replaceTable.put("year", ".getFullYear() ");
            replaceTable.put("minute", ".getMinutes() ");
            replaceTable.put("second", ".getSeconds() ");
            replaceTable.put("hour", ".getHours() ");
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String queryString;
            String parameter = replaceTable.get(root.data.toString());
            queryString = generateMongoDBQuery(root.children.get(0)) + parameter;
            return queryString;
        }

    }

    class leftRootRight extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public leftRootRight() {
            replaceTable.put("concat", new String[]{".concat(", ")"});
            replaceTable.put("substring", new String[]{".substring(", ")"});
            replaceTable.put("indexof", new String[]{".indexOf(", ")"});
            replaceTable.put("startswith", new String[]{".indexOf(", ") == 0"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String queryString;
            String[] parameters = replaceTable.get(root.data.toString());
            queryString = generateMongoDBQuery(root.children.get(0)) + parameters[0]
                    + generateMongoDBQuery(root.children.get(1))
                    + parameters[1];
            return queryString;
        }
    }

    class simpleLeftRootRight extends Generator {
        @Override
        public String generateQueryString(Node<Object> root) {
            String queryString;
            queryString = generateMongoDBQuery(root.children.get(0)) + " " + root.data.toString()
                    + " " + generateMongoDBQuery(root.children.get(1));
            return queryString;
        }
    }

    class RightRootLeft extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public RightRootLeft() {
            replaceTable.put("substringof", new String[]{".indexOf(", ") != -1"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            String queryString = generateMongoDBQuery(root.children.get(1)) + parameters[0]
                    + generateMongoDBQuery(root.children.get(0))
                    + parameters[1];
            return queryString;
        }
    }

    class endsWith extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public endsWith() {
            replaceTable.put("endswith", new String[]{".indexOf(", ") == ", ".length - ", ".length"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            String queryString = generateMongoDBQuery(root.children.get(0)) + parameters[0]
                    + generateMongoDBQuery(root.children.get(1))
                    + parameters[1] + generateMongoDBQuery(root.children.get(0)) + parameters[2]
                    + generateMongoDBQuery(root.children.get(1)) + parameters[3];
            return queryString;
        }
    }

    class leftRootRightThreeParameters extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public leftRootRightThreeParameters() {
            replaceTable.put("substring", new String[]{".substring(", ",", ")"});
            replaceTable.put("replace", new String[]{".replace(", ",", ")"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            String queryString = generateMongoDBQuery(root.children.get(0)) + parameters[0]
                    + generateMongoDBQuery(root.children.get(1))
                    + parameters[1] + generateMongoDBQuery(root.children.get(2)) + parameters[2];
            return queryString;
        }
    }

    String generateMongoDBQuery(Node<Object> root) {
        String rootDataValue = root.data.toString();
        Generator gen;
        if (rootDataValue.equals("substring")) {
            String childrenNum = Integer.toString(root.children.size());
            gen = generateMatchTable.get(rootDataValue + childrenNum);
            return gen.generateQueryString(root);
        } else {
            if (generateMatchTable.containsKey(rootDataValue)) {
                gen = generateMatchTable.get(rootDataValue);
                return gen.generateQueryString(root);
            } else {
                return rootDataValue;
            }
        }
    }

    public BasicDBObject generateMongoDBQuery(Tree AST) {
        String finString = generateMongoDBQuery(AST.root);
        mongoDBQuery.append("$where", finString);
        return mongoDBQuery;
    }
}
