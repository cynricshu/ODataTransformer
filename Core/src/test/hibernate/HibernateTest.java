package test.hibernate;

import datasource.Datasource;
import datasource.HibernateDatasource;

/**
 * User: Cynric
 * Date: 14-3-11
 * Time: 21:28
 */
public class HibernateTest {
    protected Datasource datasource;

    public HibernateTest() {
        this.datasource = new HibernateDatasource();
    }
}
