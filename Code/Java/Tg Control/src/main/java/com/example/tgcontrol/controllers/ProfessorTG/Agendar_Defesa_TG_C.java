package com.example.tgcontrol.controllers.ProfessorTG;

import com.example.tgcontrol.utils.DatabaseUtils;
import com.example.tgcontrol.utils.SessaoManager;
import com.example.tgcontrol.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agendar_Defesa_TG_C {

    private static final Logger LOGGER = Logger.getLogger(Agendar_Defesa_TG_C.class.getName());

    @FXML private Label lblAluno;
    @FXML private DatePicker dpData;
    @FXML private TextField txtHora;
    @FXML private TextField txtLocal;
    @FXML private TextArea txtBanca;

    private String studentEmail;
    private String studentName;

    public void setDadosAluno(String studentEmail, String studentName) {
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        lblAluno.setText("Aluno: " + studentName + " (" + studentEmail + ")");
    }

    @FXML
    private void handleAgendar() {
        LocalDate data = dpData.getValue();
        String horaStr = txtHora.getText();
        String local = txtLocal.getText();
        String banca = txtBanca.getText();
        String schedulerEmail = SessaoManager.getInstance().getEmailUsuario();

        if (data == null || horaStr.isBlank() || local.isBlank() || banca.isBlank()) {
            UIUtils.showAlert("Atenção", "Todos os campos são obrigatórios.");
            return;
        }

        try {
            LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dataHoraDefesa = LocalDateTime.of(data, hora);

            if (dataHoraDefesa.isBefore(LocalDateTime.now())) {
                UIUtils.showAlert("Erro de Data", "A data e hora da defesa devem ser futuras.");
                return;
            }

            boolean sucesso = DatabaseUtils.agendarDefesa(studentEmail, schedulerEmail, dataHoraDefesa, local, banca);

            if (sucesso) {
                UIUtils.showAlert("Sucesso", "Defesa agendada para " + studentName + " em " + dataHoraDefesa.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) + "!");
                Stage stage = (Stage) lblAluno.getScene().getWindow();
                stage.close();
            }


        } catch (DateTimeParseException e) {
            UIUtils.showAlert("Erro de Formato", "A hora deve estar no formato HH:mm (Ex: 14:30).");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro inesperado ao agendar defesa.", e);
            UIUtils.showAlert("Erro Inesperado", "Não foi possível completar o agendamento.");
        }
    }
}