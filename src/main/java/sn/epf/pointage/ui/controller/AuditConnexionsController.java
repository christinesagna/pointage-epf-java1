package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sn.epf.pointage.model.AuditConnexion;
import sn.epf.pointage.service.AuditConnexionService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AuditConnexionsController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> actionBox;
    @FXML private DatePicker debutPicker;
    @FXML private DatePicker finPicker;
    @FXML private TableView<AuditConnexion> auditTable;
    @FXML private TableColumn<AuditConnexion, String> loginColumn;
    @FXML private TableColumn<AuditConnexion, LocalDateTime> horodatageColumn;
    @FXML private TableColumn<AuditConnexion, String> ipColumn;
    @FXML private TableColumn<AuditConnexion, String> actionColumn;

    private final AuditConnexionService auditConnexionService = new AuditConnexionService();

    @FXML
    public void initialize() {
        actionBox.getItems().setAll("TOUS", "CONNEXION", "DECONNEXION");
        actionBox.setValue("TOUS");

        loginColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLogin()));
        horodatageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("horodatage"));
        ipColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAdresseIp()));
        actionColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAction()));

        refresh();
    }

    @FXML
    public void refresh() {
        auditTable.setItems(FXCollections.observableArrayList(auditConnexionService.listerTousAudits()));
    }

    @FXML
    public void search() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedAction = actionBox.getValue();
        LocalDate debut = debutPicker.getValue();
        LocalDate fin = finPicker.getValue();

        List<AuditConnexion> audits = auditConnexionService.listerTousAudits().stream()
                .filter(a -> keyword.isBlank() || contains(a.getLogin(), keyword)
                        || contains(a.getAdresseIp(), keyword)
                        || contains(a.getAction(), keyword))
                .filter(a -> "TOUS" .equals(selectedAction) || a.getAction().equals(selectedAction))
                .filter(a -> dateInRange(a.getHorodatage().toLocalDate(), debut, fin))
                .collect(Collectors.toList());

        auditTable.setItems(FXCollections.observableArrayList(audits));
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

    private boolean dateInRange(LocalDate date, LocalDate debut, LocalDate fin) {
        if (debut != null && date.isBefore(debut)) {
            return false;
        }
        if (fin != null && date.isAfter(fin)) {
            return false;
        }
        return true;
    }
}
