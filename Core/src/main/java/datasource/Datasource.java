package datasource;

import util.json.JSONArray;
import util.json.JSONObject;

/**
 * User: Cynric
 * Date: 14-3-9
 * Time: 18:28
 */
public interface Datasource {

    public JSONArray query(String modelName, JSONObject queryParameters);

    public Object create(String modelName, JSONObject entry);

    public Object[] create(String modelName, JSONArray entries);

    public int update(String modelName, JSONObject queryParameters, JSONObject entry);

    public int delete(String modelName, JSONObject queryParameters);

    public void flush();

    public void close();
}
