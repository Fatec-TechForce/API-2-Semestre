package com.example.tgcontrol.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Notification {
    private final IntegerProperty notificationId;
    private final StringProperty userEmail;
    private final ObjectProperty<LocalDateTime> timestamp;
    private final StringProperty content;
    private final StringProperty relatedTaskStudentEmail;
    private final IntegerProperty relatedTaskSequenceOrder;
    private final BooleanProperty isRead;

    // Construtor para carregar do banco de dados
    public Notification(int notificationId, String userEmail, LocalDateTime timestamp, String content,
                        String relatedTaskStudentEmail, int relatedTaskSequenceOrder, boolean isRead) {
        this.notificationId = new SimpleIntegerProperty(notificationId);
        this.userEmail = new SimpleStringProperty(userEmail);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.content = new SimpleStringProperty(content);
        this.relatedTaskStudentEmail = new SimpleStringProperty(relatedTaskStudentEmail);
        this.relatedTaskSequenceOrder = new SimpleIntegerProperty(relatedTaskSequenceOrder);
        this.isRead = new SimpleBooleanProperty(isRead);
    }

    // Construtor para criar nova notificação (sem ID e com isRead = false)
    public Notification(String userEmail, String content, String relatedTaskStudentEmail, int relatedTaskSequenceOrder) {
        this.notificationId = new SimpleIntegerProperty(0); // Será gerado pelo banco
        this.userEmail = new SimpleStringProperty(userEmail);
        this.timestamp = new SimpleObjectProperty<>(LocalDateTime.now()); // Será atualizado pelo banco
        this.content = new SimpleStringProperty(content);
        this.relatedTaskStudentEmail = new SimpleStringProperty(relatedTaskStudentEmail);
        this.relatedTaskSequenceOrder = new SimpleIntegerProperty(relatedTaskSequenceOrder);
        this.isRead = new SimpleBooleanProperty(false);
    }

    // Getters para os valores
    public int getNotificationId() { return notificationId.get(); }
    public String getUserEmail() { return userEmail.get(); }
    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public String getContent() { return content.get(); }
    public String getRelatedTaskStudentEmail() { return relatedTaskStudentEmail.get(); }
    public int getRelatedTaskSequenceOrder() { return relatedTaskSequenceOrder.get(); }
    public boolean isIsRead() { return isRead.get(); }

    // Setters
    public void setIsRead(boolean isRead) { this.isRead.set(isRead); }

    // Property Getters para TableView
    public IntegerProperty notificationIdProperty() { return notificationId; }
    public StringProperty userEmailProperty() { return userEmail; }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
    public StringProperty contentProperty() { return content; }
    public StringProperty relatedTaskStudentEmailProperty() { return relatedTaskStudentEmail; }
    public IntegerProperty relatedTaskSequenceOrderProperty() { return relatedTaskSequenceOrder; }
    public BooleanProperty isReadProperty() { return isRead; }
}