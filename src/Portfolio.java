/**
 * @brief Represents a portfolio (of stokcs only)
 * @author EL-ACHAB Aymane
 * @author Khalfallah Firas
 * @author Kabbaj Reda
 * @version 1.0
 * @date 31 Mars 2024
 */

import java.util.LinkedList;

public class Portfolio {
    private LinkedList<Stock> heldAssets;
    private double value;
    private double avgReturn;

    public Portfolio() {
        heldAssets = new LinkedList();
    }
}