import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionDB connectionDB = new ConnectionDB();
        Connection connection = connectionDB.getConnection();
        Market stockMarket = new Market(connection);
        Scanner sc = new Scanner(System.in);
        User investor = new User();
        System.out.println("Welcome to Stock Exchange of Ensimag !");
        System.out.println("To start the game, you have to create an account...");
        System.out.println("Please enter your username: ");
        String username = sc.nextLine();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();
        while (!investor.createAccount(username, password, connection)) {
            System.out.println("Please enter your username: ");
            username = sc.nextLine();
            System.out.println("Please enter your password: ");
            password = sc.nextLine();
        }
    }
}
