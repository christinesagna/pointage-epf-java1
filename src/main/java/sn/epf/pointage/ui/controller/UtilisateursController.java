package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.service.UtilisateurService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class UtilisateursController {

    @FXML private TextField searchField;
    @FXML private TableView<Utilisateur> utilisateurTable;
    @FXML private TableColumn<Utilisateur, String> loginColumn;
    @FXML private TableColumn<Utilisateur, String> roleColumn;
    @FXML private TableColumn<Utilisateur, String> professeurColumn;

    private final UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        loginColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLogin()));
        roleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getRole() == null ? "" : cell.getValue().getRole().name()));
        professeurColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getProfesseurLie() == null ? "" : cell.getValue().getProfesseurLie().getMatricule()));
        refresh();
    }

    @FXML
    public void refresh() {
        utilisateurTable.setItems(FXCollections.observableArrayList(utilisateurService.listerTousUtilisateurs()));
    }

    @FXML
    public void search() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        List<Utilisateur> filtered = utilisateurService.listerTousUtilisateurs().stream()
                .filter(u -> keyword.isBlank() || contains(u.getLogin(), keyword)
                        || contains(u.getRole() == null ? "" : u.getRole().name(), keyword)
                        || contains(u.getProfesseurLie() == null ? "" : u.getProfesseurLie().getMatricule(), keyword))
                .collect(Collectors.toList());
        utilisateurTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void openCreateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/utilisateur_form.fxml"));
            Parent root = loader.load();
            UtilisateurFormController controller = loader.getController();
            controller.setOnSaved(this::refresh);
            Stage stage = new Stage();
            stage.setTitle("Nouvel utilisateur");
            stage.setScene(new Scene(root, 500, 320));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.error("Erreur", "Impossible d'ouvrir le formulaire utilisateur.");
        }
    }

    @FXML
    public void openEditForm() {
        Utilisateur selected = utilisateurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un utilisateur.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/utilisateur_form.fxml"));
            Parent root = loader.load();
            UtilisateurFormController controller = loader.getController();
            controller.setUtilisateur(selected);
            controller.setOnSaved(this::refresh);
            Stage stage = new Stage();
            stage.setTitle("Modifier l'utilisateur");
            stage.setScene(new Scene(root, 500, 320));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.error("Erreur", "Impossible d'ouvrir le formulaire utilisateur.");
        }
    }

    @FXML
    public void deleteSelected() {
        Utilisateur selected = utilisateurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un utilisateur.");
            return;
        }
        if (!AlertUtils.confirm("Confirmation", "Supprimer ce compte utilisateur ?")) {
            return;
        }
        utilisateurService.supprimerUtilisateur(selected);
        refresh();
        AlertUtils.info("Succès", "Utilisateur supprimé avec succès.");
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
