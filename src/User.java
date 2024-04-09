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
    private double balance = 100000;
    private Portfolio personalPortfolio = new Portfolio();
    /*Favourites*/

    public User() {
        super();
    }
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void updateBalance(double cash) {
        this.balance += cash;
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

    public static boolean createAccount(String userN, String pwd, Connection con) {
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

    public void buyAsset(Stock stock, double quantity, Market market, Connection con)  throws Exception{
        if (balance < quantity * stock.getCurrPrice()) {
            throw new Exception("Insufficient funds to buy this quantity of this stock !");
        }
        personalPortfolio.buyAsset(stock, quantity);
        personalPortfolio.updateValue(quantity*stock.getCurrPrice());
        updateBalance(-quantity*stock.getCurrPrice());
        market.updateStock(stock, -quantity, con);
    }

    public void sellAsset(Stock stock, double quantity, Market market, Connection con) throws Exception {
        personalPortfolio.sellAsset(stock, quantity);
        personalPortfolio.updateValue(-quantity * stock.getCurrPrice());
        updateBalance(quantity * stock.getCurrPrice());
        market.updateStock(stock, quantity, con);
    }

}