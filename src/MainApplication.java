import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class MainApplication extends Application {

    private Stage window;
    private User investor;
    private Portfolio portfolio;
    private final ConnectionDB connectionDB = new ConnectionDB();
    private final  Connection con = connectionDB.getConnection();
    private Market market = new Market(con);

    public Stage getWindow() {
        return window;
    }

    public User getUser() {
        return investor;
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    public void setUser(User investor) {
        this.investor = investor;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Market getMarket() {
        return market;
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login-page.fxml"));;
        Parent loginRoot = loginLoader.load();
        LoginScene loginController = loginLoader.getController();
        loginController.setMainApp(this);

        Scene loginScene = new Scene(loginRoot);
        stage.setTitle("TradeMasters");
        stage.setScene(loginScene);
        stage.show();
    }
}
