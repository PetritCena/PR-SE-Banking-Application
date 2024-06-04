package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KartenController implements Controller{
    @FXML
    private ListView<Transaction> transactionsListView;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private BorderPane borderpane;
    @FXML
    private HBox kartennummerHbox, ibanHbox, inhaberHbox, kartenlimitHbox, folgenummerHbox, geheimzahlHbox;
    @FXML
    private TextField kartenlimitFeld, searchbar;
    @FXML
    private Button kartenlimitButton, searchButton;
    @FXML
    private Label ibanLabel, nameLabel;

    private List<Transaction> transactions;
    private Account account;
    private Card card;
    private boolean verschleiert = true;


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

    public void addCardData(){
        Label kartennnummer = new Label(card.getKartenNummer() + "");
        kartennummerHbox.getChildren().add(kartennnummer);
        Label iban = new Label(card.getIban() + "");
        ibanHbox.getChildren().add(iban);
        Label inhaber = new Label(account.getUser().getFirstName() + " " + account.getUser().getFirstName());
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

    public void showGeheimzahl(Label geheimzahl){
        if (verschleiert){
            geheimzahl.setText(card.getGeheimZahl() + "");
            verschleiert = false;
        }
        else {
            geheimzahl.setText("****");
            verschleiert = true;
        }
    }
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

    public void searchButtonOnAction(ActionEvent event) {
        String filterType = (String) filterComboBox.getValue();
        String searchTerm = searchbar.getText();

        List<Transaction> filteredTransactions = new ArrayList<>();

        if(filterComboBox.getValue() != null) {
            filteredTransactions = filterTransactionsByType(filterType);
        }
        else filteredTransactions = searchTransactions(searchTerm);

        fillListView(filteredTransactions);
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
                        content = new HBox(description, amount);
                    }

                    @Override
                    protected void updateItem(Transaction item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            switch (item.getVerwendungszweck()) {
                                case "Transfer" -> content.setSpacing(470);
                                case "Abhebung" -> content.setSpacing(459);
                                case "Einzahlung" -> content.setSpacing(458);
                                case "Kartenzahlung" -> content.setSpacing(438);
                                default -> content.setSpacing(441);
                            }
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