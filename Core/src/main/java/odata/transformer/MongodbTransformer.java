package odata.transformer;

import com.mongodb.BasicDBObject;
import odata.expressionvisitor.MongodbExpressionVisitor;
import odata.generator.MongodbQueryGenerator;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:37
 */
public class MongodbTransformer implements DataTransformer {

    @Override
    public BasicDBObject transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        MongodbExpressionVisitor visitor = new MongodbExpressionVisitor();
        expression.visit(visitor);

        BasicDBObject mongoDBQuery = new MongodbQueryGenerator().generateMongoDBQuery(visitor.getAST());
        System.out.println(mongoDBQuery);
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
