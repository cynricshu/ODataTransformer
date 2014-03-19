package datasource.hibernate;

import util.GenData;
import util.json.JSONArray;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 21:24
 */
public class HibernateCreateTest extends HibernateTest {

    public void testSingleInsert() {
        JSONObject me = GenData.genPeopleMe();
        datasource.create("People", me);
    }

    public void testMultiInsert() {
        JSONArray peoples = GenData.genPeopleList(200);
        datasource.create("People", peoples);
    }
}
