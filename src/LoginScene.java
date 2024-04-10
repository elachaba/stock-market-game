import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginScene {


    private ConnectionDB connectionDB = new ConnectionDB();
    private Connection con = connectionDB.getConnection();

    private static MainApplication mainApp;
    @FXML
    private Button registerButton;

    @FXML
    private Button exitButton;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField pwdTextField;
    @FXML
    private Text errorText;

    @FXML
    public void registerAction() {
        String username = userTextField.getText();
        String password = pwdTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorText.setText("Fields are missing !");
            errorText.setFill(Color.RED);
        } else {
            if (!User.createAccount(username, password, con)) {
                errorText.setText("Username already exists ! Choose another Username...");
                errorText.setFill(Color.RED);

            } else {
                switchToMarketScene();
                mainApp.setUser(new User(username, password));
            }
        }
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    public static MainApplication getMainApp() {
        return mainApp;
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

//    public void setMainApp(MainApplication mainApp) {
//        this.mainApp = mainApp;
//    }

    public void switchToMarketScene() {
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


}
