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
            dropStocksTable(connection);
            createStocksTable(connection);
            brownianSimulation(connection, 10, 10);
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

    public void updateStock(Stock stock, double quantity, Connection con) {
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
            PreparedStatement subQueryStmt = con.prepareStatement(
                    "SELECT MIN(id) AS next_id FROM (SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn FROM Stocks WHERE CompanyName = ?) WHERE rn > (SELECT ROW_NUMBER() OVER (ORDER BY id) FROM Stocks WHERE CompanyName = ? AND ROWNUM <= 1)"
            );
            subQueryStmt.setString(1, stock.getCodeName());
            subQueryStmt.setString(2, stock.getCodeName());
            ResultSet subResultSet = subQueryStmt.executeQuery();
            int nextId = 0;
            if (subResultSet.next()) {
                nextId = subResultSet.getInt("next_id");
            }
            subResultSet.close();
            subQueryStmt.close();

            PreparedStatement updateStmt = con.prepareStatement(
                    "UPDATE Stocks SET CapMarket = CapMarket + ? * Price " +
                            "WHERE CompanyName = ? AND id = ?"
            );
            updateStmt.setDouble(1, quantity);
            updateStmt.setString(2, stock.getCodeName());
            updateStmt.setInt(3, nextId);
            updateStmt.executeUpdate();
            updateStmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }





}