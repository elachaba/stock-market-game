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
            /*Stocker la liste des actions de la base de données dans LinkedList*/
            clearMarketStocks();
            dropStocksTable(connection);
            createStocksTable(connection);
            brownianSimulation(connection, 10, 10);
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT CompanyName, Price, CapMarket FROM (SELECT * FROM Stocks ORDER BY id DESC) WHERE ROWNUM <= 10");
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
    private void brownianSimulation(Connection connection, int nbStocks, int nbTimes) throws SQLException {
        String insertPriceSQL = "INSERT INTO Stocks (CompanyName, Price, CapMarket) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertPriceSQL)) {
            double[][] prices = BrownianSimulator.simulation(nbTimes, nbStocks);
            for (int i = 1; i <= nbTimes; i++) {
                for (int j = 0; j < nbStocks; j++) {
                    pstmt.setString(1, "Stock_" + (j + 1));
                    pstmt.setDouble(2, prices[j][i-1]);
                    pstmt.setDouble(3, prices[j][i-1] * 10); // Default market Cap at the start of the game
                    pstmt.executeQuery();
                }
            }
        }
    }

    private void dropStocksTable(Connection connection) throws SQLException {
        String dropTableSQL = "DROP TABLE Stocks";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            // Ignore if table doesn't exist
            if (!"42Y55".equals(e.getSQLState())) {
                throw e;
            }
        }
    }

    private void createStocksTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE Stocks ("
                + "id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
                + "CompanyName VARCHAR2(255) NOT NULL,"
                + "Price NUMBER NOT NULL,"
                + "CapMarket NUMBER NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

    public LinkedList<Stock> getMarketStocks() {
        return marketStocks;
    }

    public void clearMarketStocks() {
        if (!marketStocks.isEmpty()) {
            marketStocks.clear();
        }
    }

    public void updateStock(Stock stock, double quantity, Connection con){
        /*We suppose here that the stock exists in the market*/
        /*quantity can be positive or negative double*/
        try {
            /*We update it in the linkedList*/
            this.getStock(stock).updateQuantity(quantity);
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

    public Stock getStock(String code){
        for (Stock s : marketStocks) {
            if (s.getCodeName().equals(code)) {
                return s;
            }
        }
        return null;
    }

    public Stock getStock(Stock stock){
        for (Stock s : marketStocks) {
            if (s.getCodeName().equals(stock.getCodeName())) {
                return s;
            }
        }
        return null;
    }
}