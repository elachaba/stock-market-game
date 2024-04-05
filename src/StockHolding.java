/**
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
 */

public class StockHolding {
    private Stock stock;
    private int quantityHeld;
    private double buyPrice;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stock) {
            Stock stock = (Stock) obj;
            return this.stock.equals(stock);
        }
        return false;
    }
}