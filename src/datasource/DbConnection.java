package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Simon
 */
public class DbConnection implements ConnectionIf {

    private Connection con;
    private final String driverAndDialect = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";

    public DbConnection() {
        con = null;
    }

    @Override
    public Connection getConnection(String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(driverAndDialect, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    @Override
    public void releaseConnection() {
        try {
            con.close();
        } catch (SQLException e) {
        }
    }

}
