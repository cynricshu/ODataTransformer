package odata.transformer;

import odata.expressionvisitor.CommonExpressionVisitor;
import odata.expressionvisitor.HqlWhereClauseExpressionVisitor;
import odata.generator.Generator;
import odata.generator.HqlWhereClauseGenerator;
import org.odata4j.expression.CommonExpression;
import org.odata4j.expression.ExpressionParser;

public class HqlWhereClauseTransformer extends DataTransformer {

    @Override
    public String transform(String inputData, Object[] params) {
        ExpressionParser.DUMP_EXPRESSION_INFO = true;
        CommonExpression expression = parse(inputData);
        CommonExpressionVisitor visitor = new HqlWhereClauseExpressionVisitor();
        expression.visit(visitor);

        Generator gen = new HqlWhereClauseGenerator();
        String hqlWhereClause = (String) gen.generateQueryString(visitor.getAST().root);
        System.out.println(hqlWhereClause);
        return hqlWhereClause;
    }

    @Override
    public String getDistTypeName() {
        return "HQLWhereClause";
    }


}
