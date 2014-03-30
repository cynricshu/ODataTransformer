package odata.expressionvisitor;

import org.odata4j.expression.AndExpression;
import org.odata4j.expression.DateTimeLiteral;
import org.odata4j.expression.DateTimeOffsetLiteral;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.EqExpression;
import org.odata4j.expression.OrExpression;

/**
 * User: Cynric
 * Date: 14-3-20
 * Time: 14:58
 */
public class MongodbExpressionVisitor extends CommonExpressionVisitor {
    @Override
    public void visit(DateTimeLiteral arg0) {
        currentNode.data = arg0.getValue();
    }

    @Override
    public void visit(DateTimeOffsetLiteral arg0) {
        currentNode.data = "'" + arg0.getValue() + "'";
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
        currentNode.data = "==";
    }

    @Override
    public void visit(AndExpression arg0) {
        currentNode.data = "&&";
    }

    @Override
    public void visit(OrExpression arg0) {
        currentNode.data = "||";
    }
}
