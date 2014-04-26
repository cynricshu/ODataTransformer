package datasource.hibernate;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * User: Cynric
 * Date: 14-4-8
 * Time: 11:46
 */
public class BasicDataSource extends org.apache.commons.dbcp.BasicDataSource {


    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public BasicDataSource() {
        super();
        setUrl("jdbc:mariadb://localhost:3306/transformer");
        setUsername("root");
        setPassword("root");
        setDriverClassName("org.mariadb.jdbc.Driver");
    }
}
