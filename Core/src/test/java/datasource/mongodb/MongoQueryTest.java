package datasource.mongodb;

import com.mongodb.BasicDBObject;
import datasource.TestFilterString;
import org.junit.Test;
import util.GenData;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * User: Cynric
 * Date: 14-3-10
 * Time: 9:52
 */
public class MongoQueryTest extends MongoTest {

    public void test(String filterString) throws Exception {

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") <= 25);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testEq() throws Exception {
        String filterString = TestFilterString.EQ.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);


        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertEquals(entity.getString("Name"), "Cynric");
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testGe() throws Exception {
        String filterString = TestFilterString.GE.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") >= 22);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testAnd() throws Exception {
        String filterString = TestFilterString.AND.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Date date = (Date) entity.get("BirthDate");
            assertTrue(entity.getInt("Age") >= 22 && date.compareTo(GenData._formater.parse("1992-03-22 19:46:40")) == 0);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testOr() throws Exception {
        String filterString = "Age ge 22 or Name eq 'Cynric'";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") >= 22 || entity.getString("Name").equals("Cynric"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testNot() throws Exception {
        String filterString = TestFilterString.NOT.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") < 22 || entity.getString("Name").equals("Cynric"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testNot2() throws Exception {
        String filterString = TestFilterString.NOT2.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") < 50 && entity.getString("Name").equals("Cynric"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testAdd() throws Exception {
        String filterString = "Age add 5 gt 20";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") > 15);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testSub() throws Exception {
        String filterString = "Age sub 5 lt 20";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") < 25);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testMul() throws Exception {
        String filterString = "Age mul 2 le 50";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") <= 25);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testDiv() throws Exception {
        String filterString = "(Age div 2) ne 20";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getInt("Age") != 40);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testMod() throws Exception {
        String filterString = "(Age add 2) mod 2 ne 0";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue((entity.getInt("Age") % 2) != 0);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testSubstringof() throws Exception {
        String filterString = "substringof('A', Name) eq true";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").contains("A"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testStartswith() throws Exception {
        String filterString = "startswith(Name, 'B') eq true";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").startsWith("B"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testEndswith() throws Exception {
        String filterString = "endswith(Name, 'c') eq true";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").endsWith("c"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testLength() throws Exception {
        String filterString = "length(Name) eq 6";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").length() == 6);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testIndexof() throws Exception {
        String filterString = "indexof(Name, 'c') eq 3";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").indexOf('c') == 3);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

//    @Test
//    public void testReplace() throws Exception {
//        String filterString = "replace(Name, 'C', 'c') eq 'cynric'";
//        System.out.println("Test start for Mongo$filter: " + filterString);
//
//        JSONArray resultList = getResult(filterString);
//        assertTrue("no match result entity!", resultList.length() > 0);
//
//        for (int i = 0; i < resultList.length(); i++) {
//            BasicDBObject entity = (BasicDBObject) resultList.get(i);
//            assertTrue(entity.getString("Name").equals("Cynric"));
//        }
//        System.out.println("Test Pass for Mongo$filter: " + filterString);
//    }

    @Test
    public void testSubstring() throws Exception {
        String filterString = TestFilterString.SUBSTRING3.toString();
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").substring(1, 3).equals("yn"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testTolower() throws Exception {
        String filterString = "tolower(Name) eq 'cynric'";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").toLowerCase().equals("cynric"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testToupper() throws Exception {
        String filterString = "toupper(Name) eq 'CYNRIC'";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").toUpperCase().equals("CYNRIC"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testTrim() throws Exception {
        String filterString = "trim(Name) eq 'Bye bye'";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue((entity.getString("Name").trim()).equals("Bye bye"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testConcat() throws Exception {
        String filterString = "concat(concat(Name, ', '), Description) eq 'Cynric, me'";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(entity.getString("Name").concat(", ").concat(entity.getString("Description")).equals("Cynric, me"));
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }


    @Test
    public void testYear() throws Exception {
        String filterString = "year(BirthDate) eq 1992";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.YEAR) == 1992);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testMonth() throws Exception {
        String filterString = "month(BirthDate) eq 3";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.MONTH) == 2);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testDay() throws Exception {
        String filterString = "day(BirthDate) eq 22";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 22);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testHour() throws Exception {
        String filterString = "hour(BirthDate) eq 10";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.HOUR_OF_DAY) == 10);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testMinute() throws Exception {
        String filterString = "minute(BirthDate) eq 58";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.MINUTE) == 58);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testSecond() throws Exception {
        String filterString = "second(BirthDate) eq 31";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate("BirthDate"));
            assertTrue(calendar.get(Calendar.SECOND) == 31);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testFloor() throws Exception {
        String filterString = "floor(Height) eq 170";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(Math.floor(entity.getDouble("Height")) == 170);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testRound() throws Exception {
        String filterString = "round(Height) eq 165";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(Math.round(entity.getDouble("Height")) == 165);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }

    @Test
    public void testCeiling() throws Exception {
        String filterString = "ceiling(Height) eq 160";
        System.out.println("Test start for Mongo$filter: " + filterString);

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            BasicDBObject entity = (BasicDBObject) resultList.get(i);
            assertTrue(Math.ceil(entity.getDouble("Height")) == 160);
        }
        System.out.println("Test Pass for Mongo$filter: " + filterString);
    }


    private JSONArray getResult(String filterString) {

        JSONObject queryParameter = new JSONObject();
        if (filterString != null && !"".equals(filterString)) {
            try {
                queryParameter.put("$filter", filterString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return datasource.query("People", queryParameter);
    }
}
