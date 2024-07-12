package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse entspricht dem Controller für die Krypto-Seite.
 */
public class KryptoController implements Controller {
    @FXML
    private TableView<CryptoData> trendsTable;
    @FXML
    private TableView<CryptoData> topKryptoTable;
    @FXML
    private TableColumn<CryptoData, String> topKryptoNameColumn, topKryptoPriceColumn, trendsNameColumn, trendsPriceColumn;
    @FXML
    private LineChart<String, Number> bitcoinChart;
    @FXML
    private BorderPane borderPane;

    private User user;
    private Timeline timeline;

    // Cache variables
    private List<CryptoData> cachedTrends = new ArrayList<>();
    private List<CryptoData> cachedTopKrypto = new ArrayList<>();
    private List<Double> bitcoinPrices = new ArrayList<>();
    private List<String> bitcoinDates = new ArrayList<>();
    private LocalDate lastUpdateTime;
    private final ReentrantLock lock = new ReentrantLock();

    private static final DecimalFormat df = new DecimalFormat("#.000");
    private static final Logger LOGGER = Logger.getLogger(KryptoController.class.getName());

    private static final String BTC_API_URL = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=usd&days=30";

    /**
     * Diese Methode initialisiert die Krypto-Seite
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da keine Account-Instanz notwendig ist.
     */
    @Override
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;

        setupBitcoinChart();
        topKryptoNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        topKryptoPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceFormatted"));
        trendsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        trendsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("priceFormatted"));

        lastUpdateTime = LocalDate.now().minusDays(10);

        updateData();

        timeline = new Timeline(new KeyFrame(Duration.minutes(5), event -> updateData()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    private void setupBitcoinChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price (USD)");

        bitcoinChart.setTitle("Bitcoin Price Over Last 6 months");
        bitcoinChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bitcoin");
        bitcoinChart.getData().add(series);

        bitcoinChart.setCreateSymbols(false); // Ensures the chart is a line chart without symbols at data points
    }

    private void updateBitcoinChart() {
        Platform.runLater(() -> {
            XYChart.Series<String, Number> series = bitcoinChart.getData().get(0);
            series.getData().clear();

            for (int i = 0; i < bitcoinPrices.size(); i++) {
                series.getData().add(new XYChart.Data<>(bitcoinDates.get(i), bitcoinPrices.get(i)));
            }
        });
    }

    private void updateData() {
        new Thread(() -> {
            lock.lock();
            try {
                // Check if last update was more than 5 minutes ago
                if (lastUpdateTime != null && lastUpdateTime.plusDays(1).isAfter(LocalDate.now())) {
                    LOGGER.log(Level.INFO, "Data update skipped. Last update was less than 5 minutes ago.");
                    return;
                }

                lastUpdateTime = LocalDate.now();

                try {
                    // Fetch new data
                    List<CryptoData> trends = getTrends();
                    List<CryptoData> topKrypto = getTopKrypto();
                    List<Double> btcPrices = getHistoricalDataWithRetry(BTC_API_URL, 5);

                    // Cache the new data
                    cachedTrends = trends;
                    cachedTopKrypto = topKrypto;
                    bitcoinPrices = btcPrices;
                    bitcoinDates.clear();
                    LocalDate today = LocalDate.now();
                    for (int i = btcPrices.size(); i > 0; i--) {
                        bitcoinDates.add(today.minusDays(30 + i).toString());
                    }

                    // Update the UI with the new data
                    Platform.runLater(() -> {
                        updateTable(trendsTable, cachedTrends);
                        updateTable(topKryptoTable, cachedTopKrypto);
                        updateBitcoinChart();
                    });
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to fetch data", e);
                    // Show default or error values if the data cannot be fetched
                    Platform.runLater(this::showErrorState);
                }
            } finally {
                lock.unlock();
            }
        }).start();
    }

    private void showErrorState() {
        updateTable(trendsTable, List.of(new CryptoData("Unable to fetch trends", 0.0)));
        updateTable(topKryptoTable, List.of(new CryptoData("Unable to fetch top cryptocurrencies", 0.0)));
    }

    private void updateTable(TableView<CryptoData> table, List<CryptoData> data) {
        table.getItems().clear();
        table.getItems().addAll(data.subList(0, Math.min(data.size(), 5)));
    }

    private static List<CryptoData> getTrends() throws Exception {
        String apiUrl = "https://api.coingecko.com/api/v3/search/trending";
        JSONObject json = getJsonResponseWithRetry(apiUrl, 5);
        List<CryptoData> trends = new ArrayList<>();
        if (json != null) {
            JSONArray coins = json.getJSONArray("coins");
            for (int i = 0; i < coins.length(); i++) {
                JSONObject coin = coins.getJSONObject(i).getJSONObject("item");
                String name = coin.getString("name");
                double priceBtc = coin.optDouble("price_btc", 0.0);  // Use correct key
                double priceUsd = priceBtc * 30000; // Assuming conversion to USD
                trends.add(new CryptoData(name, priceUsd));
            }
        }
        return trends;
    }

    private static List<CryptoData> getTopKrypto() throws Exception {
        String apiUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=5&page=1&sparkline=false";
        JSONArray coins = getJsonArrayResponseWithRetry(apiUrl, 5);
        List<CryptoData> topKrypto = new ArrayList<>();
        if (coins != null) {
            for (int i = 0; i < coins.length(); i++) {
                JSONObject coin = coins.getJSONObject(i);
                topKrypto.add(new CryptoData(coin.getString("name"), coin.getDouble("current_price")));
            }
        }
        return topKrypto;
    }

    private static List<Double> getHistoricalDataWithRetry(String apiUrl, int maxAttempts) throws IOException {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                return getHistoricalData(apiUrl);
            } catch (IOException e) {
                if (e.getMessage().contains("Server returned HTTP response code: 429")) {
                    try {
                        Thread.sleep((long) Math.pow(2, attempts) * 1000); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    attempts++;
                } else {
                    throw e;
                }
            }
        }
        throw new IOException("Failed to fetch data from API after " + maxAttempts + " attempts");
    }

    private static List<Double> getHistoricalData(String apiUrl) throws IOException {
        List<Double> prices = new ArrayList<>();
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONArray pricesArray = new JSONObject(content.toString()).getJSONArray("prices");
            for (int i = 0; i < pricesArray.length(); i++) {
                prices.add(pricesArray.getJSONArray(i).getDouble(1));
            }
        } else {
            throw new IOException("Server returned HTTP response code: " + conn.getResponseCode());
        }
        return prices;
    }

    private static JSONObject getJsonResponseWithRetry(String apiUrl, int maxAttempts) throws Exception {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                return getJsonResponse(apiUrl);
            } catch (IOException e) {
                if (e.getMessage().contains("Server returned HTTP response code: 429")) {
                    try {
                        Thread.sleep((long) Math.pow(2, attempts) * 1000); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    attempts++;
                } else {
                    throw e;
                }
            }
        }
        throw new IOException("Failed to fetch data from API after " + maxAttempts + " attempts");
    }

    private static JSONObject getJsonResponse(String apiUrl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return new JSONObject(content.toString());
        } else {
            throw new IOException("Server returned HTTP response code: " + conn.getResponseCode());
        }
    }

    private static JSONArray getJsonArrayResponseWithRetry(String apiUrl, int maxAttempts) throws Exception {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                return getJsonArrayResponse(apiUrl);
            } catch (IOException e) {
                if (e.getMessage().contains("Server returned HTTP response code: 429")) {
                    try {
                        Thread.sleep((long) Math.pow(2, attempts) * 1000); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    attempts++;
                } else {
                    throw e;
                }
            }
        }
        throw new IOException("Failed to fetch data from API after " + maxAttempts + " attempts");
    }

    private static JSONArray getJsonArrayResponse(String apiUrl) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return new JSONArray(content.toString());
        } else {
            throw new IOException("Server returned HTTP response code: " + conn.getResponseCode());
        }
    }

    /**
     * Diese innere Klasse dient als Model für die Krypto-Daten.
     */
    public static class CryptoData {
        private final String name;
        private final double price;

        /**
         * Dieser Konstruktro erstellt eine CryptoData-Instanz und setzt für name und price die mitgegebenen Parameters ein.
         * @param name ist der Name der Kryptowährung.
         * @param price ist der aktuelle Preis der Kryptowährung.
         */
        public CryptoData(String name, double price) {
            this.name = name;
            this.price = price;
        }

        /**
         * Diese Methode gibt den Namen zurück.
         * @return Gibt den Namen zurück.
         */
        public String getName() {
            return name;
        }

        /**
         * Diese Methode gibt den Preis zurück.
         * @return Gibt den Preis zurück.
         */
        public double getPrice() {
            return price;
        }

        /**
         * Diese Methode gibt den Preis in formatierter Form zurück.
         * @return Gibt den Preis in formatierter Form zurück.
         */
        public String getPriceFormatted() {
            return "$" + df.format(price);
        }
    }
}