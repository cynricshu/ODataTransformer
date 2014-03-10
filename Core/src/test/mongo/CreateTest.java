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
public class CreateTest {
    private Datasource datasource;

    public CreateTest() {
        datasource = new MongodbDatasource("127.0.0.1", "transformer");
    }

    public void insertData() {
        for (int i = 0; i < 100; i++) {
            JSONObject people = genPeople();
            ObjectId objId = datasource.create("People", people);
            System.out.println(objId);
        }
    }

    private JSONObject genPeople() {
        JSONObject people = new JSONObject();
        try {
            people.put("Name", GenData.genString(6));
            people.put("Age", GenData.genAge());
            people.put("Year", GenData.genYear());
            people.put("BirthDate", GenData.genDate());
            people.put("Description", "me");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return people;
    }
}
