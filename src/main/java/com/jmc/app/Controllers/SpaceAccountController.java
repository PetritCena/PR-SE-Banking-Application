package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpaceAccountController implements Controller {
    @FXML
    private Text accountName;
    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane listViewTitledPane;
    @FXML
    private Button transferÜberweisungButton, searchButton;
    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Label ibanLabel, saldoLabel, typLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hbox;
    @FXML
    private ListView<Transaction> transactionsListView;

    private Account account;
    private ArrayList<Card> cards = null;

    private User user;

    @Override
    public void initialize(Object o, Object o2) {
        this.account = (Account) o;
        this.user = (User) o2;
        cards = account.getCards();
        SceneChanger.loadLeftFrame(borderPane, account.getUser());
        ibanLabel.setText(account.getIban());
        saldoLabel.setText(account.getSaldo() + "€");
        typLabel.setText(account.getTyp());
        loadCard();
        if (account.getTyp().equals("Hauptkonto")){
            transferÜberweisungButton.setText("Überweisung");
            accountName.setText("Hauptkonto");
        }
        else{
            transferÜberweisungButton.setText("Transfer");
            accountName.setText("Spacekonto");
        }

        accordion.setExpandedPane(listViewTitledPane);

        searchButton.setStyle("-fx-background-color: grey; -fx-background-radius: 4");
        searchButton.setTextFill(Paint.valueOf("white"));
        filterComboBox.setStyle("-fx-background-color: grey; -fx-background-radius: 4; -fx-text-fill: white;");
        filterComboBox.setItems(FXCollections.observableArrayList(null, "Eingang", "Ausgang"));
        fillListView(this.account.getTransactions());
    }


//C2AA10FF  3F3F3F  404F1BFF    #003366  #C0C0C0  #D3D3D3  #B0E0E6

    private void loadCard(){
        if(cards == null) return;
        for (Card card : cards){
            //Kreditkarte
            VBox vboxKarte = new VBox();
            hbox.getChildren().add(vboxKarte);
            vboxKarte.setMinSize(183, 117);
            vboxKarte.setMaxSize(183, 117);
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(63,63,63), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            vboxKarte.setBackground(background);
            vboxKarte.setCursor(Cursor.OPEN_HAND);

            //VISA Zeichen
            Text visa = new Text("VISA");
            visa.setFill(Color.WHITE);
            visa.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 16));
            vboxKarte.getChildren().add(visa);
            VBox.setMargin(visa, new Insets(10,0,0,10));

            //imageview vom Chip
            Image chip = new Image(getClass().getResource("/com/jmc/app/iconsChipcard.png").toExternalForm());
            ImageView imageView = new ImageView(chip);
            imageView.setFitHeight(33);
            imageView.setFitWidth(38);
            Image nfc = new Image(getClass().getResource("/com/jmc/app/iconsNfc.png").toExternalForm());
            ImageView imageView2 = new ImageView(nfc);
            imageView2.setFitHeight(21);
            imageView2.setFitWidth(22);
            HBox hBox = new HBox(imageView, imageView2);
            vboxKarte.getChildren().add(hBox);
            HBox.setMargin(imageView, new Insets(3,0,0,7));
            HBox.setMargin(imageView2, new Insets(9,0,0,-4));

            //Kartennummer
            //card.getKartenNummer() = card.getKartenNummer() * 10000000000000000;
            Text kartennummerText = new Text(card.getKartenNummer()+""); //nicht IBAN sonder Kartennummer!!!
            kartennummerText.setFill(Color.WHITE);
            kartennummerText.setFont(Font.font( 14));
            vboxKarte.getChildren().add(kartennummerText);
            VBox.setMargin(kartennummerText, new Insets(0,0,0,30));

            //Name
            Text name = new Text(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase()); //user holen
            name.setFill(Color.WHITE);
            name.setFont(Font.font( 10));
            vboxKarte.getChildren().add(name);
            VBox.setMargin(name, new Insets(7,0,0,30));

            vboxKarte.setOnMouseClicked(event -> {
                Stage stage = (Stage) vboxKarte.getScene().getWindow();
                try {
                    SceneChanger.changeScene("/com/jmc/app/karte.fxml", stage, card, account);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void handleTransferÜberweisungButton(MouseEvent event) throws IOException {
        String resource;
        if (account.getTyp().equals("Hauptkonto")){
            resource = "/com/jmc/app/popupÜberweisung.fxml";
        }
        else {
            resource = "/com/jmc/app/popupTransfer.fxml";
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = (Parent) fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.initialize(user, account);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private List<Transaction> filterTransactionsByType(String type) {
        Stream<Transaction> filteredTransactions = account.getTransactions().stream();
        if (type.equals("Eingang")) {
            filteredTransactions = filteredTransactions.filter(t -> t.getEingangAusgang().equals("Eingang"));
        } else if (type.equals("Ausgang")) {
            filteredTransactions = filteredTransactions.filter(t -> t.getEingangAusgang().equals("Ausgang"));
        }
        return filteredTransactions.collect(Collectors.toList());
    }

    private List<Transaction> searchTransactions(String searchTerm) {
        return account.getTransactions().stream()
                .filter(t -> t.getVerwendungszweck().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public void searchButtonOnAction(ActionEvent event) {
        String filterType = filterComboBox.getValue();
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
