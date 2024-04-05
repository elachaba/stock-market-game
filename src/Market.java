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

    public void updateStock(Stock stock, double quantity, Connection con){
        /*We suppose here that the stock exists in the market*/
        /*quantity can be positive or negative double*/
        try {
            /*We update it in the linkedList*/
            for (Stock s : marketStocks) {
                if (s.equals(stock)) {
                    s.updateQuantity(quantity);
                }
            }
            /*Now we update it in the database*/
            Statement stmt = con.createStatement();
            ResultSet rSet = stmt.executeQuery("SELECT Price, CapMarket FROM Stocks WHERE CompanyName=" + "'" + stock.getCodeName() + "'");
            rSet.next();
            double prix = rSet.getDouble("Price");
            double quantStock = rSet.getDouble("CapMarket") / prix;
            PreparedStatement pstmt = con.prepareStatement("UPDATE Stocks SET CapMarket=" + (quantStock+quantity)*prix + " WHERE CompanyName=" + "'" + stock.getCodeName() + "'");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

}