package odata.transformer;

import com.mongodb.BasicDBObject;
import odata.expressionvisitor.CommonExpressionVisitor;
import odata.expressionvisitor.MongodbExpressionVisitor;
import odata.generator.Generator;
import odata.generator.MongodbQueryGenerator;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:37
 */
public class MongodbTransformer extends DataTransformer {

    @Override
    public BasicDBObject transform(String inputData, Object[] params) {
        CommonExpression expression = ExpressionParser.parse(inputData);
        CommonExpressionVisitor visitor = new MongodbExpressionVisitor();
        expression.visit(visitor);

        Generator gen = new MongodbQueryGenerator();
        BasicDBObject mongoDBQuery = (BasicDBObject) gen.generateQueryString(visitor.getAST().root);
        System.out.println(mongoDBQuery);
        return mongoDBQuery;
    }


    @Override
    public String getDistTypeName() {
        return "MongoDB";
    }
}
