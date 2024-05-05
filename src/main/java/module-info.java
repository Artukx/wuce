module org.example.sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires bcrypt;
    requires java.desktop;
    requires org.yaml.snakeyaml;
    requires itextpdf;

    opens org.example.sample to javafx.fxml;
    exports org.example.sample;
    exports Models;
    opens Models to javafx.fxml;
}