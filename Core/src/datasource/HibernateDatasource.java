package datasource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;


/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 15:04
 */
public class HibernateDatasource implements Datasource {

    private void thrownHibernateException() {

    }

    @Override
    public JSONArray query(String modelName, JSONObject queryParameters) {
        return null;
    }

    @Override
    public Object create(String modelName, JSONObject entry) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Object id = session.save(modelName, entry.getMap());

        session.flush();
        session.close();
        return id;
    }

    @Override
    public Object[] create(String modelName, JSONArray entries) {
        Object[] objects = new Object[entries.length()];
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();
        for (int i = 0; i < entries.length(); i++) {
            try {
                objects[i] = session.save(modelName, ((JSONObject) entries.get(i)).getMap());
            } catch (JSONException e) {
                e.printStackTrace();
                tx.rollback();
                session.close();
                return null;
            }
            if (i % 20 == 0) {
                session.flush();
            }
        }
        tx.commit();
        session.close();

        return objects;
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
