package test.hibernate;

import org.junit.Test;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Cynric
 * Date: 14-3-12
 * Time: 9:58
 */
public class HibernateQueryTest extends HibernateTest {

    public void test(String filterString) throws Exception {

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) <= 25);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testEq() throws Exception {
        String filterString = "Name eq 'Cynric'";
        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertEquals(entity.get("Name").toString(), "Cynric");
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testGe() throws Exception {
        String filterString = "Age ge 22";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) >= 22);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testAnd() throws Exception {
        String filterString = "Age ge 22 and Name eq 'Cynric'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) >= 22 && entity.get("Name").toString().equals("Cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testOr() throws Exception {
        String filterString = "Age ge 22 or Name eq 'Cynric'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) >= 22 || entity.get("Name").toString().equals("Cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testNot() throws Exception {
        String filterString = "not Age ge 22 or Name eq 'Cynric'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) < 22 || entity.get("Name").toString().equals("Cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testNot2() throws Exception {
        String filterString = "not (Age ge 50 or Name ne 'Cynric')";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) < 50 && entity.get("Name").toString().equals("Cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testAdd() throws Exception {
        String filterString = "Age add 5 gt 20";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) > 15);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testSub() throws Exception {
        String filterString = "Age sub 5 lt 20";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) < 25);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testMul() throws Exception {
        String filterString = "Age mul 2 le 50";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) <= 25);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testDiv() throws Exception {
        String filterString = "(Age div 2) ne 20";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Integer.parseInt(entity.get("Age").toString()) != 40);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testMod() throws Exception {
        String filterString = "(Age add 2) mod 2 ne 0";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue((Integer.parseInt(entity.get("Age").toString()) % 2) != 0);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testSubstringof() throws Exception {
        String filterString = "substringof('A', Name) eq true";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().contains("A"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testStartswith() throws Exception {
        String filterString = "startswith(Name, 'B') eq true";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().startsWith("B"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testEndswith() throws Exception {
        String filterString = "endswith(Name, 'c') eq true";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().endsWith("c"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testLength() throws Exception {
        String filterString = "length(Name) eq 6";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().length() == 6);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testIndexof() throws Exception {
        String filterString = "indexof(Name, 'c') eq 3";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().indexOf('c') == 3);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testReplace() throws Exception {
        String filterString = "replace(Name, 'C', 'c') eq 'cynric'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().equals("Cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testSubstring() throws Exception {
        String filterString = "substring(Name, 0, 2) eq 'Cy'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().substring(0, 2).equals("Cy"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testTolower() throws Exception {
        String filterString = "tolower(Name) eq 'cynric'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().toLowerCase().equals("cynric"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testToupper() throws Exception {
        String filterString = "toupper(Name) eq 'CYNRIC'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().toUpperCase().equals("CYNRIC"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testTrim() throws Exception {
        String filterString = "trim(Name) eq 'Bye bye'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue((entity.get("Name").toString().trim()).equals("Bye bye"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testConcat() throws Exception {
        String filterString = "concat(concat(Name, ', '), Description) eq 'Cynric, me'";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(entity.get("Name").toString().concat(", ").concat(entity.get("Description").toString()).equals("Cynric, me"));
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }


    @Test
    public void testYear() throws Exception {
        String filterString = "year(BirthDate) eq 1992";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.YEAR) == 1992);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testMonth() throws Exception {
        String filterString = "month(BirthDate) eq 3";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.MONTH) == 2);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testDay() throws Exception {
        String filterString = "day(BirthDate) eq 22";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.DAY_OF_MONTH) == 22);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testHour() throws Exception {
        String filterString = "hour(BirthDate) eq 10";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.HOUR_OF_DAY) == 10);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testMinute() throws Exception {
        String filterString = "minute(BirthDate) eq 58";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.MINUTE) == 58);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testSecond() throws Exception {
        String filterString = "second(BirthDate) eq 31";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) entity.get("BirthDate"));
            assertTrue(calendar.get(Calendar.SECOND) == 31);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testFloor() throws Exception {
        String filterString = "floor(Height) eq 170";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Math.floor(Double.parseDouble(entity.get("Height").toString())) == 170);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testRound() throws Exception {
        String filterString = "round(Height) eq 165";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Math.round(Double.parseDouble(entity.get("Height").toString())) == 165);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
    }

    @Test
    public void testCeiling() throws Exception {
        String filterString = "ceiling(Height) eq 160";

        JSONArray resultList = getResult(filterString);
        assertTrue("no match result entity!", resultList.length() > 0);

        for (int i = 0; i < resultList.length(); i++) {
            Map entity = (Map) resultList.get(i);
            assertTrue(Math.ceil(Double.parseDouble(entity.get("Height").toString())) == 160);
        }
        System.out.println("Test Pass for Hibernate$filter: " + filterString);
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
