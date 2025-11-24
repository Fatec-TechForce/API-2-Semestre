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
    requires java.sql;
    requires mammoth;
    requires org.apache.pdfbox;
    requires flexmark.html2md.converter;
    requires flexmark;

    opens com.example.tgcontrol to javafx.fxml;
    exports com.example.tgcontrol;
    opens com.example.tgcontrol.controllers to javafx.fxml;
    opens com.example.tgcontrol.controllers.Alunos to javafx.fxml;
    opens com.example.tgcontrol.controllers.Professor to javafx.fxml;
    opens com.example.tgcontrol.controllers.Geral to javafx.fxml;
    opens com.example.tgcontrol.controllers.ProfessorTG to javafx.fxml;
    opens com.example.tgcontrol.model to javafx.base;
    opens com.example.tgcontrol.utils to javafx.base;
}