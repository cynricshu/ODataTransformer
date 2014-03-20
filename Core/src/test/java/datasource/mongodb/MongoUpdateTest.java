package datasource.mongodb;

import util.GenData;
import util.json.JSONException;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:41
 */
public class MongoUpdateTest extends MongoTest {

    public void updateData() {
        JSONObject entry = new JSONObject();
        try {
            entry.put("double", GenData.random(0.0, 100.0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        datasource.update("People", null, entry);
    }
}
