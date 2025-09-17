module com.example.tgcontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.logging;
    requires java.desktop;

    opens com.example.tgcontrol to javafx.fxml;
    opens com.example.tgcontrol.scenes to javafx.fxml;
    exports com.example.tgcontrol;

}