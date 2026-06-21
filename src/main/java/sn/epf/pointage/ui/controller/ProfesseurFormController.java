package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.enums.TypeContrat;
import sn.epf.pointage.service.ProfesseurService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProfesseurFormController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<TypeContrat> contratBox;
    @FXML private TextField tauxField;
    @FXML private DatePicker dateEmbauchePicker;
    @FXML private TextField photoField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private final ProfesseurService professeurService = new ProfesseurService();
    private Runnable onSaved;

    @FXML
    public void initialize() {
        contratBox.getItems().setAll(TypeContrat.values());
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void browsePhoto() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir une photo");
        File file = chooser.showOpenDialog(getStage());
        if (file != null) {
            photoField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void save() {
        try {
            Professeur professeur = new Professeur();
            professeur.setNom(required(nomField.getText(), "Nom"));
            professeur.setPrenom(required(prenomField.getText(), "Prénom"));
            professeur.setEmail(required(emailField.getText(), "Email"));
            professeur.setTelephone(telephoneField.getText());
            professeur.setTypeContrat(requiredContrat());
            professeur.setTauxHoraireXOF(new BigDecimal(required(tauxField.getText(), "Taux horaire")));
            professeur.setDateEmbauche(dateEmbauchePicker.getValue() == null ? LocalDate.now() : dateEmbauchePicker.getValue());
            professeur.setPhoto(photoField.getText());
            professeur.setActif(true);

            professeurService.enrôlerProfesseur(
                    professeur,
                    required(loginField.getText(), "Login"),
                    required(passwordField.getText(), "Mot de passe")
            );

            if (onSaved != null) {
                onSaved.run();
            }
            AlertUtils.info("Succès", "Professeur créé avec succès.");
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

    private TypeContrat requiredContrat() {
        if (contratBox.getValue() == null) {
            throw new IllegalArgumentException("Type de contrat obligatoire.");
        }
        return contratBox.getValue();
    }

    private Stage getStage() {
        return (Stage) nomField.getScene().getWindow();
    }

    private void close() {
        getStage().close();
    }
}
