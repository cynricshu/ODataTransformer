package transformer;

import transformer.ODataFilterToHQLWhereClauseTransformer.HQLWhereClauseExpressionVisitor.generator;
import org.odata4j.expression.AddExpression;
import org.odata4j.expression.AggregateAllFunction;
import org.odata4j.expression.AggregateAnyFunction;
import org.odata4j.expression.AndExpression;
import org.odata4j.expression.BinaryLiteral;
import org.odata4j.expression.BoolParenExpression;
import org.odata4j.expression.BooleanLiteral;
import org.odata4j.expression.ByteLiteral;
import org.odata4j.expression.CastExpression;
import org.odata4j.expression.CeilingMethodCallExpression;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ConcatMethodCallExpression;
import org.odata4j.expression.DateTimeLiteral;
import org.odata4j.expression.DateTimeOffsetLiteral;
import org.odata4j.expression.DayMethodCallExpression;
import org.odata4j.expression.DecimalLiteral;
import org.odata4j.expression.DivExpression;
import org.odata4j.expression.DoubleLiteral;
import org.odata4j.expression.EndsWithMethodCallExpression;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.EqExpression;
import org.odata4j.expression.ExpressionParser;
import org.odata4j.expression.ExpressionVisitor;
import org.odata4j.expression.FloorMethodCallExpression;
import org.odata4j.expression.GeExpression;
import org.odata4j.expression.GtExpression;
import org.odata4j.expression.GuidLiteral;
import org.odata4j.expression.HourMethodCallExpression;
import org.odata4j.expression.IndexOfMethodCallExpression;
import org.odata4j.expression.Int64Literal;
import org.odata4j.expression.IntegralLiteral;
import org.odata4j.expression.IsofExpression;
import org.odata4j.expression.LeExpression;
import org.odata4j.expression.LengthMethodCallExpression;
import org.odata4j.expression.LtExpression;
import org.odata4j.expression.MinuteMethodCallExpression;
import org.odata4j.expression.ModExpression;
import org.odata4j.expression.MonthMethodCallExpression;
import org.odata4j.expression.MulExpression;
import org.odata4j.expression.NeExpression;
import org.odata4j.expression.NegateExpression;
import org.odata4j.expression.NotExpression;
import org.odata4j.expression.NullLiteral;
import org.odata4j.expression.OrExpression;
import org.odata4j.expression.OrderByExpression;
import org.odata4j.expression.OrderByExpression.Direction;
import org.odata4j.expression.ParenExpression;
import org.odata4j.expression.ReplaceMethodCallExpression;
import org.odata4j.expression.RoundMethodCallExpression;
import org.odata4j.expression.SByteLiteral;
import org.odata4j.expression.SecondMethodCallExpression;
import org.odata4j.expression.SingleLiteral;
import org.odata4j.expression.StartsWithMethodCallExpression;
import org.odata4j.expression.StringLiteral;
import org.odata4j.expression.SubExpression;
import org.odata4j.expression.SubstringMethodCallExpression;
import org.odata4j.expression.SubstringOfMethodCallExpression;
import org.odata4j.expression.TimeLiteral;
import org.odata4j.expression.ToLowerMethodCallExpression;
import org.odata4j.expression.ToUpperMethodCallExpression;
import org.odata4j.expression.TrimMethodCallExpression;
import org.odata4j.expression.YearMethodCallExpression;
import org.odata4j.repack.org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ODataFilterToHQLWhereClauseTransformer implements
        DataTransformer {

    static final Map<String, generator> generateMatchTable = new HashMap<String, generator>();

    @Override
    public String transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        HQLWhereClauseExpressionVisitor visitor = new HQLWhereClauseExpressionVisitor();
        // PrintExpressionVisitor visitor = new PrintExpressionVisitor();
        expression.visit(visitor);

        // generateMongodb mongdb=new generateMongodb();
        String hqlWhereClause = visitor.generateHQLWhereClause();
        // mongdb.generateMongoDBQuery(visitor.getAST(),visitor.simpleOperation);
        return hqlWhereClause;
    }

    @Override
    public String getSourceTypeName() {
        return "ODataFilter";

    }

    @Override
    public String getDistTypeName() {
        return "HQLWhereClause";

    }

    class HQLWhereClauseExpressionVisitor implements ExpressionVisitor {
        // private Tree.Node<String> treeCurrentNode=AST.root;
        StringBuilder query = new StringBuilder();
        StringBuilder anotherQueryString = new StringBuilder();
        Tree<Object> AST = new Tree("");

        Node<Object> currentNode = AST.root;

        boolean boolExpression = false;

        boolean simpleOperation = true;

        class Tree<T> {

            public Node<T> root;

            public Tree(T rootData) {
                root = new Node<T>();
                root.data = rootData;
                root.children = new ArrayList<Node<T>>();
            }
        }

        class Node<T> {
            public T data;
            public Node<T> parent;
            public List<Node<T>> children = new ArrayList<Node<T>>();
            public StringBuilder QueryObject = new StringBuilder();
            // public StringBuilder jsString=new StringBuilder();
        }


        public HQLWhereClauseExpressionVisitor() {
            if (generateMatchTable.size() == 0) {
                rootLeft rootLeftGenerator = new rootLeft();
                substringTwoParameter substringTwoParameterGenerator = new substringTwoParameter();
                concatGenertor rootRightGenerator = new concatGenertor();
                timeFunction timeFunctionGenerator = new timeFunction();
                leftRoot leftRootGenerator = new leftRoot();
                rootRightLeft rootRightLeftGenerator = new rootRightLeft();
                rootLeftRight rootLeftRightGenerator = new rootLeftRight();
                RightRootLeft RightRootLeftGenerator = new RightRootLeft();
                leftRootRight leftRootRightGenerator = new leftRootRight();
                endsWith endWithGenerator = new endsWith();
                leftRootRightThreeParameters leftRootRightThreeParametersGenerator = new leftRootRightThreeParameters();
                generateMatchTable.put("ceiling,1", rootLeftGenerator);
                generateMatchTable.put("floor,1", rootLeftGenerator);
                generateMatchTable.put("round,1", rootLeftGenerator);
                generateMatchTable.put("not,1", rootLeftGenerator);
                generateMatchTable.put("trim,1", rootLeftGenerator);
                generateMatchTable.put("tolower,1", rootLeftGenerator);
                generateMatchTable.put("boolparen,1", leftRootGenerator);
                generateMatchTable.put("toupper,1", rootLeftGenerator);
                generateMatchTable.put("length,1", timeFunctionGenerator);
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
                generateMatchTable.put("startswith,2", rootLeftRightGenerator);
                generateMatchTable.put("endswith,2", endWithGenerator);
                generateMatchTable.put("indexof,2", rootRightLeftGenerator);
                generateMatchTable.put("substring,2",
                        substringTwoParameterGenerator);
                generateMatchTable.put("replace,2", leftRootRightGenerator);
                generateMatchTable.put("concat,2", rootRightGenerator);
                generateMatchTable.put("substringof,2", RightRootLeftGenerator);
                generateMatchTable.put("month,1", timeFunctionGenerator);
                generateMatchTable.put("year,1", timeFunctionGenerator);
                generateMatchTable.put("second,1", timeFunctionGenerator);
                generateMatchTable.put("minute,1", timeFunctionGenerator);
                generateMatchTable.put("hour,1", timeFunctionGenerator);
                generateMatchTable.put("day,1", timeFunctionGenerator);
                generateMatchTable.put("replace,3",
                        leftRootRightThreeParametersGenerator);
                generateMatchTable.put("substring,3",
                        leftRootRightThreeParametersGenerator);
            }
        }

        abstract class generator {
            public abstract String generateQueryString(Node<Object> root);
        }

        class substringTwoParameter extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public substringTwoParameter() {
                replaceTable.put("substring", new String[]{"substring(", ")"});

            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = parameters[0]
                        + generateMongoDBQuery(root.children.get(0)) + ", "
                        + generateMongoDBQuery(root.children.get(1)) + "+1"
                        + ", length("
                        + generateMongoDBQuery(root.children.get(0)) + ")-" + generateMongoDBQuery(root.children.get(1))
                        + parameters[1] + " ";
                return queryString;
            }

        }

        class rootRightLeft extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public rootRightLeft() {
                replaceTable.put("indexof", new String[]{"charindex(", ")-1"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = " " + parameters[0]
                        + generateMongoDBQuery(root.children.get(1)) + ", "
                        + generateMongoDBQuery(root.children.get(0))
                        + parameters[1] + " ";
                return queryString;
            }

        }

        class concatGenertor extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public concatGenertor() {
                replaceTable.put("concat", new String[]{"concat(", ")"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = " " + parameters[0]
                        + generateMongoDBQuery(root.children.get(0)) + ", "
                        + generateMongoDBQuery(root.children.get(1))
                        + parameters[1] + " ";
                return queryString;
            }

        }

        class rootLeftRight extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public rootLeftRight() {
                replaceTable.put("startswith", new String[]{"charindex(", ")=1"});

            }


            @Override

            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = " (" + parameters[0]
                        + generateMongoDBQuery(root.children.get(1)) + ","
                        + generateMongoDBQuery(root.children.get(0)) + parameters[1] + ") ";
                return queryString;
            }
        }

        class rootLeft extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public rootLeft() {
                replaceTable.put("ceiling", new String[]{"ceiling(", ")"});
                replaceTable.put("floor", new String[]{"floor(", ")"});
                replaceTable.put("round", new String[]{"round(", ", 0)"});
                replaceTable.put("not", new String[]{"!(", ")"});
                replaceTable.put("tolower", new String[]{"lower(", ")"});
                replaceTable.put("toupper", new String[]{"upper(", ")"});
                replaceTable.put("trim", new String[]{"trim(", ")"});
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
            Map<String, String> replaceTable = new HashMap<String, String>();

            public leftRoot() {
                replaceTable.put("length", ".size");
                // replaceTable.put("day", "CURRENT_DATE() ");
                // replaceTable.put("month", "month()+1 ");
                // replaceTable.put("year", "year() ");
                // replaceTable.put("minute", "minute() ");
                // replaceTable.put("second", "second(CURRENT_()) ");
                // replaceTable.put("hour", "hour(CURRENT_TIME()) ");
                replaceTable.put("boolparen", "");
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                // TODO Auto-generated method stub
                String parameter = replaceTable.get(root.data.toString());
                String queryString = generateMongoDBQuery(root.children.get(0))
                        + parameter;
                return queryString;
            }

        }

        class timeFunction extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public timeFunction() {
                // replaceTable.put("length", ".size");
                replaceTable.put("length", new String[]{"length(", ")"});

                replaceTable.put("day", new String[]{"day(", ")"});
                replaceTable.put("month", new String[]{"month(", ")"});
                replaceTable.put("year", new String[]{"year(", ")"});
                replaceTable.put("minute", new String[]{"minute(", ")"});
                replaceTable.put("second", new String[]{"second(", ")"});
                replaceTable.put("hour", new String[]{"hour(", ")"});
                // replaceTable.put("boolparen", "");
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                // TODO Auto-generated method stub
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = " " + parameters[0]
                        + generateMongoDBQuery(root.children.get(0))
                        + parameters[1] + " ";
                return queryString;
            }

        }

        class leftRootRight extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public leftRootRight() {
                // replaceTable.put("concat", new String[]{"(",")"});
                // replaceTable.put("substring", new String[]{"charindex(",")"});
                // replaceTable.put("indexof", new String[]{"index(",")"});

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
                String queryString = null;

                if (root.data.toString().equals("eq")
                        && (root.children.get(0).data.toString().equals("startswith")
                        || root.children.get(0).data.toString().equals("endswith")
                        || root.children.get(0).data.toString().equals("substringof")
                )
                        ) {
                    if (root.children.get(1).data.toString().equals("false"))

                    {
                        if (root.children.get(0).data.toString().equals("substringof"))
                            queryString = generateMongoDBQuery(root.children.get(0)).replace("!=", "=");
                        else
                            queryString = generateMongoDBQuery(root.children.get(0)).replace("=", "!=");

                    } else {
                        queryString = " " + generateMongoDBQuery(root.children.get(0));

                        //	 System.out.print(queryString);
                    }
                } else {
                    queryString = " " + generateMongoDBQuery(root.children.get(0)) + " " + parameters[0] + generateMongoDBQuery(root.children.get(1));
                }

                return " (" + queryString + ") ";
            }
        }

        class RightRootLeft extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public RightRootLeft() {
                replaceTable.put("substringof", new String[]{"charindex(",
                        ")!=0"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = "(" + parameters[0]
                        + generateMongoDBQuery(root.children.get(0)) + ", "
                        + generateMongoDBQuery(root.children.get(1))
                        + parameters[1] + ")";
                return queryString;
            }
        }

        class endsWith extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public endsWith() {
                replaceTable.put("endswith", new String[]{"substring(", ",length(", ")-length(", ")+1,length(", "))="});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                String queryString = " (" + parameters[0]
                        + generateMongoDBQuery(root.children.get(0))
                        + parameters[1] + generateMongoDBQuery(root.children.get(0)) + parameters[2] + generateMongoDBQuery(root.children.get(1))
                        + parameters[3] + generateMongoDBQuery(root.children.get(1)) + parameters[4] + generateMongoDBQuery(root.children.get(1)) + ") ";
                return queryString;
            }
        }

        class leftRootRightThreeParameters extends generator {
            Map<String, String[]> replaceTable = new HashMap<String, String[]>();

            public leftRootRightThreeParameters() {
                replaceTable.put("substring", new String[]{"substring(", ",",
                        ")"});
                replaceTable.put("replace",
                        new String[]{"replace(", ",", ")"});
            }

            @Override
            public String generateQueryString(Node<Object> root) {
                String[] parameters = replaceTable.get(root.data.toString());
                if (root.data.toString().equals("replace")) {
                    String queryString = " " + parameters[0]
                            + generateMongoDBQuery(root.children.get(0)) + ","
                            + generateMongoDBQuery(root.children.get(1))
                            + parameters[1]
                            + generateMongoDBQuery(root.children.get(2))
                            + parameters[2] + " ";

                    return queryString;
                } else {
                    String queryString = " " + parameters[0]
                            + generateMongoDBQuery(root.children.get(0)) + ","
                            + generateMongoDBQuery(root.children.get(1)) + "+1"
                            + parameters[1]
                            + generateMongoDBQuery(root.children.get(2))
                            + parameters[2] + " ";
                    return queryString;
                }

            }
        }

        String generateMongoDBQuery(Node<Object> root) {
            String rootDataValue = root.data.toString();
            String childrenNum = Integer.toString(root.children.size());
            if (generateMatchTable.containsKey(rootDataValue + ","
                    + childrenNum)) {
                generator gen = generateMatchTable.get(rootDataValue + ","
                        + childrenNum);
                return gen.generateQueryString(root);
            } else
                return rootDataValue;
        }

        String generateHQLWhereClause() {
            // BasicDBObject query=new BasicDBObject();
            // if(simpleOperation)
            // mongoPostNode(AST.root);
            // else
            // {
            // mongoMidNode(AST.root);
            String finString = generateMongoDBQuery(AST.root);
            query.append(finString);

            return query.toString();

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
            Node<Object> newNode = new Node<Object>();
            newNode.parent = currentNode;
            // newNode.children=new ArrayList<Node<Object>>();
            currentNode.children.add(newNode);
            currentNode = newNode;
        }

        @Override
        public void betweenDescend() {
            Node<Object> newNode = new Node<Object>();
            newNode.parent = currentNode.parent;
            currentNode.parent.children.add(newNode);
            currentNode = newNode;

        }

        @Override
        public void visit(String arg0) {

        }

        @Override
        public void visit(OrderByExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "orderBy";

        }

        @Override
        public void visit(AddExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "add";
            simpleOperation = false;
        }

        @Override
        public void visit(AndExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "and";
        }

        @Override
        public void visit(BooleanLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();

        }

        @Override
        public void visit(CastExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "cast";

        }

        @Override
        public void visit(ConcatMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "concat";

        }

        @Override
        public void visit(DateTimeLiteral arg0) {
            // TODO Auto-generated method stub
            /*new add*/
            currentNode.data = "'" + arg0.getValue() + "'";
        }

        @Override
        public void visit(DateTimeOffsetLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "'" + arg0.getValue() + "'";
        }

        @Override
        public void visit(DecimalLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(DivExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "div";
            simpleOperation = false;
        }

        @Override
        public void visit(EndsWithMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "endswith";

        }

        @Override
        public void visit(EntitySimpleProperty arg0) {

            if (arg0.getPropertyName().contains("/"))
                currentNode.data = arg0.getPropertyName().replace("/", ".");
            else
                currentNode.data = arg0.getPropertyName();

        }

        @Override
        public void visit(EqExpression arg0) {
            currentNode.data = "eq";

        }

        @Override
        public void visit(GeExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "ge";

        }

        @Override
        public void visit(GtExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "gt";

        }

        @Override
        public void visit(GuidLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(BinaryLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = Hex.encodeHexString(arg0.getValue());

        }

        @Override
        public void visit(ByteLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(SByteLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IndexOfMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "indexof";

        }

        @Override
        public void visit(Direction arg0) {
            // TODO Auto-generated method stub
            currentNode.data = (arg0 == Direction.ASCENDING ? "asc" : "desc");
        }

        @Override
        public void visit(SingleLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(DoubleLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IntegralLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(Int64Literal arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue();
        }

        @Override
        public void visit(IsofExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "isof";

        }

        @Override
        public void visit(LeExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "le";
        }

        @Override
        public void visit(LengthMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "length";

        }

        @Override
        public void visit(LtExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "lt";
        }

        @Override
        public void visit(ModExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "mod";
            simpleOperation = false;
        }

        @Override
        public void visit(MulExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "mul";
            simpleOperation = false;
        }

        @Override
        public void visit(NeExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "ne";
        }

        @Override
        public void visit(NegateExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "negate";
        }

        @Override
        public void visit(NotExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "not";
        }

        @Override
        public void visit(NullLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "null";
        }

        @Override
        public void visit(OrExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "or";
        }

        @Override
        public void visit(ParenExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "paren";
        }

        @Override
        public void visit(BoolParenExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "boolparen";
        }

        @Override
        public void visit(ReplaceMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "replace";

        }

        @Override
        public void visit(StartsWithMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "startswith";

        }

        @Override
        public void visit(StringLiteral arg0) {
            currentNode.data = "'" + arg0.getValue() + "'";
        }

        @Override
        public void visit(SubExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "sub";
        }

        @Override
        public void visit(SubstringMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "substring";

        }

        @Override
        public void visit(SubstringOfMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "substringof";

        }

        @Override
        public void visit(TimeLiteral arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getValue().toString(
                    ExpressionParser.TIME_FORMATTER);
        }

        @Override
        public void visit(ToLowerMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "tolower";

        }

        @Override
        public void visit(ToUpperMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "toupper";

        }

        @Override
        public void visit(TrimMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "trim";

        }

        @Override
        public void visit(YearMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "year";
        }

        @Override
        public void visit(MonthMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "month";
        }

        @Override
        public void visit(DayMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "day";
        }

        @Override
        public void visit(HourMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "hour";
        }

        @Override
        public void visit(MinuteMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "minute";
        }

        @Override
        public void visit(SecondMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "second";
        }

        @Override
        public void visit(RoundMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "round";

        }

        @Override
        public void visit(FloorMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "floor";

        }

        @Override
        public void visit(CeilingMethodCallExpression arg0) {
            // TODO Auto-generated method stub
            currentNode.data = "ceiling";
        }

        @Override
        public void visit(AggregateAnyFunction arg0) {
            // TODO Auto-generated method stub
            if (arg0.getVariable() != null) {
                currentNode.data = arg0.getVariable();
            } else {
                currentNode.data = "";
            }
        }

        @Override
        public void visit(AggregateAllFunction arg0) {
            // TODO Auto-generated method stub
            currentNode.data = arg0.getVariable();
        }

    }

}
