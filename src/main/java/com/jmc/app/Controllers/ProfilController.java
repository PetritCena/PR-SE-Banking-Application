package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ProfilController {
    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    public static final String USER = "admin";
    public static final String PWD = "BigBankSoSe2024";
    public static final String CONNECT_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/oemer.t/Downloads/Wallet_E4XXMJ5EY9KFQZZ5";

    @FXML
    private TextField vornameFeld;
    @FXML
    private Button speichernButton;
    @FXML
    private TextField nachnameFeld;
    @FXML
    private PasswordField altesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortBestätigungFeld;













}
