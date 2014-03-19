package datasource.hibernate;

import java.sql.Types;

/**
 * User: Cynric
 * Date: 14-3-12
 * Time: 16:00
 */
public class MySQL5Dialect extends org.hibernate.dialect.MySQL5Dialect {

    public MySQL5Dialect() {
        super();
    }

    @Override
    protected void registerVarcharTypes() {
        registerColumnType(Types.VARCHAR, "longtext");
//		registerColumnType( Types.VARCHAR, 16777215, "mediumtext" );
        registerColumnType(Types.VARCHAR, 65535, "varchar($l) binary ");
        registerColumnType(Types.LONGVARCHAR, "longtext");
    }

}
