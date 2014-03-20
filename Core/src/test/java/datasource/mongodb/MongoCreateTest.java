package datasource.mongodb;

import util.GenData;
import util.json.JSONArray;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:41
 */
public class MongoCreateTest extends MongoTest {

    public void testSingleInsert() {
        JSONObject me = GenData.genPeopleMe();
        datasource.create("People", me);
    }

    public void testMultiInsert() {
        JSONArray peoples = GenData.genPeopleList(200);
        datasource.create("People", peoples);
    }


}
