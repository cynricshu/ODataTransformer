package odata.producer.command;

import datasource.Datasource;

/**
 * User: Cynric
 * Date: 14-3-26
 * Time: 15:02
 */
public class CommandContext {
    private Datasource dateSource;

    public Datasource getDateSource() {
        return dateSource;
    }

    public void setDateSource(Datasource dateSource) {
        this.dateSource = dateSource;
    }
}
