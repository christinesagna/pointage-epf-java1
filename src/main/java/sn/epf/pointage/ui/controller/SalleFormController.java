package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.epf.pointage.model.Salle;
import sn.epf.pointage.service.SalleService;
import sn.epf.pointage.ui.util.AlertUtils;

public class SalleFormController {

    @FXML private TextField nomField;
    @FXML private TextField capaciteField;
    @FXML private TextField batimentField;
    @FXML private TextField equipementsField;

    private final SalleService salleService = new SalleService();
    private Runnable onSaved;

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void save() {
        try {
            Salle salle = new Salle();
            salle.setNom(required(nomField.getText(), "Nom"));
            salle.setCapacite(parseInteger(capaciteField.getText(), "Capacité"));
            salle.setBatiment(batimentField.getText());
            salle.setEquipements(equipementsField.getText());

            salleService.sauvegarderSalle(salle);

            if (onSaved != null) {
                onSaved.run();
            }
            AlertUtils.info("Succès", "Salle créée avec succès.");
            close();
        } catch (Exception e) {
            AlertUtils.error("Erreur", e.getMessage());
        }
    }

    @FXML
    public void cancel() {
        close();
    }

    private String required(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " obligatoire.");
        }
        return value.trim();
    }

    private Integer parseInteger(String value, String label) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " doit être un nombre entier.");
        }
    }

    private Stage getStage() {
        return (Stage) nomField.getScene().getWindow();
    }

    private void close() {
        getStage().close();
    }
}
