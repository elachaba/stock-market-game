import java.sql.Connection;
import java.sql.*;
/**
 * @brief Represents a user of the simulator game
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
*/


public class User {
    private String userName;
    private String password;
    private double balance = 10000;
    private Portfolio personalPortfolio = new Portfolio();
    /*Favourites*/

    public User() {
        super();
    }
    public User(String userName, String password, double balance) {
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public Portfolio getPersonalPortfolio() {
        return personalPortfolio;
    }

    public static void checklLogin(String userN, String pwd, Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM Users WHERE UserName=" + "'" + userN + "'");
            if (rset.next()) {
                if (rset.getString("Password").equals(pwd)) {
                    System.out.println("Logged in Successfully !");
                }
                else {
                    System.out.println("Wrong Password !");
                }
            }
            else {
                System.out.println("User not found ! You have to create an account...");
            }
            stmt.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public boolean createAccount(String userN, String pwd, Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM Users WHERE UserName=" + "'" + userN + "'");
            if (rset.next()) {
                System.out.println("Username already exists ! Choose another Username...");
                return false;
            }
            PreparedStatement ptsmt = con.prepareStatement("INSERT INTO Users VALUES (?, ?)");
            ptsmt.setString(1, userN);
            ptsmt.setString(2, pwd);
            ptsmt.executeUpdate();
            System.out.println("Account created successfully !");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }
}