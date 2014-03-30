package datasource.hibernate;

import datasource.Datasource;
import odata.transformer.DataTransformer;
import odata.transformer.HqlWhereClauseTransformer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.json.JSONArray;
import util.json.JSONException;
import util.json.JSONObject;

import java.util.List;


/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 15:04
 */
public class HibernateDatasource implements Datasource {

    private Session session;

    private void thrownHibernateException() {

    }

    @Override
    public JSONArray query(String modelName, JSONObject queryParameters) {
        int skip = 0;
        int top = 0;
        StringBuilder hqlBuilder = new StringBuilder();
        String whereClause = null;

        if (queryParameters != null) {
            if (queryParameters.has("$filter")) {
                try {
                    String filterString = queryParameters.getString("$filter");
                    DataTransformer transformer = new HqlWhereClauseTransformer();
                    whereClause = transformer.transform(filterString, null);

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

        hqlBuilder.append("from ").append(modelName);
        if (whereClause != null) {
            hqlBuilder.append(" WHERE ").append(whereClause);
        }

        Session session = datasource.hibernate.HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hqlBuilder.toString());
        if (skip != 0) {
            query.setFirstResult(skip);
        }
        if (top != 0) {
            query.setMaxResults(top);
        }

        List list = query.list();
        session.flush();
        session.close();

        JSONArray jsonArray = new JSONArray(list);

        return jsonArray;
    }

    @Override
    public Object create(String modelName, JSONObject entry) {
        Session session = getSession();
        Object id = session.save(modelName, entry.getMap());

        session.flush();
        session.close();
        return id;
    }

    @Override
    public Object[] create(String modelName, JSONArray entries) {
        Object[] objects = new Object[entries.length()];
        Session session = getSession();

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

    public Session getSession() {
        if (session == null || !session.isOpen()) {
            this.session = datasource.hibernate.HibernateUtil.getSessionFactory().openSession();
            return session;
        }
        return this.session;
    }

    @Override
    public int update(String modelName, JSONObject queryParameters, JSONObject entry) {
        return 0;
    }

    @Override
    public int delete(String modelName, JSONObject queryParameters) {
        return 0;
    }

    @Override
    public void flush() {
        if (session != null) {
            session.flush();
        }
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }
    }
}
