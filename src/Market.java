import java.sql.*;
import java.util.LinkedList;

/**
 * @brief Represents the stock market
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
 */


public class Market {
    private LinkedList<Stock> marketStocks;

    public Market(Connection connection) {
        marketStocks = new LinkedList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM Stocks");
            while (rset.next()) {
                String stockName = rset.getString("CompanyName");
                double price = rset.getDouble("Price");
                double capMarket = rset.getDouble("CapMarket");
                double quantity = capMarket / price;
                Stock stock = new Stock(stockName, price, quantity);
                marketStocks.add(stock);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public LinkedList<Stock> getMarketStocks() {
        return marketStocks;
    }

    public void addStock(Stock stock, Connection con){
        marketStocks.add(stock);
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Stocks VALUES (?, ?, ?)");
            pstmt.setString(1, stock.getCodeName());
            pstmt.setDouble(2, stock.getCurrPrice());
            pstmt.setDouble(3, stock.getQuantity() * stock.getCurrPrice());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

}