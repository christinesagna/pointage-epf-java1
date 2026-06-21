package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sn.epf.pointage.service.AuthService;
import sn.epf.pointage.ui.Router;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label feedbackLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLogin() {
        feedbackLabel.setText("");
        String login = loginField.getText() == null ? "" : loginField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("Veuillez renseigner le login et le mot de passe.");
            return;
        }

        boolean ok = authService.login(login, password, "127.0.0.1");
        if (!ok) {
            feedbackLabel.setText("Identifiants invalides.");
            return;
        }
        Router.showMainLayout();
    }
}
