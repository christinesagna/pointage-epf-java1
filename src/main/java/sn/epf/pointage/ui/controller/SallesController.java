package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.epf.pointage.model.Salle;
import sn.epf.pointage.service.SalleService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SallesController {

    @FXML private TextField searchField;
    @FXML private TableView<Salle> salleTable;
    @FXML private TableColumn<Salle, String> nomColumn;
    @FXML private TableColumn<Salle, Integer> capaciteColumn;
    @FXML private TableColumn<Salle, String> batimentColumn;
    @FXML private TableColumn<Salle, String> equipementsColumn;

    private final SalleService salleService = new SalleService();

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));
        capaciteColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCapacite()));
        batimentColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getBatiment()));
        equipementsColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEquipements()));
        refresh();
    }

    @FXML
    public void refresh() {
        salleTable.setItems(FXCollections.observableArrayList(salleService.listerToutesSalles()));
    }

    @FXML
    public void search() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        List<Salle> filtered = salleService.listerToutesSalles().stream()
                .filter(s -> keyword.isBlank()
                        || contains(s.getNom(), keyword)
                        || contains(s.getBatiment(), keyword)
                        || contains(s.getEquipements(), keyword))
                .collect(Collectors.toList());
        salleTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void openCreateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/salle_form.fxml"));
            Parent root = loader.load();
            SalleFormController controller = loader.getController();
            controller.setOnSaved(this::refresh);
            Stage stage = new Stage();
            stage.setTitle("Nouvelle salle");
            stage.setScene(new Scene(root, 560, 380));
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.error("Erreur", "Impossible d'ouvrir le formulaire de la salle.");
        }
    }

    @FXML
    public void deleteSelected() {
        Salle selected = salleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez une salle.");
            return;
        }
        if (!AlertUtils.confirm("Confirmation", "Supprimer cette salle ?")) {
            return;
        }
        salleService.supprimerSalle(selected);
        refresh();
        AlertUtils.info("Succès", "Salle supprimée.");
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
