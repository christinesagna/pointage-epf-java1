package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.service.ProfesseurService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProfesseursController {

    @FXML private TextField searchField;
    @FXML private TableView<Professeur> professeurTable;
    @FXML private TableColumn<Professeur, String> matriculeColumn;
    @FXML private TableColumn<Professeur, String> nomColumn;
    @FXML private TableColumn<Professeur, String> prenomColumn;
    @FXML private TableColumn<Professeur, String> emailColumn;
    @FXML private TableColumn<Professeur, String> telephoneColumn;
    @FXML private TableColumn<Professeur, String> contratColumn;
    @FXML private TableColumn<Professeur, Boolean> actifColumn;

    private final ProfesseurService professeurService = new ProfesseurService();

    @FXML
    public void initialize() {
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        contratColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getTypeContrat() == null ? "" : cell.getValue().getTypeContrat().name()
        ));
        actifColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleBooleanProperty(cell.getValue().isActif()));
        refresh();
    }

    @FXML
    public void refresh() {
        professeurTable.setItems(FXCollections.observableArrayList(professeurService.listerTousProfesseurs()));
    }

    @FXML
    public void search() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        List<Professeur> filtered = professeurService.listerTousProfesseurs().stream()
                .filter(p -> keyword.isBlank()
                        || contains(p.getMatricule(), keyword)
                        || contains(p.getNom(), keyword)
                        || contains(p.getPrenom(), keyword)
                        || contains(p.getEmail(), keyword))
                .collect(Collectors.toList());
        professeurTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void openCreateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/prof_form.fxml"));
            BorderPane root = loader.load();
            ProfesseurFormController controller = loader.getController();
            controller.setOnSaved(this::refresh);
            Stage stage = new Stage();
            stage.setTitle("Nouveau professeur");
            stage.setScene(new Scene(root, 700, 560));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.error("Erreur", "Impossible d'ouvrir le formulaire professeur.");
        }
    }

    @FXML
    public void deactivateSelected() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un professeur.");
            return;
        }
        if (!AlertUtils.confirm("Confirmation", "Désactiver ce professeur ?")) {
            return;
        }
        professeurService.desactiverProfesseur(selected);
        refresh();
        AlertUtils.info("Succès", "Professeur désactivé avec succès.");
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
