package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.service.AuthService;
import sn.epf.pointage.ui.Router;

public class MainLayoutController {

    @FXML private Label currentUserLabel;
    @FXML private Label roleLabel;
    @FXML private StackPane contentPane;
    @FXML private Button dashboardButton;
    @FXML private Button professeursButton;
    @FXML private Button coursButton;
    @FXML private Button sallesButton;
    @FXML private Button absenceRetardsButton;
    @FXML private Button pointageButton;
    @FXML private Button rapportsButton;
    @FXML private Button profilButton;
    @FXML private Button utilisateursButton;
    @FXML private Button auditButton;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        Utilisateur user = authService.currentUser();
        if (user != null) {
            currentUserLabel.setText(user.getLogin());
            roleLabel.setText(user.getRole().name());
            if (user.getRole() == Role.PROFESSEUR) {
                dashboardButton.setVisible(false);
                dashboardButton.setManaged(false);
                professeursButton.setVisible(false);
                professeursButton.setManaged(false);
                coursButton.setVisible(false);
                coursButton.setManaged(false);
                sallesButton.setVisible(false);
                sallesButton.setManaged(false);
                absenceRetardsButton.setVisible(false);
                absenceRetardsButton.setManaged(false);
                utilisateursButton.setVisible(false);
                utilisateursButton.setManaged(false);
                auditButton.setVisible(false);
                auditButton.setManaged(false);
                profilButton.setVisible(true);
                profilButton.setManaged(true);
            } else if (user.getRole() == Role.SCOLARITE) {
                absenceRetardsButton.setVisible(true);
                absenceRetardsButton.setManaged(true);
                pointageButton.setVisible(false);
                pointageButton.setManaged(false);
                utilisateursButton.setVisible(false);
                utilisateursButton.setManaged(false);
                auditButton.setVisible(false);
                auditButton.setManaged(false);
                profilButton.setVisible(false);
                profilButton.setManaged(false);
            } else if (user.getRole() == Role.ADMIN) {
                pointageButton.setVisible(false);
                pointageButton.setManaged(false);
                absenceRetardsButton.setVisible(false);
                absenceRetardsButton.setManaged(false);
                profilButton.setVisible(false);
                profilButton.setManaged(false);
            }
        }
    }

    public void showDefaultView() {
        Utilisateur user = authService.currentUser();
        if (user != null && user.getRole() == Role.PROFESSEUR) {
            showPlanning();
        } else {
            showDashboard();
        }
    }

    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

    @FXML
    public void showDashboard() {
        Utilisateur user = authService.currentUser();
        if (user != null && user.getRole() == Role.PROFESSEUR) {
            return;
        }
        Router.setCenter("/views/dashboard.fxml");
    }

    @FXML
    public void showProfesseurs() {
        Router.setCenter("/views/professeurs.fxml");
    }

    @FXML
    public void showCours() {
        Router.setCenter("/views/cours.fxml");
    }

    @FXML
    public void showSalles() {
        Router.setCenter("/views/salles.fxml");
    }

    @FXML
    public void showPlanning() {
        Router.setCenter("/views/planning.fxml");
    }

    @FXML
    public void showPointage() {
        Router.setCenter("/views/pointage.fxml");
    }

    @FXML
    public void showRapports() {
        Router.setCenter("/views/rapports.fxml");
    }

    @FXML
    public void showUtilisateurs() {
        Router.setCenter("/views/utilisateurs.fxml");
    }

    @FXML
    public void showAuditConnexions() {
        Router.setCenter("/views/audit_connexions.fxml");
    }

    @FXML
    public void showAbsenceRetards() {
        Router.setCenter("/views/absence_retards.fxml");
    }

    @FXML
    public void showProfil() {
        Router.setCenter("/views/profil.fxml");
    }

    @FXML
    public void logout() {
        authService.logout("127.0.0.1");
        Router.showLogin();
    }
}
