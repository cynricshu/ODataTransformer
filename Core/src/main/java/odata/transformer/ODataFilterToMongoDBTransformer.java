package odata.transformer;

import com.mongodb.BasicDBObject;
import odata.expressionvisitor.MongoDBExpressionVisitor;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:37
 */
public class ODataFilterToMongoDBTransformer implements DataTransformer {

    @Override
    public BasicDBObject transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        MongoDBExpressionVisitor visitor = new MongoDBExpressionVisitor();
        expression.visit(visitor);

        BasicDBObject mongoDBQuery = visitor.generateMongoDBQuery();
//        System.out.println(mongoDBQuery);
        return mongoDBQuery;
    }

    @Override
    public String getSourceTypeName() {
        return "ODataFilter";
    }

    @Override
    public String getDistTypeName() {
        return "MongoDB";
    }
}
