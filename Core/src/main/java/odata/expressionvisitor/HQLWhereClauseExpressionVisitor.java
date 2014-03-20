package odata.expressionvisitor;

import util.Node;
import util.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 14:37
 */
public class HQLWhereClauseExpressionVisitor extends CommonExpressionVisitor {
    StringBuilder hqlQuery = new StringBuilder();

    public HQLWhereClauseExpressionVisitor() {
        if (generateMatchTable.size() == 0) {
            rootLeft rootLeftGenerator = new rootLeft();
            substringTwoParameter substringTwoParameterGenerator = new substringTwoParameter();
            concatGenertor rootRightGenerator = new concatGenertor();
            leftRoot leftRootGenerator = new leftRoot();
            rootRightLeft rootRightLeftGenerator = new rootRightLeft();
            rootLeftRight rootLeftRightGenerator = new rootLeftRight();
            RightRootLeft RightRootLeftGenerator = new RightRootLeft();
            leftRootRight leftRootRightGenerator = new leftRootRight();
            endsWith endWithGenerator = new endsWith();
            leftRootRightThreeParameters leftRootRightThreeParametersGenerator = new leftRootRightThreeParameters();
            generateMatchTable.put("ceiling", rootLeftGenerator);
            generateMatchTable.put("floor", rootLeftGenerator);
            generateMatchTable.put("round", rootLeftGenerator);
            generateMatchTable.put("not", rootLeftGenerator);
            generateMatchTable.put("trim", rootLeftGenerator);
            generateMatchTable.put("tolower", rootLeftGenerator);
            generateMatchTable.put("boolparen", leftRootGenerator);
            generateMatchTable.put("paren", leftRootGenerator);
            generateMatchTable.put("toupper", rootLeftGenerator);
            generateMatchTable.put("length", rootLeftGenerator);
            generateMatchTable.put("and", leftRootRightGenerator);
            generateMatchTable.put("or", leftRootRightGenerator);
            generateMatchTable.put("add", leftRootRightGenerator);
            generateMatchTable.put("sub", leftRootRightGenerator);
            generateMatchTable.put("div", leftRootRightGenerator);
            generateMatchTable.put("mod", leftRootRightGenerator);
            generateMatchTable.put("mul", leftRootRightGenerator);
            generateMatchTable.put("eq", leftRootRightGenerator);
            generateMatchTable.put("ne", leftRootRightGenerator);
            generateMatchTable.put("le", leftRootRightGenerator);
            generateMatchTable.put("ge", leftRootRightGenerator);
            generateMatchTable.put("gt", leftRootRightGenerator);
            generateMatchTable.put("lt", leftRootRightGenerator);
            generateMatchTable.put("startswith", rootLeftRightGenerator);
            generateMatchTable.put("endswith", endWithGenerator);
            generateMatchTable.put("indexof", rootRightLeftGenerator);
            generateMatchTable.put("substring2",
                    substringTwoParameterGenerator);
            generateMatchTable.put("substring3",
                    leftRootRightThreeParametersGenerator);
            generateMatchTable.put("replace", leftRootRightGenerator);
            generateMatchTable.put("concat", rootRightGenerator);
            generateMatchTable.put("substringof", RightRootLeftGenerator);
            generateMatchTable.put("month", rootLeftGenerator);
            generateMatchTable.put("year", rootLeftGenerator);
            generateMatchTable.put("second", rootLeftGenerator);
            generateMatchTable.put("minute", rootLeftGenerator);
            generateMatchTable.put("hour", rootLeftGenerator);
            generateMatchTable.put("day", rootLeftGenerator);
            generateMatchTable.put("replace",
                    leftRootRightThreeParametersGenerator);

        }
    }

    class substringTwoParameter extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public substringTwoParameter() {
            replaceTable.put("substring", new String[]{"substring(", ")"});

        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(parameters[0])
                    .append(generateHQL(root.children.get(0))).append(", ")
                    .append(generateHQL(root.children.get(1))).append("+1")
                    .append(", length(")
                    .append(generateHQL(root.children.get(0))).append(")-")
                    .append(generateHQL(root.children.get(1)))
                    .append(parameters[1]).append(" ");
            return queryString.toString();
        }

    }

    class rootRightLeft extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public rootRightLeft() {
            replaceTable.put("indexof", new String[]{"locate(", ")-1"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(parameters[0])
                    .append(generateHQL(root.children.get(1))).append(", ")
                    .append(generateHQL(root.children.get(0)))
                    .append(parameters[1]).append(" ");
            return queryString.toString();
        }

    }

    class concatGenertor extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public concatGenertor() {
            replaceTable.put("concat", new String[]{"concat(", ")"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(parameters[0])
                    .append(generateHQL(root.children.get(0))).append(", ")
                    .append(generateHQL(root.children.get(1)))
                    .append(parameters[1]).append(" ");
            return queryString.toString();
        }

    }

    class rootLeftRight extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public rootLeftRight() {
            replaceTable.put("startswith", new String[]{"locate(", ") = 1"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder().append(parameters[0])
                    .append(generateHQL(root.children.get(1))).append(",")
                    .append(generateHQL(root.children.get(0)))
                    .append(parameters[1]).append(" ");
            return queryString.toString();
        }
    }

    class rootLeft extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public rootLeft() {
            replaceTable.put("ceiling", new String[]{"ceiling(", ")"});
            replaceTable.put("floor", new String[]{"floor(", ")"});
            replaceTable.put("round", new String[]{"round(", ", 0)"});
            replaceTable.put("not", new String[]{"not (", ")"});
            replaceTable.put("tolower", new String[]{"lower(", ")"});
            replaceTable.put("toupper", new String[]{"upper(", ")"});
            replaceTable.put("trim", new String[]{"trim(", ")"});
            replaceTable.put("length", new String[]{"length(", ")"});

            replaceTable.put("day", new String[]{"day(", ")"});
            replaceTable.put("month", new String[]{"month(", ")"});
            replaceTable.put("year", new String[]{"year(", ")"});
            replaceTable.put("minute", new String[]{"minute(", ")"});
            replaceTable.put("second", new String[]{"second(", ")"});
            replaceTable.put("hour", new String[]{"hour(", ")"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(parameters[0])
                    .append(generateHQL(root.children.get(0)))
                    .append(parameters[1]).append(" ");
            return queryString.toString();
        }
    }

    class leftRoot extends Generator {
        Map<String, String> replaceTable = new HashMap<>();

        public leftRoot() {
            replaceTable.put("boolparen", "");
            replaceTable.put("paren", "");
        }

        @Override
        public String generateQueryString(Node<Object> root) {

            String parameter = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder();

            if ("boolparen".equals(root.data.toString())
                    || "paren".equals(root.data.toString())) {
                queryString.append("(").append(generateHQL(root.children.get(0)))
                        .append(parameter).append(") ");
            } else {
                queryString.append(generateHQL(root.children.get(0))).append(parameter).append(" ");
            }
            return queryString.toString();
        }
    }

    class leftRootRight extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public leftRootRight() {
            replaceTable.put("add", new String[]{"+", ""});
            replaceTable.put("mul", new String[]{"*", ""});
            replaceTable.put("mod", new String[]{"%", ""});
            replaceTable.put("div", new String[]{"/", ""});
            replaceTable.put("sub", new String[]{"-", ""});
            replaceTable.put("and", new String[]{"and", ""});
            replaceTable.put("or", new String[]{"or", ""});
            replaceTable.put("eq", new String[]{"=", ""});
            replaceTable.put("ne", new String[]{"!=", ""});
            replaceTable.put("gt", new String[]{">", ""});
            replaceTable.put("ge", new String[]{">=", ""});
            replaceTable.put("lt", new String[]{"<", ""});
            replaceTable.put("le", new String[]{"<=", ""});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder();

            if (root.data.toString().equals("eq")
                    && (root.children.get(0).data.toString().equals("startswith")
                    || root.children.get(0).data.toString().equals("endswith")
                    || root.children.get(0).data.toString().equals("substringof"))) {
                if (root.children.get(1).data.toString().equals("false")) {
                    if (root.children.get(0).data.toString().equals("substringof")) {
                        queryString.append(generateHQL(root.children.get(0)).replace(" != ", " = "));
                    } else {
                        queryString.append(generateHQL(root.children.get(0)).replace(" = ", " != "));
                    }
                } else {
                    queryString.append(generateHQL(root.children.get(0))).append(" ");
                }
            } else {
                queryString.append(generateHQL(root.children.get(0))).append(parameters[0])
                        .append(generateHQL(root.children.get(1)));
            }
            return " (" + queryString + ") ";
        }
    }

    class RightRootLeft extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public RightRootLeft() {
            replaceTable.put("substringof", new String[]{"locate(", ") != 0"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(parameters[0])
                    .append(generateHQL(root.children.get(0))).append(", ")
                    .append(generateHQL(root.children.get(1)))
                    .append(parameters[1]);
            return queryString.toString();
        }
    }

    class endsWith extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public endsWith() {
            replaceTable.put("endswith", new String[]{"substring(", ", length(",
                    ") - length(", ") + 1) = "});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            StringBuilder queryString = new StringBuilder(" (").append(parameters[0])
                    .append(generateHQL(root.children.get(0)))
                    .append(parameters[1]).append(generateHQL(root.children.get(0)))
                    .append(parameters[2]).append(generateHQL(root.children.get(1)))
                    .append(parameters[3]).append(generateHQL(root.children.get(1)))
                    .append(") ");
            return queryString.toString();
        }
    }

    class leftRootRightThreeParameters extends Generator {
        Map<String, String[]> replaceTable = new HashMap<>();

        public leftRootRightThreeParameters() {
            replaceTable.put("substring", new String[]{"substring(", ", ",
                    ")"});
            replaceTable.put("replace",
                    new String[]{"replace(", ", ", ")"});
        }

        @Override
        public String generateQueryString(Node<Object> root) {
            String[] parameters = replaceTable.get(root.data.toString());
            if (root.data.toString().equals("replace")) {
                StringBuilder queryString = new StringBuilder(parameters[0])
                        .append(generateHQL(root.children.get(0))).append(", ")
                        .append(generateHQL(root.children.get(1)))
                        .append(parameters[1])
                        .append(generateHQL(root.children.get(2)))
                        .append(parameters[2]).append(" ");
                return queryString.toString();
            } else {
                StringBuilder queryString = new StringBuilder(parameters[0])
                        .append(generateHQL(root.children.get(0))).append(", ")
                        .append(generateHQL(root.children.get(1))).append(" + 1")
                        .append(parameters[1])
                        .append(generateHQL(root.children.get(2)))
                        .append(parameters[2]).append(" ");
                return queryString.toString();
            }
        }
    }

    String generateHQL(Node<Object> root) {
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

    public String generateHQLWhereClause() {
        String finString = generateHQL(AST.root);
        hqlQuery.append(finString);

        return hqlQuery.toString();

    }

    public Tree<Object> getAST() {
        return AST;
    }


}
