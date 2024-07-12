module org.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires sendgrid.java;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.json;
    requires javafx.swing;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;
    requires org.junit.platform.launcher;
    requires org.junit.jupiter.engine;


    opens com.jmc.app.Controllers to javafx.fxml;
    exports com.jmc.app;
    exports com.jmc.app.Controllers;
    exports com.jmc.app.Models;
    opens com.jmc.app.Models to javafx.fxml;
    opens com.jmc.app.Models.tests to org.junit.platform.commons;
}