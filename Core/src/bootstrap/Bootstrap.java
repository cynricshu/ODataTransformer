package bootstrap;

import datasource.MongodbDatasource;
import org.bson.types.ObjectId;
import transformer.DataTransformer;
import transformer.ODataFilterToMongoDBTransformer;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * User: Cynric
 * Date: 14-3-4
 * Time: 9:28
 */
public class Bootstrap {
    MongodbDatasource datasource = new MongodbDatasource();

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.testQuery();
    }

    public void insertData() {

        JSONObject people = new JSONObject();
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            people.put("Name", "Cynric");
            people.put("Age", 22);
            people.put("Year", 1992);
            people.put("BirthDate", formater.parse("1992-03-22"));
            people.put("Description", "me");

            ObjectId objId = datasource.create("People", people);
            System.out.println(objId);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void testQuery() {
        Scanner in = new Scanner(System.in);

        DataTransformer transformer = new ODataFilterToMongoDBTransformer();


        while (in.hasNextLine()) {
            String filterString = in.nextLine();
            JSONObject queryParameter = new JSONObject();

            if (filterString != null && !"".equals(filterString)) {
                try {
                    queryParameter.put("$filter", filterString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONArray resultList = datasource.query("People", queryParameter);
            System.out.println("finish");
        }
    }
}
