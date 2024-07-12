package com.jmc.app.Controllers;

import com.jmc.app.Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Diese Klasse entspricht dem Controller für die Konto-Seite.
 */
public class SpaceAccountController implements Controller {
    @FXML
    private Circle photoCircle;
    @FXML
    private FontAwesomeIconView stiftIcon;
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
    private Label ibanLabel, typLabel;
    @FXML
    public Label saldoLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hbox;
    @FXML
    private ListView<Transaction> transactionsListView;

    private Account account;
    private ArrayList<Card> cards = null;
    private User user;
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Diese Methode initialisiert die Konto-Seite.
     * @param o ist eine Account-Instanz.
     * @param o2 ist eine User-Instanz.
     */
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
            photoCircle.setVisible(false);
            stiftIcon.setVisible(false);
        }
        else{
            transferÜberweisungButton.setText("Transfer");
            accountName.setText(account.getName());
        }

        if (this.account.getPic() != null && this.account.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(this.account.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        }
        else photoCircle.setFill(Paint.valueOf("#DAECFB"));
        accordion.setExpandedPane(listViewTitledPane);

        searchButton.setStyle("-fx-background-color: grey; -fx-background-radius: 4");
        searchButton.setTextFill(Paint.valueOf("white"));
        filterComboBox.setStyle("-fx-background-color: grey; -fx-background-radius: 4; -fx-text-fill: white;");
        filterComboBox.setItems(FXCollections.observableArrayList(null, "Eingang", "Ausgang"));
        fillListView(this.account.getTransactions());
    }

    /**
     * Diese Methode übernimmt das Auswählen eines Fotos für das Konto.
     * @throws SQLException wird geworfen, wenn account.setPic(imageFile) eine Fehler zurückgibt.
     * @throws IOException wird geworfen, wenn account.setPic(imageFile) eine Fehler zurückgibt.
     */
    public void choosePhoto() throws SQLException, IOException {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File imageFile = fileChooser.showOpenDialog(new Stage());
        if (imageFile != null) {
            account.setPic(imageFile);
            Image image = new Image(imageFile.toURI().toString());
            photoCircle.setFill(new ImagePattern(image));
        }
    }

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

            //Imageview vom Chip
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
            Text kartennummerText = new Text(card.getKartenNummer()+"");
            kartennummerText.setFill(Color.WHITE);
            kartennummerText.setFont(Font.font( 14));
            vboxKarte.getChildren().add(kartennummerText);
            VBox.setMargin(kartennummerText, new Insets(0,0,0,30));

            //Name
            Text name = new Text(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
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

    /**
     * Diese Methode bringt den User zum Überweisungs-Dialog.
     * @throws IOException wird geworfen, wenn fxmlLoader.load() einen Fehler zurückgibt.
     */
    public void handleTransferÜberweisungButton() throws IOException {
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
        if(account.getTyp().equals("Hauptkonto")){
            popupUeberweisungController cntrl = (popupUeberweisungController) controller;
            cntrl.setSpaceController(this);
        }
        else{
            popupTransferController cntrl = (popupTransferController) controller;
            cntrl.setSpaceController(this);
        }
        controller.initialize(user, account);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
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

    /**
     * Diese Methode filtert die Transaktionsliste nach filterType oder searchTerm.
     */
    public void searchButtonOnAction() {
        String filterType = filterComboBox.getValue();
        String searchTerm = searchbar.getText();

        List<Transaction> filteredTransactions = new ArrayList<>();

        if(filterComboBox.getValue() != null) {
            filteredTransactions = filterTransactionsByType(filterType);
        }
        else filteredTransactions = searchTransactions(searchTerm);

        fillListView(filteredTransactions);
    }

    /**
     * Diese Methode füllt die ListView transactionsListView mit den mitgegebenen Transaktionen.
     * @param transactions ist die Liste von Transaktionen, mit dem das ListView transactionsListView befüllt wird.
     */
    public void fillListView(List<Transaction> transactions){
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
