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
    private double quantityHeld = 0;
    private double buyPrice = currPrice;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

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

    public double getQuantityHeld() {
        return quantityHeld;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setQuantityHeld(double quantityHeld) {
        this.quantityHeld = quantityHeld;
    }
    public void updateQuantity(double newQuantity) {
        quantity += newQuantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stock) {
            Stock stock = (Stock) obj;
            return this.codeName.equals(stock.codeName);
        }
        return false;
    }


}