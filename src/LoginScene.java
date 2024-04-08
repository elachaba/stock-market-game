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


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginScene extends Application implements EventHandler<ActionEvent>, Initializable {

    Connection con;
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
                errorText.setText("Account successfully registered!");
                errorText.setFill(Color.GREEN);
            }
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ConnectionDB connectionDB = new ConnectionDB();
        con = connectionDB.getConnection();
        Parent root = FXMLLoader.load(getClass().getResource("login-page.fxml"));
        stage.setTitle("TradeMasters");
        stage.setScene(new Scene(root));
        registerButton = (Button) root.lookup("#registerButton");
        userTextField = (TextField) root.lookup("#userTextField");
        pwdTextField = (TextField) root.lookup("#pwdTextField");
        errorText = (Text) root.lookup("#errorText");
        exitButton = (Button) root.lookup("#exitButton");
        registerButton.setOnAction(this);
        stage.show();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == registerButton) {
            registerAction();
        }
        if (actionEvent.getSource() == exitButton) {
            exit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
