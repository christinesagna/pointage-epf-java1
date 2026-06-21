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
    @FXML private Button professeursButton;
    @FXML private Button pointageButton;
    @FXML private Button rapportsButton;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        Utilisateur user = authService.currentUser();
        if (user != null) {
            currentUserLabel.setText(user.getLogin());
            roleLabel.setText(user.getRole().name());
            if (user.getRole() == Role.PROFESSEUR) {
                professeursButton.setVisible(false);
                professeursButton.setManaged(false);
            }
            if (user.getRole() == Role.ADMIN) {
                pointageButton.setVisible(false);
                pointageButton.setManaged(false);
            }
        }
    }

    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

    @FXML
    public void showDashboard() {
        Router.setCenter("/views/dashboard.fxml");
    }

    @FXML
    public void showProfesseurs() {
        Router.setCenter("/views/professeurs.fxml");
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
    public void logout() {
        authService.logout("127.0.0.1");
        Router.showLogin();
    }
}
