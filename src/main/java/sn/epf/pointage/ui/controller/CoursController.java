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
import sn.epf.pointage.model.Cours;
import sn.epf.pointage.service.CoursService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CoursController {

    @FXML private TextField searchField;
    @FXML private TableView<Cours> coursTable;
    @FXML private TableColumn<Cours, String> codeColumn;
    @FXML private TableColumn<Cours, String> intituleColumn;
    @FXML private TableColumn<Cours, Integer> volumeHoraireColumn;
    @FXML private TableColumn<Cours, String> niveauColumn;
    @FXML private TableColumn<Cours, String> filiereColumn;
    @FXML private TableColumn<Cours, String> semestreColumn;

    private final CoursService coursService = new CoursService();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCode()));
        intituleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIntitule()));
        volumeHoraireColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getVolumeHoraireTotal()));
        niveauColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNiveauEtude()));
        filiereColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFiliere()));
        semestreColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSemestre()));
        refresh();
    }

    @FXML
    public void refresh() {
        coursTable.setItems(FXCollections.observableArrayList(coursService.listerTousCours()));
    }

    @FXML
    public void search() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        List<Cours> filtered = coursService.listerTousCours().stream()
                .filter(c -> keyword.isBlank()
                        || contains(c.getCode(), keyword)
                        || contains(c.getIntitule(), keyword)
                        || contains(c.getFiliere(), keyword)
                        || contains(c.getNiveauEtude(), keyword))
                .collect(Collectors.toList());
        coursTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void openCreateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cours_form.fxml"));
            Parent root = loader.load();
            CoursFormController controller = loader.getController();
            controller.setOnSaved(this::refresh);
            Stage stage = new Stage();
            stage.setTitle("Nouveau cours");
            stage.setScene(new Scene(root, 600, 420));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.error("Erreur", "Impossible d'ouvrir le formulaire du cours.");
        }
    }

    @FXML
    public void deleteSelected() {
        Cours selected = coursTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un cours.");
            return;
        }
        if (!AlertUtils.confirm("Confirmation", "Supprimer ce cours ?")) {
            return;
        }
        coursService.supprimerCours(selected);
        refresh();
        AlertUtils.info("Succès", "Cours supprimé.");
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
