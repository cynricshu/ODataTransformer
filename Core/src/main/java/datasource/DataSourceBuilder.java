package datasource;

/**
 * User: Cynric
 * Date: 14-4-14
 * Time: 10:52
 */
public class DataSourceBuilder {
    private DataSourceConfig dataSourceConfig;

    private DataSourceBuilder dataSourceConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        return this;
    }


}
