package datasource.mongodb;

import datasource.Datasource;

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
