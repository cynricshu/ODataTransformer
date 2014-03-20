package datasource.mongodb;

import util.json.JSONException;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:41
 */
public class MongoDeleteTest extends MongoTest {

    public void deleteData() {
        JSONObject queryParameters = new JSONObject();
        try {
            queryParameters.put("$filter", "Name ne 'Cynric' or Name ne ' Bye bye '");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        datasource.delete("People", queryParameters);
    }
}
