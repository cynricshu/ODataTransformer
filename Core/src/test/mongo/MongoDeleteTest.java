package test.mongo;

import datasource.Datasource;
import datasource.MongodbDatasource;
import util.json.JSONException;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:41
 */
public class MongoDeleteTest {
    private Datasource datasource;

    public MongoDeleteTest() {
        datasource = new MongodbDatasource("127.0.0.1", "transformer");
    }

    public void deleteData() {
        JSONObject queryParameters = new JSONObject();
        try {
            queryParameters.put("$filter", "Name ne 'Cynric'");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        datasource.delete("People", queryParameters);
    }
}
