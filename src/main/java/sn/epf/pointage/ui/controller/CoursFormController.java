package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.epf.pointage.model.Cours;
import sn.epf.pointage.service.CoursService;
import sn.epf.pointage.ui.util.AlertUtils;

public class CoursFormController {

    @FXML private TextField codeField;
    @FXML private TextField intituleField;
    @FXML private TextField volumeHoraireField;
    @FXML private TextField niveauField;
    @FXML private TextField filiereField;
    @FXML private TextField semestreField;

    private final CoursService coursService = new CoursService();
    private Runnable onSaved;

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void save() {
        try {
            Cours cours = new Cours();
            cours.setCode(required(codeField.getText(), "Code"));
            cours.setIntitule(required(intituleField.getText(), "Intitulé"));
            cours.setVolumeHoraireTotal(parseInteger(volumeHoraireField.getText(), "Volume horaire total"));
            cours.setNiveauEtude(niveauField.getText());
            cours.setFiliere(filiereField.getText());
            cours.setSemestre(semestreField.getText());

            coursService.sauvegarderCours(cours);

            if (onSaved != null) {
                onSaved.run();
            }
            AlertUtils.info("Succès", "Cours créé avec succès.");
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
        return (Stage) codeField.getScene().getWindow();
    }

    private void close() {
        getStage().close();
    }
}
