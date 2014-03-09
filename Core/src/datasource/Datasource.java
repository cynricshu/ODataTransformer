package datasource;

/**
 * User: Cynric
 * Date: 14-3-9
 * Time: 18:28
 */
public abstract class Datasource {

    public abstract <T, V> T singleQuery(V queryParameters);

    public abstract <T, V> T query(V queryParameters);

    public abstract <T, V> T create(V entry);

    public abstract <T, V> T update(V queryParameters, V update);

    public abstract <T, V> T delete(V queryParameters);
}
