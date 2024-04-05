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
    private double value = 0;
    private double avgReturn = 0;

    public Portfolio() {
        heldAssets = new LinkedList();
    }

    public LinkedList<Stock> getHeldAssets() {
        return heldAssets;
    }
    public double getValue() {
        return value;
    }

    public void updateValue(double newValue) {
        value += newValue;
    }

    public void buyAsset(Stock asset, double quantity) {
        int index = heldAssets.indexOf(asset);
        if (index != -1) {
            Stock stockHeld = heldAssets.get(index);
            stockHeld.setQuantityHeld(asset.getQuantityHeld() + quantity);
            stockHeld.setBuyPrice(asset.getCurrPrice());
        }
        else {
            asset.setBuyPrice(asset.getCurrPrice());
            asset.setQuantityHeld(quantity);
            heldAssets.add(asset);
        }
    }

    public void sellAsset(Stock asset, double quantity) throws Exception{
        int index = heldAssets.indexOf(asset);
        if (index == -1) {
            throw new Exception("No such asset in your portfolio !");
        }
        double quant = asset.getQuantityHeld() - quantity;
        if (quant < 0) {
            throw new Exception("Quantity you want to sell exceeds stock limit !");
        }
        if (quant == 0) {
            heldAssets.remove(asset);
        }
        else {
            Stock stockHeld = heldAssets.get(index);
            stockHeld.setQuantityHeld(asset.getQuantityHeld() - quantity);
        }
    }

}