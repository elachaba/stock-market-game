/**
 * @brief Represents a company stock
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
 */

public class Stock {
    private final String codeName; /*Entreprise*/
    private double currPrice;
    private double quantity;

    public Stock(String codeName, double currPrice, double quantity) {
        this.codeName = codeName;
        this.currPrice = currPrice;
        this.quantity = quantity;
    }

    public String getCodeName() {
        return codeName;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getCurrPrice() {
        return currPrice;
    }
}