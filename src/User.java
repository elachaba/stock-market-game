/**
 * @brief Represents a user of the simulator game
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
*/


public class User {
    private int id;
    private String userName;
    private String password;
    private double balance = 10000;
    private Porfolio personalPortfolio = new Portfolio();
    /*Favourites*/

    public User(String name) {
        this.userName = name;
    }
}