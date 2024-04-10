import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MarketScene implements Initializable {

    private ConnectionDB connectionDB = new ConnectionDB();
    private Connection con = connectionDB.getConnection();

    private static MainApplication mainApp = LoginScene.getMainApp();

    private Timer globalTimer;

    @FXML
    private Label timerLabel;
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
        updateStockPrices(); // Update stock prices initially
        setupTimer(); // Start the timer

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

                return stock.getCodeName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        /*We initialize here the choice Box*/
        actionChoice.setValue("Action");
        actionChoice.setItems(buySellList);

        tableView.setRowFactory(tv -> {
            TableRow<Stock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    openGraphView(row.getItem());
                }
            });
            return row;
        });

        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // Vérifier si une ligne est sélectionnée
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    // Mettre à jour le champ de texte stockNameField avec le nom du stock sélectionné
                    Stock selectedStock = tableView.getSelectionModel().getSelectedItem();
                    stockNameField.setText(selectedStock.getCodeName());
                }
            }
        });
    }

    private void setupTimer() {
        Timeline globalTimeline = new Timeline();
        AtomicInteger totalSeconds = new AtomicInteger(600);
        Timeline finalGlobalTimeline = globalTimeline;
        globalTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    int minutes = totalSeconds.get() / 60;
                    int seconds = totalSeconds.get() % 60;
                    // Update the timer label
                    timerLabel.setText(String.format("Game Time remaining: %02d:%02d", minutes, seconds));

                    // Decrement the remaining time
                    totalSeconds.getAndDecrement();


                    // If the countdown reaches 0, stop the timer
                    if (totalSeconds.get() < 0) {
                        assert false;
                        finalGlobalTimeline.stop();


                    }
                })
        );

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(30), event -> {
                    // Update stock prices
                    updateStockPrices();
                })
        );
        globalTimeline.setCycleCount(Timeline.INDEFINITE);
        globalTimeline.play();

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }




    private int currentGroup = 0;

    private void updateStockPrices() {
        try {
            Statement stmt = con.createStatement();
            int offset = currentGroup * 10;
            ResultSet rset = stmt.executeQuery("SELECT * FROM (SELECT id,CompanyName, Price, CapMarket, ROWNUM AS row_num FROM Stocks ORDER BY ID) WHERE row_num > " + offset + " AND row_num <= " + (offset + 10));

            dataList.clear(); // Clear previous data

            while (rset.next()) {
                int id = rset.getInt("id");
                String companyName = rset.getString("CompanyName");
                double price = Double.parseDouble(String.format("%.2f",rset.getDouble("Price")));
                double volume = Double.parseDouble(String.format("%.2f",rset.getDouble("CapMarket") / price));
                dataList.add(new Stock(id, companyName, price, volume));
            }

            // Update the UI with the new data
            tableView.refresh();

            currentGroup++; // Move to the next group of 10 stocks

            // Reset currentGroup to 0 if we reach the end of the data
            if (currentGroup * 10 >= 600) {
                currentGroup = 0;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }


    public void setGlobalTimer(Timer globalTimer){
        this.globalTimer=globalTimer;
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

        tableView.refresh();


    }

    // Méthode pour ouvrir la fenêtre de visualisation du graphe
    public void openGraphView(Stock selectedStock) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GraphView.fxml"));
            Scene graphScene = new Scene(loader.load());
            GraphViewController graphController = loader.getController();
            graphController.setStock(selectedStock); // Passer le stock sélectionné au contrôleur de vue du graphe
            List<Double> stockPrices = getStockPricesFromDatabase(selectedStock);
            graphController.setPrices(stockPrices);
            Stage graphStage = new Stage();
            graphStage.initModality(Modality.APPLICATION_MODAL);
            graphStage.setScene(graphScene);
            graphStage.setTitle( selectedStock.getCodeName()+" Price Evolution");
            graphStage.showAndWait(); // Attendre que la fenêtre soit fermée
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Double> getStockPricesFromDatabase(Stock selectedStock) {
        List<Double> prices = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT Price, CapMarket FROM Stocks WHERE CompanyName=" + "'" + selectedStock.getCodeName() + "'" +"AND id<=" + selectedStock.getId())/* initialiser votre PreparedStatement */;
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double price = resultSet.getDouble("Price");
                prices.add(price);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return prices;
    }
    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

}
