package datasource;

import java.sql.Connection;

/**
 *
 * @author Simon
 */
public interface ConnectionIf {

    public Connection getConnection(String username, String password);

    public void releaseConnection();

}
