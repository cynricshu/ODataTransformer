package datasource;

import javax.sql.DataSource;

/**
 * User: Cynric
 * Date: 14-4-14
 * Time: 10:43
 */
public class DataSourceConfig {
    enum DataSourceType {
        MySQL,
        MongoDB
    }

    DataSourceType dataSourceType;

    DataSource dataSource;


}
