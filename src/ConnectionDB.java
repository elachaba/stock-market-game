import java.sql.*;

public class ConnectionDB {
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "kabbajr";
    static final String PASSWD = "kabbajr";
    private Connection con;


    public ConnectionDB() {
        try {
            /*Enregistrement du driver Oracle*/
            System.out.println("Loading Oracle Driver... Please wait...");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("Loaded successfully");
            System.out.println("Connecting to the database... Please wait...");
            this.con = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("Connected successfully");
        } catch (SQLException e) {
            System.err.println("Connection failed !");
            e.printStackTrace(System.err);
        }
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() throws SQLException {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println("Connection failed to properly close!");
            e.printStackTrace(System.err);
        }
    }
}
