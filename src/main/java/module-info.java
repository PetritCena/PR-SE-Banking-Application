module org.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;


    opens com.jmc.app.Controllers to javafx.fxml;
    exports com.jmc.app;
}