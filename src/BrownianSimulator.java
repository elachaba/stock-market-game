import java.util.Random;

/**
 This class will generate random stock prices using a Brownian Bridge which simulates the prices
 according to a standard brownian motion .
 
 @brief Stock Market Price Simulation
 @author Khalfallah Firas
 @author EL-ACHAB Aymane
 @author Kabbaj Reda
 */

public class BrownianSimulator {
    

    private static final double[] initialStockPrices = init(); // Initial Stock Prices

    public static double[][] simulation(int nbTimes, int nbStocks){

        double[][] StockPrices = new double[nbStocks][nbTimes];
        for (int i=0; i<nbStocks; i++){
                StockPrices[i][0] = initialStockPrices[i];
                StockPrices[i][nbTimes-1] = initialStockPrices[i]+ normal(0,3* nbTimes);
                recursiveBridge(StockPrices,0, nbTimes-1, i);
        }
        return StockPrices;
    }

    private static double normal(double m, double v){
        Random random = new Random();
        double gaussianValue = random.nextGaussian();
        return m+ gaussianValue*Math.sqrt(v);

    }

    private static void recursiveBridge(double[][] r, int start, int finish, int stockIndex){
        if (finish- start>1){
            int mid = (finish+start)/2;
            double m = r[stockIndex][start] + (((double)mid-(double)start)/((double)finish-(double)start))*(r[stockIndex][finish]- r[stockIndex][start]);
            double v = (((double)finish - (double)mid)*((double)mid- (double)start))/((double)finish- (double) start);
            r[stockIndex][mid] = normal(m,v);
            recursiveBridge(r, start, mid, stockIndex);
            recursiveBridge(r, mid, finish, stockIndex);
        }
    }

    private static double[] init(){
        double[] initPrices = new double[10];
        initPrices[0] = 169.58;   // Apple Stock
        initPrices[1] = 153.94;   // Google Stock
        initPrices[2] = 527.34;   // Meta Stock
        initPrices[3] = 797.56;   // BlackRock Stock
        initPrices[4] = 197.45;   // JPMorgan & Chase Stock
        initPrices[5] = 312.73;   // Amazon Stock
        initPrices[6] = 97.85;    // Microsoft Stock
        initPrices[7] = 2845.58;  // Tesla Stock
        initPrices[8] = 175.22;   // Walmart Stock
        initPrices[9] = 155.68;   // Coca-Cola Stock
        return initPrices;
    }


}
