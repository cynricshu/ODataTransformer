package transformer;

import com.mongodb.BasicDBObject;
import org.odata4j.expression.*;
import org.odata4j.repack.org.apache.commons.codec.binary.Hex;
import transformer.ODataFilterToMongoDBTransformer.MongoDBQueryGenerator.generator;
import util.Node;
import util.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:37
 */
public class ODataFilterToMongoDBTransformer implements DataTransformer {
    final static Map<String, generator> generateMatchTable = new HashMap<>();

    @Override
    public BasicDBObject transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        MongoDBQueryGenerator visitor = new MongoDBQueryGenerator();
        expression.visit(visitor);

        BasicDBObject query = visitor.generateMongoDBQuery();
        return query;
    }

    @Override
    public String getSourceTypeName() {
        return "ODataFilter";
    }

    @Override
    public String getDistTypeName() {
        return "MongoDB";
    }

    class MongoDBQueryGenerator implements org.odata4j.expression.ExpressionVisitor {
        BasicDBObject query = new BasicDBObject();
        StringBuilder anotherQueryString = new StringBuilder();
        Tree<Object> AST = new Tree("");

        Node<Object> currentNode = AST.root;

        boolean simpleOperation = true;

        public MongoDBQueryGenerator() {
            if (generateMatchTable.size() == 0) {
                rootLeft rootLeftGenerator = new rootLeft();
                leftRoot leftRootGenerator = new leftRoot();
                RightRootLeft RightRootLeftGenerator = new RightRootLeft();
                leftRootRight leftRootRightGenerator = new leftRootRight();
                endsWith endWithGenerator = new endsWith();
                leftRootRightThreeParameters leftRootRightThreeParametersGenerator = new leftRootRightThreeParameters();
                generateMatchTable.put("ceiling,1", rootLeftGenerator);
                generateMatchTable.put("floor,1", rootLeftGenerator);
                generateMatchTable.put("round,1", rootLeftGenerator);
                generateMatchTable.put("not,1", rootLeftGenerator);
                generateMatchTable.put("trim,1", leftRootGenerator);
                generateMatchTable.put("tolower,1", leftRootGenerator);
                generateMatchTable.put("boolparen,1", leftRootGenerator);
                generateMatchTable.put("toupper,1", leftRootGenerator);
                generateMatchTable.put("length,1", leftRootGenerator);
                generateMatchTable.put("paren,1", leftRootRightGenerator);
                generateMatchTable.put("and,2", leftRootRightGenerator);
                generateMatchTable.put("or,2", leftRootRightGenerator);
                generateMatchTable.put("add,2", leftRootRightGenerator);
                generateMatchTable.put("sub,2", leftRootRightGenerator);
                generateMatchTable.put("div,2", leftRootRightGenerator);
                generateMatchTable.put("mod,2", leftRootRightGenerator);
                generateMatchTable.put("mul,2", leftRootRightGenerator);
                generateMatchTable.put("eq,2", leftRootRightGenerator);
                generateMatchTable.put("ne,2", leftRootRightGenerator);
                generateMatchTable.put("le,2", leftRootRightGenerator);
                generateMatchTable.put("ge,2", leftRootRightGenerator);
                generateMatchTable.put("gt,2", leftRootRightGenerator);
                generateMatchTable.put("lt,2", leftRootRightGenerator);
                generateMatchTable.put("startswith,2", leftRootRightGenerator);
                generateMatchTable.put("indexof,2", leftRootRightGenerator);
                generateMatchTable.put("substring,2", leftRootRightGenerator);
                generateMatchTable.put("replace,2", leftRootRightGenerator);
                generateMatchTable.put("concat,2", leftRootRightGenerator);
                generateMatchTable.put("substringof,2", RightRootLeftGenerator);
                generateMatchTable.put("month,1", leftRootGenerator);
                generateMatchTable.put("year,1", leftRootGenerator);
                generateMatchTable.put("second,1", leftRootGenerator);
                generateMatchTable.put("minute,1", leftRootGenerator);
                generateMatchTable.put("hour,1", leftRootGenerator);
                generateMatchTable.put("day,1", leftRootGenerator);
                generateMatchTable.put("replace,3", leftRootRightThreeParametersGenerator);
                generateMatchTable.put("substring,3", leftRootRightThreeParametersGenerator);
                generateMatchTable.put("endswith,2", endWithGenerator);
            }
        }

        abstract class generator {
            public abstract String generateQueryString(Node<Object> root);
        }

        class rootLeft extends generator {
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
                String queryString = parameters[0]
                        + generateMongoDBQuery(root.children.get(0))
                        + parameters[1];
                return queryString;
            }
        }

        class leftRoot extends generator {
            Map<String, String> replaceTable = new HashMap<>();

            public leftRoot() {
                replaceTable.put("length", ".length");
                replaceTable.put("tolower", ".toLowerCase()");
                replaceTable.put("toUpper", ".toUpperCase()");
                replaceTable.put("trim", ".replace(' ','')");
                replaceTable.put("day", ".getDate() ");
                replaceTable.put("month", ".getMonth() + 1 ");
                replaceTable.put("year", ".getFullYear() ");
                replaceTable.put("minute", ".getMinutes() ");
                replaceTable.put("second", ".getSeconds() ");
                replaceTable.put("hour", ".getHours() ");
                replaceTable.put("boolparen", "");
            }

            @Override
            public String generateQueryString(Node<Object> root) {

                String parameter = replaceTable.get(root.data.toString());
                String queryString = generateMongoDBQuery(root.children.get(0)) + parameter;
                return queryString;
            }

        }

        class leftRootRight extends generator {
            Map<String, String[]> replaceTable = new HashMap<>();

            public leftRootRight() {
                replaceTable.put("concat", new String[]{"(", ")"});
                replaceTable.put("substring", new String[]{".substring(", ")"});
                replaceTable.put("indexof", new String[]{".indexOf(", ")"});
                replaceTable.put("startswith", new String[]{".indexOf(", ") == 0"});
                replaceTable.put("add", new String[]{" + ", ""});
                replaceTable.put("mul", new String[]{" * ", ""});
                replaceTable.put("mod", new String[]{" % ", ""});
                replaceTable.put("div", new String[]{" / ", ""});
                replaceTable.put("sub", new String[]{" - ", ""});
                replaceTable.put("and", new String[]{" && ", ""});
                replaceTable.put("or", new String[]{" || ", ""});
                replaceTable.put("eq", new String[]{" == ", ""});
                replaceTable.put("ne", new String[]{" != ", ""});
                replaceTable.put("gt", new String[]{" > ", ""});
                replaceTable.put("ge", new String[]{" >= ", ""});
                replaceTable.put("lt", new String[]{" < ", ""});
                replaceTable.put("le", new String[]{" <= ", ""});
                replaceTable.put("paren", new String[]{"", ""});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String queryString;
                String[] parameters = replaceTable.get(root.data.toString());
                if ("paren".equals(root.data.toString())) {
                    queryString = "(" + generateMongoDBQuery(root.children.get(0)) + ")";
                } else {
                    queryString = generateMongoDBQuery(root.children.get(0)) + parameters[0]
                            + generateMongoDBQuery(root.children.get(1))
                            + parameters[1];
                }
                return queryString;
            }
        }

        class RightRootLeft extends generator {
            Map<String, String[]> replaceTable = new HashMap<>();

            public RightRootLeft() {
                replaceTable.put("substringof", new String[]{".indexOf(", ") != -1"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = "(" + generateMongoDBQuery(root.children.get(1)) + parameters[0]
                        + generateMongoDBQuery(root.children.get(0))
                        + parameters[1] + ")";
                return queryString;
            }
        }

        class endsWith extends generator {
            Map<String, String[]> replaceTable = new HashMap<>();

            public endsWith() {
                replaceTable.put("endswith", new String[]{".indexOf(", ") == ", ".length - ", ".length"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = "(" + generateMongoDBQuery(root.children.get(0)) + parameters[0]
                        + generateMongoDBQuery(root.children.get(1))
                        + parameters[1] + generateMongoDBQuery(root.children.get(0)) + parameters[2]
                        + generateMongoDBQuery(root.children.get(1)) + parameters[3] + ")";
                return queryString;
            }
        }

        class leftRootRightThreeParameters extends generator {
            Map<String, String[]> replaceTable = new HashMap<>();

            public leftRootRightThreeParameters() {
                replaceTable.put("substring", new String[]{".substring(", ",", ")"});
                replaceTable.put("replace", new String[]{".replace(", ",", ")"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = generateMongoDBQuery(root.children.get(1)) + parameters[0]
                        + generateMongoDBQuery(root.children.get(1))
                        + parameters[1] + generateMongoDBQuery(root.children.get(2)) + parameters[2];
                return queryString;
            }
        }

        String generateMongoDBQuery(Node<Object> root) {
            String rootDataValue = root.data.toString();
            String childrenNum = Integer.toString(root.children.size());
            if (generateMatchTable.containsKey(rootDataValue + "," + childrenNum)) {
                generator gen = generateMatchTable.get(rootDataValue + ","
                        + childrenNum);
                return gen.generateQueryString(root);
            } else
                return rootDataValue;
        }

        BasicDBObject generateMongoDBQuery() {
            String finString = generateMongoDBQuery(AST.root);
            query.append("$where", finString);
            return query;
        }

        public Tree<Object> getAST() {
            return AST;
        }

        @Override
        public void afterDescend() {
            if (currentNode.parent != null) {
                currentNode = currentNode.parent;
            }
        }

        @Override
        public void beforeDescend() {
            Node<Object> newNode = new Node<>();
            newNode.parent = currentNode;
            currentNode.children.add(newNode);
            currentNode = newNode;
        }

        @Override
        public void betweenDescend() {
            Node<Object> newNode = new Node<>();
            newNode.parent = currentNode.parent;
            currentNode.parent.children.add(newNode);
            currentNode = newNode;

        }

        @Override
        public void visit(String arg0) {

        }

        @Override
        public void visit(OrderByExpression arg0) {

            currentNode.data = "orderBy";

        }

        @Override
        public void visit(OrderByExpression.Direction arg0) {

            currentNode.data = (arg0 == OrderByExpression.Direction.ASCENDING ? "asc" : "desc");
        }

        @Override
        public void visit(AddExpression arg0) {

            currentNode.data = "add";
            simpleOperation = false;
        }

        @Override
        public void visit(AndExpression arg0) {

            currentNode.data = "and";
        }

        @Override
        public void visit(BooleanLiteral arg0) {

            currentNode.data = arg0.getValue();

        }

        @Override
        public void visit(CastExpression arg0) {

            currentNode.data = "cast";

        }

        @Override
        public void visit(ConcatMethodCallExpression arg0) {

            currentNode.data = "concat";

        }

        @Override
        public void visit(DateTimeLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(DateTimeOffsetLiteral arg0) {

            currentNode.data = "'" + arg0.getValue() + "'";
        }

        @Override
        public void visit(DecimalLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(DivExpression arg0) {

            currentNode.data = "div";
            simpleOperation = false;
        }

        @Override
        public void visit(EndsWithMethodCallExpression arg0) {

            currentNode.data = "endswith";

        }

        @Override
        public void visit(EntitySimpleProperty arg0) {

            if (arg0.getPropertyName().contains("/"))
                currentNode.data = "this."
                        + arg0.getPropertyName().replace("/", ".");
            else
                currentNode.data = "this." + arg0.getPropertyName();

        }

        @Override
        public void visit(EqExpression arg0) {
            currentNode.data = "eq";

        }

        @Override
        public void visit(GeExpression arg0) {

            currentNode.data = "ge";

        }

        @Override
        public void visit(GtExpression arg0) {

            currentNode.data = "gt";

        }

        @Override
        public void visit(GuidLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(BinaryLiteral arg0) {

            currentNode.data = Hex.encodeHexString(arg0.getValue());

        }

        @Override
        public void visit(ByteLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(SByteLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IndexOfMethodCallExpression arg0) {

            currentNode.data = "indexof";

        }

        @Override
        public void visit(SingleLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(DoubleLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IntegralLiteral arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(Int64Literal arg0) {

            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IsofExpression arg0) {

            currentNode.data = "isof";

        }

        @Override
        public void visit(LeExpression arg0) {

            currentNode.data = "le";
        }

        @Override
        public void visit(LengthMethodCallExpression arg0) {

            currentNode.data = "length";

        }

        @Override
        public void visit(LtExpression arg0) {

            currentNode.data = "lt";
        }

        @Override
        public void visit(ModExpression arg0) {

            currentNode.data = "mod";
            simpleOperation = false;
        }

        @Override
        public void visit(MulExpression arg0) {

            currentNode.data = "mul";
            simpleOperation = false;
        }

        @Override
        public void visit(NeExpression arg0) {

            currentNode.data = "ne";
        }

        @Override
        public void visit(NegateExpression arg0) {

            currentNode.data = "negate";
        }

        @Override
        public void visit(NotExpression arg0) {

            currentNode.data = "not";
        }

        @Override
        public void visit(NullLiteral arg0) {

            currentNode.data = "null";
        }

        @Override
        public void visit(OrExpression arg0) {

            currentNode.data = "or";
        }

        @Override
        public void visit(ParenExpression arg0) {

            currentNode.data = "paren";
        }

        @Override
        public void visit(BoolParenExpression arg0) {

            currentNode.data = "boolparen";
        }

        @Override
        public void visit(ReplaceMethodCallExpression arg0) {

            currentNode.data = "replace";

        }

        @Override
        public void visit(StartsWithMethodCallExpression arg0) {

            currentNode.data = "startswith";

        }

        @Override
        public void visit(StringLiteral arg0) {
            currentNode.data = "'" + arg0.getValue() + "'";
        }

        @Override
        public void visit(SubExpression arg0) {

            currentNode.data = "sub";
        }

        @Override
        public void visit(SubstringMethodCallExpression arg0) {

            currentNode.data = "substring";

        }

        @Override
        public void visit(SubstringOfMethodCallExpression arg0) {

            currentNode.data = "substringof";

        }

        @Override
        public void visit(TimeLiteral arg0) {

            currentNode.data = arg0.getValue().toString(
                    ExpressionParser.TIME_FORMATTER);
        }

        @Override
        public void visit(ToLowerMethodCallExpression arg0) {

            currentNode.data = "tolower";

        }

        @Override
        public void visit(ToUpperMethodCallExpression arg0) {

            currentNode.data = "toupper";

        }

        @Override
        public void visit(TrimMethodCallExpression arg0) {

            currentNode.data = "trim";

        }

        @Override
        public void visit(YearMethodCallExpression arg0) {

            currentNode.data = "year";
        }

        @Override
        public void visit(MonthMethodCallExpression arg0) {

            currentNode.data = "month";
        }

        @Override
        public void visit(DayMethodCallExpression arg0) {

            currentNode.data = "day";
        }

        @Override
        public void visit(HourMethodCallExpression arg0) {

            currentNode.data = "hour";
        }

        @Override
        public void visit(MinuteMethodCallExpression arg0) {

            currentNode.data = "minute";
        }

        @Override
        public void visit(SecondMethodCallExpression arg0) {

            currentNode.data = "second";
        }

        @Override
        public void visit(RoundMethodCallExpression arg0) {

            currentNode.data = "round";

        }

        @Override
        public void visit(FloorMethodCallExpression arg0) {

            currentNode.data = "floor";

        }

        @Override
        public void visit(CeilingMethodCallExpression arg0) {

            currentNode.data = "ceiling";
        }

        @Override
        public void visit(AggregateAnyFunction arg0) {

            if (arg0.getVariable() != null) {
                currentNode.data = arg0.getVariable();
            } else {
                currentNode.data = "";
            }
        }

        @Override
        public void visit(AggregateAllFunction arg0) {
            currentNode.data = arg0.getVariable();
        }

    }
}
