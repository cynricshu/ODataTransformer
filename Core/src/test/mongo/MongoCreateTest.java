package test.mongo;

import datasource.Datasource;
import datasource.MongodbDatasource;
import org.bson.types.ObjectId;
import util.GenData;
import util.json.JSONException;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:41
 */
public class MongoCreateTest {
    private Datasource datasource;

    public MongoCreateTest() {
        datasource = new MongodbDatasource("127.0.0.1", "transformer");
    }

    public void insertData() {
        for (int i = 0; i < 200; i++) {
            JSONObject people = genPeople();
            ObjectId objId = datasource.create("People", people);
//            System.out.println(objId);
        }
    }

    private JSONObject genPeople() {
        JSONObject people = new JSONObject();
        try {
            people.put("Name", GenData.genString(6));
            people.put("Age", GenData.genAge(1, 100));
            people.put("Year", GenData.genYear(1970, 2015));
            people.put("BirthDate", GenData.genDate("1970-01-01", "2014-12-30"));
            people.put("Height", GenData.random(155.0, 200.0));
            people.put("Description", "me");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }
}
