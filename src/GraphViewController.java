import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.List;

public class GraphViewController {

    @FXML
    private LineChart<Number, Number> lineChart;

    private Stock stock;
    private List<Double> prices;

    public void initialize() {
        // Créer les axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        // Créer le graphique
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stock Price Graph");

        if (stock != null) {


            // Créer les données du graphe à partir des données de la base de données
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(stock.getCodeName() + " Stock Price");
            for (int i = 0; i < prices.size(); i++) {
                series.getData().add(new XYChart.Data<>(i + 1, prices.get(i)));
            }

            // Ajouter les données au graphique
            lineChart.getData().add(series);
        }
    }

    public void setStock(Stock selectedStock) {
        this.stock = selectedStock;
    }

    public void setPrices(List<Double> prices){
        this.prices = prices;
    }

    public Stock getStock() {
        return stock;
    }

    public List<Double> getPrices() {
        return prices;
    }
}
