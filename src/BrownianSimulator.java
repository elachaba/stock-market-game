import java.util.Random;
import javax.swing.*;
import java.awt.*;

/**
 This class will generate random stock prices using a Brownian Bridge which simulates the prices
 according to a standard brownian motion .
 
 @brief Stock Market Price Simulation
 @author Khalfallah Firas
 @author EL-ACHAB Aymane
 @author Kabbaj Reda
 */

public class BrownianSimulator {
    

    private double[] initialStockPrices = init(); // Initial Stock Prices on April 5th

    public double[][] simulation(int nbTimes, int nbStocks){

        double[][] StockPrices = new double[nbStocks][nbTimes];
        for (int i=0; i<nbStocks; i++){
            for (int j=0; j<nbTimes;j++){
                StockPrices[i][0] = initialStockPrices[i];
                StockPrices[i][nbTimes-1] = initialStockPrices[i]+ normal(0,3* nbTimes);
                recursiveBridge(StockPrices,0, nbTimes-1, i); 
            }
        }
        return StockPrices;
    }

    private double normal(double m, double v){
        Random random = new Random();
        double gaussianValue = random.nextGaussian();
        return m+ gaussianValue*Math.sqrt(v);

    }

    private void recursiveBridge(double[][] r, int start , int finish, int stockIndex){
        if (finish- start>1){
            int mid = (finish+start)/2;
            double m = r[stockIndex][start] + ((mid-start)/(finish-start))*(r[stockIndex][finish]- r[stockIndex][start]);
            double v = ((finish - mid)*(mid- start))/(finish- start);
            r[stockIndex][mid] = normal(m,v);
            recursiveBridge(r, start, mid, stockIndex);
            recursiveBridge(r, mid, finish, stockIndex);
        }
    }

    private double[] init(){
        /** 
         * This function returns initial stock prices at time = 0 as of April 5th
         */

        double[] initPrices = new double[5];
        initPrices[0] = 169.58; // Apple Stock
        initPrices[1] = 153.94; // Google Stock
        initPrices[2] = 527.34; // Meta Stock
        initPrices[3] = 797.56; // BlackRock Stock
        initPrices[4] = 197.45; // JPMorgan & Chase Stock
        return initPrices;

    }


    /* Don't focus on this it's just for plotting */
    /*public void plot(double[] prices) {
        JFrame frame = new JFrame("Stock Price Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                double minValue = Double.MAX_VALUE;
                double maxValue = Double.MIN_VALUE;
                for (double price : prices) {
                    minValue = Math.min(minValue, price);
                    maxValue = Math.max(maxValue, price);
                }

                int numPoints = prices.length;
                double xScale = (double) width / (numPoints - 1);
                double yScale = (double) height / (maxValue - minValue);

                // Draw gridlines
                g2d.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < numPoints; i++) {
                    int x = (int) (i * xScale);
                    g2d.drawLine(x, 0, x, height);
                }
                for (int i = 0; i <= 10; i++) {
                    int y = (int) ((maxValue - minValue) / 10 * i * yScale);
                    g2d.drawLine(0, height - y, width, height - y);
                }

                // Draw axes
                g2d.setColor(Color.BLACK);
                g2d.drawLine(0, height, width, height);
                g2d.drawLine(0, 0, 0, height);

                // Draw axis labels
                g2d.drawString("Time", width / 2, height - 10);
                g2d.drawString("Price", 10, height / 2);

                // Draw price line
                g2d.setColor(Color.BLUE);
                for (int i = 1; i < numPoints; i++) {
                    int x1 = (int) ((i - 1) * xScale);
                    int y1 = (int) (height - (prices[i - 1] - minValue) * yScale);
                    int x2 = (int) (i * xScale);
                    int y2 = (int) (height - (prices[i] - minValue) * yScale);
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }*/

}
