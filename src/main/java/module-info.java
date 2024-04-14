module org.example.bankingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.jmc.bankingapplication to javafx.fxml;
    exports com.jmc.bankingapplication;
}