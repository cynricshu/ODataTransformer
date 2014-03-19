package mongo;

import datasource.Datasource;
import datasource.mongodb.MongodbDatasource;

/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 21:29
 */
public class MongoTest {
    protected Datasource datasource;

    public MongoTest() {
        datasource = new MongodbDatasource("127.0.0.1", "transformer");
    }
}
