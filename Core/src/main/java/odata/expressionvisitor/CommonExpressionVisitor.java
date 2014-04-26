package odata.expressionvisitor;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.odata4j.expression.*;
import org.odata4j.repack.org.apache.commons.codec.binary.Hex;
import util.Node;
import util.Tree;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 14:33
 */
public class CommonExpressionVisitor implements org.odata4j.expression.ExpressionVisitor {

    static DateTimeFormatter m_DateTimeParser = ISODateTimeFormat.dateTime();
    Tree<Object> AST = new Tree("");
    Node<Object> currentNode = AST.root;

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
    public void visit(EntitySimpleProperty arg0) {
        if (arg0.getPropertyName().contains("/"))
            currentNode.data = arg0.getPropertyName().replace("/", ".");
        else
            currentNode.data = arg0.getPropertyName();
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
    public void visit(BooleanLiteral arg0) {
        currentNode.data = arg0.getValue();
    }

    @Override
    public void visit(NullLiteral arg0) {
        currentNode.data = "null";
    }

    @Override
    public void visit(DateTimeLiteral arg0) {
        currentNode.data = "'" + m_DateTimeParser.parseDateTime(arg0.getValue().toString() + "Z").toString() + "'";
    }

    @Override
    public void visit(DateTimeOffsetLiteral arg0) {
        currentNode.data = "'" + arg0.getValue() + "'";
    }

    @Override
    public void visit(TimeLiteral arg0) {
        currentNode.data = arg0.getValue().toString(
                ExpressionParser.TIME_FORMATTER);
    }

    @Override
    public void visit(DecimalLiteral arg0) {
        currentNode.data = arg0.getValue();
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
    public void visit(StringLiteral arg0) {
        currentNode.data = "'" + arg0.getValue() + "'";
    }

    @Override
    public void visit(AddExpression arg0) {
        currentNode.data = "+";
    }

    @Override
    public void visit(SubExpression arg0) {
        currentNode.data = "-";
    }

    @Override
    public void visit(MulExpression arg0) {
        currentNode.data = "*";
    }

    @Override
    public void visit(DivExpression arg0) {
        currentNode.data = "/";
    }

    @Override
    public void visit(ModExpression arg0) {
        currentNode.data = "%";
    }

    @Override
    public void visit(EqExpression arg0) {
        currentNode.data = "=";
    }

    @Override
    public void visit(GeExpression arg0) {
        currentNode.data = ">=";
    }

    @Override
    public void visit(GtExpression arg0) {
        currentNode.data = ">";
    }

    @Override
    public void visit(LeExpression arg0) {
        currentNode.data = "<=";
    }

    @Override
    public void visit(LtExpression arg0) {
        currentNode.data = "<";
    }

    @Override
    public void visit(NeExpression arg0) {
        currentNode.data = "!=";
    }

    @Override
    public void visit(AndExpression arg0) {
        currentNode.data = "and";
    }

    @Override
    public void visit(OrExpression arg0) {
        currentNode.data = "or";
    }

    @Override
    public void visit(NotExpression arg0) {
        currentNode.data = "not";
    }

    @Override
    public void visit(NegateExpression arg0) {
        currentNode.data = "negate";
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
    public void visit(LengthMethodCallExpression arg0) {
        currentNode.data = "length";
    }

    @Override
    public void visit(StartsWithMethodCallExpression arg0) {
        currentNode.data = "startswith";
    }

    @Override
    public void visit(EndsWithMethodCallExpression arg0) {
        currentNode.data = "endswith";
    }

    @Override
    public void visit(IndexOfMethodCallExpression arg0) {
        currentNode.data = "indexof";
    }

    @Override
    public void visit(ConcatMethodCallExpression arg0) {
        currentNode.data = "concat";
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

    @Override
    public void visit(IsofExpression arg0) {
        currentNode.data = "isof";
    }

    @Override
    public void visit(CastExpression arg0) {
        currentNode.data = "cast";
    }
}
