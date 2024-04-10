import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class PortfolioScene implements Initializable {

    private ConnectionDB connectionDB = new ConnectionDB();

    private static MainApplication mainApp = LoginScene.getMainApp();

    private Connection con = connectionDB.getConnection();

    private final ObservableList<Stock> portfolioList = FXCollections.observableArrayList();


    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private TableView<Stock> tableView;

    @FXML
    private Text accountValue;

    @FXML
    private Text balanceValue;

    @FXML
    private TableColumn<Stock, String> currPricePCol;

    @FXML
    private Button marketMenu;

    @FXML
    private Button performanceButton;

    @FXML
    private Button portfolioMenu;

    @FXML
    private TableColumn<Stock, String> purchasePricePCol;

    @FXML
    private TableColumn<Stock, String> quantityPCol;

    @FXML
    private Text returnValue;

    @FXML
    private TableColumn<Stock, String> stockNamePCol;

    @FXML
    void marketMenuAction(ActionEvent event) {
        try {
            FXMLLoader marketLoader = new FXMLLoader(getClass().getResource("market-stocks.fxml"));
            Parent marketRoot = marketLoader.load();
            Scene marketScene = new Scene(marketRoot);
            mainApp.getWindow().setScene(marketScene);
            mainApp.getWindow().setTitle("TradeMasters");
            mainApp.getWindow().show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    @FXML
    void performanceAction(ActionEvent event) {

    }

    @FXML
    void portfolioMenuAction(ActionEvent event) {

    }


    public void updatePortfolio() {
        User mainUser = mainApp.getUser();
        clearPortolioList();
        Portfolio portfolio = mainUser.getPersonalPortfolio();
        portfolioList.addAll(portfolio.getHeldAssets());

        stockNamePCol.setCellValueFactory(new PropertyValueFactory<>("codeName"));
        currPricePCol.setCellValueFactory(new PropertyValueFactory<>("currPrice"));
        purchasePricePCol.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        quantityPCol.setCellValueFactory(new PropertyValueFactory<>("quantityHeld"));

        tableView.setItems(portfolioList);

        accountValue.setText("$" + portfolio.getValue());
        balanceValue.setText("$" + mainUser.getBalance());
    }

    private void clearPortolioList() {
        if (!portfolioList.isEmpty()) {
            portfolioList.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatePortfolio();
    }
}
