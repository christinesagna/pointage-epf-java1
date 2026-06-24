package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.service.UtilisateurService;
import sn.epf.pointage.ui.util.AlertUtils;

public class UtilisateurFormController {

    @FXML private TextField loginField;
    @FXML private ComboBox<Role> roleBox;
    @FXML private PasswordField passwordField;
    @FXML private Label noteLabel;

    private final UtilisateurService utilisateurService = new UtilisateurService();
    private Utilisateur utilisateur;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        roleBox.getItems().setAll(Role.values());
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        loginField.setText(utilisateur.getLogin());
        roleBox.setValue(utilisateur.getRole());
        noteLabel.setText("Laissez vide pour conserver le mot de passe actuel.");
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    public void save() {
        try {
            String login = required(loginField.getText(), "Login");
            Role role = requiredRole();
            String motDePasse = passwordField.getText();

            if (utilisateur == null) {
                if (motDePasse == null || motDePasse.isBlank()) {
                    throw new IllegalArgumentException("Mot de passe obligatoire.");
                }
                utilisateur = utilisateurService.creerUtilisateur(login, motDePasse, role);
            } else {
                utilisateur.setLogin(login);
                utilisateur.setRole(role);
                utilisateurService.sauvegarderUtilisateur(utilisateur, motDePasse);
            }

            if (onSaved != null) {
                onSaved.run();
            }
            AlertUtils.info("Succès", "Utilisateur enregistré avec succès.");
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

    private Role requiredRole() {
        if (roleBox.getValue() == null) {
            throw new IllegalArgumentException("Rôle obligatoire.");
        }
        return roleBox.getValue();
    }

    private Stage getStage() {
        return (Stage) loginField.getScene().getWindow();
    }

    private void close() {
        getStage().close();
    }
}
