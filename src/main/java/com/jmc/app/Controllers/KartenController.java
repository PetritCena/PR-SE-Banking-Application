package com.jmc.app.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Diese Klasse entspricht dem Controller für die Dashboard-Seite.
 */
public class KartenController implements Controller{
    @FXML
    private ListView<Transaction> transactionsListView;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private BorderPane borderpane;
    @FXML
    private HBox kartennummerHbox, ibanHbox, inhaberHbox, folgenummerHbox, geheimzahlHbox;
    @FXML
    private TextField kartenlimitFeld, searchbar;
    @FXML
    private Button searchButton;
    @FXML
    private Label ibanLabel, nameLabel;
    @FXML
    private ImageView qrCodeImageView;

    private List<Transaction> transactions;
    private Account account;
    private Card card;
    private boolean verschleiert = true;

    /**
     * Diese Methode initialisiert die Karten-Seite
     * @param o ist eine Karten-Instanz.
     * @param o2 ist eine Konto-Instanz.
     */
    @Override
    public void initialize(Object o, Object o2) {
        this.card = (Card) o;
        this.account = (Account) o2;
        SceneChanger.loadLeftFrame(borderpane, account.getUser());
        addCardData();
        kartenlimitFeld.setText(card.getKartenLimit()+"");
        ibanLabel.setText(card.getKartenNummer() + "");
        nameLabel.setText(account.getUser().getFirstName() + " " + account.getUser().getLastName());


        searchButton.setStyle("-fx-background-color: grey; -fx-background-radius: 4");
        searchButton.setTextFill(Paint.valueOf("white"));
        filterComboBox.setStyle("-fx-background-color: grey; -fx-background-radius: 4; -fx-text-fill: white;");
        filterComboBox.setItems(FXCollections.observableArrayList(null, "Eingang", "Ausgang"));
        transactions = this.account.getTransactions().stream()
                .filter(s -> s.getKartennummer() != 0)
                .collect(Collectors.toList());
        fillListView(transactions);
    }

    /**
     * Diese Methode zeigt die Karten-Details an.
     */
    public void addCardData(){
        Label kartennnummer = new Label(card.getKartenNummer() + "");
        kartennummerHbox.getChildren().add(kartennnummer);
        Label iban = new Label(card.getIban() + "");
        ibanHbox.getChildren().add(iban);
        Label inhaber = new Label(account.getUser().getFirstName() + " " + account.getUser().getLastName());
        inhaberHbox.getChildren().add(inhaber);
        Label folgenummer = new Label(card.getFolgeNummer() + "");
        folgenummerHbox.getChildren().add(folgenummer);
        Label geheimzahl = new Label("****");
        geheimzahlHbox.getChildren().add(geheimzahl);
        geheimzahl.setCursor(Cursor.OPEN_HAND);
        geheimzahl.setOnMouseClicked(mouseEvent -> {
            showGeheimzahl(geheimzahl);
        });
    }

    private void showGeheimzahl(Label geheimzahl){
        if (verschleiert){
            geheimzahl.setText(card.getGeheimZahl() + "");
            verschleiert = false;
        }
        else {
            geheimzahl.setText("****");
            verschleiert = true;
        }
    }

    /**
     * Diese Methode ändert das Kartenlimit.
     * @throws SQLException wird geworfen, wenn db.changeCardLimit(card, kartenlimitFeld.getText()) einen Fehler zurückgibt.
     */
    public void cardLimitButtonOnAction() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        db.changeCardLimit(card, kartenlimitFeld.getText());
        kartenlimitFeld.setText(card.getKartenLimit()+"");
    }

    private List<Transaction> filterTransactionsByType(String type) {
        Stream<Transaction> filteredTransactions = transactions.stream();
        if (type.equals("Eingang")) {
            filteredTransactions = filteredTransactions.filter(t -> t.getEingangAusgang().equals("Eingang"));
        } else if (type.equals("Ausgang")) {
            filteredTransactions = filteredTransactions.filter(t -> t.getEingangAusgang().equals("Ausgang"));
        }
        return filteredTransactions.collect(Collectors.toList());
    }

    private List<Transaction> searchTransactions(String searchTerm) {
        return transactions.stream()
                .filter(t -> t.getVerwendungszweck().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Diese Methode filtert die Transaktionsliste nach filterType oder searchTerm.
     */
    public void searchButtonOnAction() {
        String filterType = (String) filterComboBox.getValue();
        String searchTerm = searchbar.getText();

        List<Transaction> filteredTransactions = new ArrayList<>();

        if(filterComboBox.getValue() != null) {
            filteredTransactions = filterTransactionsByType(filterType);
        }
        else filteredTransactions = searchTransactions(searchTerm);

        fillListView(filteredTransactions);
    }

    /**
     * Diese Methode generiert einen QR-Code für die Karten-Deatails.
     */
    public void generateQRCode() {
        String data = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "FN:" + account.getUser().getFirstName() + " " + account.getUser().getLastName() + "\n" +
                "EMAIL:" + account.getUser().getEmail() + "\n" +
                "NOTE:IBAN - " + card.getIban() + ", Kartennummer - " + card.getKartenNummer() + "\n" +
                "END:VCARD";
        System.out.println("QR Code Data: " + data); // Debugging line
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 150; // Smaller size
        int height = 150; // Smaller size
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            qrCodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
            qrCodeImageView.setVisible(true); // Make the ImageView visible
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void fillListView(List<Transaction> transactions){
        ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();
        transactionsData.addAll(transactions);
        transactionsListView.setItems(transactionsData);

        transactionsListView.setCellFactory(new Callback<ListView<Transaction>, ListCell<Transaction>>() {
            @Override
            public ListCell<Transaction> call(ListView<Transaction> listView) {
                return new ListCell<Transaction>() {
                    private final HBox content;
                    private final Text description;
                    private final Text amount;

                    {
                        description = new Text();
                        amount = new Text();
                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        content = new HBox(description, spacer, amount);
                    }

                    @Override
                    protected void updateItem(Transaction item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            description.setText(item.getVerwendungszweck());
                            amount.setText(item.getBetrag() + " €");
                            if(item.getEingangAusgang().equals("Eingang")) amount.setFill(Color.GREEN);
                            else amount.setFill(Color.RED);
                            setGraphic(content);
                            if (getIndex() % 2 == 0){
                                setStyle("-fx-background-color: #DAECFB");
                            }
                            else {
                                setStyle("-fx-background-color: white");
                            }
                        }
                        else {
                            setGraphic(null);
                            if (getIndex() % 2 == 0){
                                setStyle("-fx-background-color: #DAECFB");
                            }
                            else {
                                setStyle("-fx-background-color: white");
                            }
                        }
                    }
                };
            }
        });
    }
}