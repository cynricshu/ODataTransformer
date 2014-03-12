package datasource.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import datasource.Datasource;
import org.bson.types.ObjectId;
import transformer.DataTransformer;
import transformer.ODataFilterToMongoDBTransformer;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Cynric
 * Date: 14-3-9
 * Time: 16:17
 */
public class MongodbDatasource implements Datasource {
    private String _serverAddress;
    private String _databaseName;
    private Mongo _mongo;

    private void thrownMongoException() {

    }

    public MongodbDatasource(String serverAddress, String databaseName) {
        this._serverAddress = serverAddress;
        this._databaseName = databaseName;
    }

    public Mongo getMongo() {
        try {
            return new Mongo(_serverAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeMongo() {
        if (_mongo != null) {
            _mongo.close();
            _mongo = null;
        }
    }

    public DB getDB() {
        if (_mongo == null) {
            _mongo = getMongo();
        }
        return _mongo.getDB(_databaseName);
    }

    public MongodbDatasource() {
        getMongo();
    }

    public BasicDBObject singleQuery(String modelName, JSONObject queryParameters) {

        DBCollection coll = getDB().getCollection(modelName);
        BasicDBObject entityObject = (BasicDBObject) coll.findOne(queryParameters);
        return entityObject;
    }

    @Override
    public JSONArray query(String modelName, JSONObject queryParameters) {
        BasicDBObject query = new BasicDBObject();
        int skip = 0;
        int top = 0;

        if (queryParameters != null) {
            if (queryParameters.has("$filter")) {
                try {
                    String filterString = queryParameters.getString("$filter");
                    DataTransformer transformer = new ODataFilterToMongoDBTransformer();
                    query = transformer.transform(filterString, null);

//                System.out.println(query.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            try {
                if (queryParameters.has("$skip")) {
                    skip = queryParameters.getInt("$skip");
                }
                if (queryParameters.has("$top")) {
                    top = queryParameters.getInt("$top");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        DBCollection coll = getDB().getCollection(modelName);
        DBCursor cursor = coll.find(query);


        if (skip != 0) {
            cursor = cursor.skip(skip);
        }
        if (top != 0) {
            cursor = cursor.limit(top);
        }

        JSONArray jsonArray = new JSONArray(cursor.length());
        for (DBObject dbObject : cursor) {
            jsonArray.put(dbObject);
        }

        closeMongo();
        return jsonArray;
    }

    @Override
    public Object create(String modelName, JSONObject entry) {

        DBCollection coll = getDB().getCollection(modelName);
        int result = 0;

        ObjectId objId = new ObjectId();
        try {
            entry.put("_id", objId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BasicDBObject b = new BasicDBObject(entry.getMap());

        WriteResult writeResult = coll.insert(b, WriteConcern.SAFE);
        if (writeResult.getError() != null) {
            thrownMongoException();
        }
        closeMongo();
        return objId;
    }

    @Override
    public Object[] create(String modelName, JSONArray entries) {

        DBCollection coll = getDB().getCollection(modelName);
        Object[] objects = new Object[entries.length()];

        List<DBObject> list = new ArrayList<>();
        for (int i = 0; i < entries.length(); i++) {
            DBObject o;
            ObjectId objId = new ObjectId();
            try {
                JSONObject entry = (JSONObject) entries.get(i);
                o = new BasicDBObject(entry.getMap());
                o.put("_id", objId);
                objects[i] = objId;
                list.add(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        WriteResult writeResult = coll.insert(list, WriteConcern.SAFE);
        if (writeResult.getError() != null) {
            thrownMongoException();
        }
        closeMongo();
        return objects;
    }

    @Override
    public int update(String modelName, JSONObject queryParameters, JSONObject entry) {

        DBCollection coll = getDB().getCollection(modelName);
        BasicDBObject query = new BasicDBObject();
        int result = 0;

        if (queryParameters != null) {
            if (queryParameters.has("$filter")) {
                try {
                    String filterString = queryParameters.getString("$filter");
                    DataTransformer transformer = new ODataFilterToMongoDBTransformer();
                    query = transformer.transform(filterString, null);

                    System.out.println(query.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    query.put("_id", new ObjectId(queryParameters.getString("_id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        entry.remove("_id");
        entry.remove("__metadata");

        BasicDBObject dbUpdateObject = new BasicDBObject(entry.getMap());
        BasicDBObject dbUpdatePattern = new BasicDBObject("$set", dbUpdateObject);

        WriteResult writeResult = coll.update(query, dbUpdatePattern, false, true, WriteConcern.SAFE);
        if (writeResult.getError() == null) {
            result = writeResult.getN();
        } else {
            thrownMongoException();
        }
        closeMongo();
        return result;
    }

    @Override
    public int delete(String modelName, JSONObject queryParameters) {
        DBCollection coll = getDB().getCollection(modelName);
        BasicDBObject query = new BasicDBObject();
        int result = 0;

        if (queryParameters != null && queryParameters.has("$filter")) {
            try {
                String filterString = queryParameters.getString("$filter");
                DataTransformer transformer = new ODataFilterToMongoDBTransformer();
                query = transformer.transform(filterString, null);

                System.out.println(query.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                query.put("_id", new ObjectId(queryParameters.getString("_id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WriteResult writeResult = coll.remove(query, WriteConcern.SAFE);
        if (writeResult.getError() == null) {
            result = writeResult.getN();
        } else {
            thrownMongoException();
        }
        closeMongo();
        return result;
    }
}
