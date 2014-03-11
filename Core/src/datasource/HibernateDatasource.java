package datasource;

import org.bson.types.ObjectId;
import util.json.JSONArray;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 15:04
 */
public class HibernateDatasource implements Datasource {
    @Override
    public JSONArray query(String modelName, JSONObject queryParameters) {
        return null;
    }

    @Override
    public ObjectId create(String modelName, JSONObject entry) {
        return null;
    }

    @Override
    public int create(String modelName, JSONArray entries) {
        return 0;
    }

    @Override
    public int update(String modelName, JSONObject queryParameters, JSONObject entry) {
        return 0;
    }

    @Override
    public int delete(String modelName, JSONObject queryParameters) {
        return 0;
    }
}
