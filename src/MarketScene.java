import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MarketScene implements Initializable {

    private ConnectionDB connectionDB = new ConnectionDB();

    private static MainApplication mainApp = LoginScene.getMainApp();

    private Connection con = connectionDB.getConnection();
    @FXML
    private TextField stockNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private ChoiceBox<String> actionChoice;
    @FXML
    private Text errorText;
    @FXML
    private TableView<Stock> tableView;
    @FXML
    private TableColumn<Stock, String> stockNameCol;
    @FXML
    private TableColumn<Stock, String> priceCol;
    @FXML
    private TableColumn<Stock, String> quantityCol;
    @FXML
    private Button marketMenu;
    @FXML
    private Button portfolioMenu;


    @FXML
    void portfolioMenuAction(ActionEvent event) {
        try {
            FXMLLoader portLoader = new FXMLLoader(getClass().getResource("portfolio.fxml"));
            Parent portRoot = portLoader.load();
            Scene portScene = new Scene(portRoot);
            mainApp.getWindow().setScene(portScene);
            mainApp.getWindow().setTitle("TradeMasters");
            mainApp.getWindow().show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
    @FXML
    void marketMenuAction(ActionEvent event) {}

    private final ObservableList<Stock> dataList = FXCollections.observableArrayList();

    private final ObservableList<String> buySellList = FXCollections.observableArrayList("Buy", "Sell");

    public void orderAction(ActionEvent event) {
        String stockName = stockNameField.getText();
        if (stockName.isBlank() || quantityField.getText().isBlank() || actionChoice.getValue().isBlank()) {
            errorText.setText("Fields are missing !");
            errorText.setFill(Color.RED);
        }
        else if (Double.parseDouble(quantityField.getText()) <= 0) {
            errorText.setText("You must type a positive quantity !");
            errorText.setFill(Color.RED);
        } else {
            double quantity = Double.parseDouble(String.format("%.2f", Double.parseDouble(quantityField.getText())));
            User mainUser = mainApp.getUser();
            Market mainMarket = mainApp.getMarket();
            Stock desiredStock = mainMarket.getStock(stockName);
            try {
                if (actionChoice.getValue().equals("Buy")) {
                    mainUser.buyAsset(desiredStock, quantity, mainMarket, con);
                    updateTable(stockName, -quantity);
                    errorText.setText(stockNameField.getText() + " bought successfully");
                    errorText.setFill(Color.GREEN);
                } else if (actionChoice.getValue().equals("Sell")) {
                    mainUser.sellAsset(desiredStock, quantity, mainMarket, con);
                    updateTable(stockName, quantity);
                    errorText.setText(stockNameField.getText() + " sold successfully");
                    errorText.setFill(Color.GREEN);
                }

            } catch (Exception e) {
                errorText.setText(e.getMessage());
                errorText.setFill(Color.RED);
            }
        }
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
//    @Override
//    public void start(Stage stage) throws Exception {
//
//        Parent root = FXMLLoader.load(getClass().getResource("market-stocks.fxml"));
//        stage.setTitle("TradeMasters");
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT CompanyName, Price, CapMarket FROM (SELECT * FROM Stocks ORDER BY id DESC) WHERE ROWNUM <= 10");

            while (rset.next()) {
                String companyName = rset.getString("CompanyName");
                double price = rset.getDouble("Price");
                double volume = Double.parseDouble(String.format("%.2f", rset.getDouble("CapMarket") / price));
                dataList.add(new Stock(companyName, price, volume));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }

        stockNameCol.setCellValueFactory(new PropertyValueFactory<>("codeName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("currPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        FilteredList<Stock> filteredData = new FilteredList<>(dataList, b -> true);
        stockNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (stock.getCodeName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        /*We initialize here the choice Box*/
        actionChoice.setValue("Action");
        actionChoice.setItems(buySellList);
    }

    public void updateTable(String stockName, double quantity) {
        for (Stock stock : dataList) {
            if (stock.getCodeName().equals(stockName)) {
                stock.setQuantity(stock.getQuantity()+quantity);
            }
        }
        stockNameCol.setCellValueFactory(new PropertyValueFactory<>("codeName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("currPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));


//        FilteredList<Stock> filteredData = new FilteredList<>(dataList, b -> true);
//        stockNameField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(stock -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if (stock.getCodeName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
//                    return true;
//                } else {
//                    return false;
//                }
//            });
//        });
//        SortedList<Stock> sortedData = new SortedList<>(filteredData);
//        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
//        tableView.setItems(sortedData);
        tableView.refresh();

    }
    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }
}
