package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.epf.pointage.model.Pointage;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.service.AbsenceRetardService;
import sn.epf.pointage.service.ProfesseurService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AbsenceRetardsController {

    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<Professeur> professeurFilterBox;
    @FXML private TableView<Pointage> absenceTable;
    @FXML private TableColumn<Pointage, String> dateColumn;
    @FXML private TableColumn<Pointage, String> professeurColumn;
    @FXML private TableColumn<Pointage, String> heureColumn;
    @FXML private TableColumn<Pointage, String> typeColumn;
    @FXML private TableColumn<Pointage, String> statutColumn;
    @FXML private TableColumn<Pointage, String> observationsColumn;
    @FXML private Label statistiquesLabel;

    private final AbsenceRetardService absenceRetardService = new AbsenceRetardService();
    private final ProfesseurService professeurService = new ProfesseurService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        // Configuration des colonnes
        dateColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getHeurePointage().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );
        professeurColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getProfesseur().getNomComplet()
                )
        );
        heureColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getHeurePointage().format(formatter)
                )
        );
        typeColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getTypePointage().name()
                )
        );
        statutColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getStatut().name()
                )
        );
        observationsColumn.setCellValueFactory(new PropertyValueFactory<>("observations"));

        // Configuration des dates par défaut
        LocalDate today = LocalDate.now();
        dateDebutPicker.setValue(today.minusMonths(1));
        dateFinPicker.setValue(today);

        // Configuration du sélecteur de professeur
        setupProfesseurFilter();

        refresh();
    }

    private void setupProfesseurFilter() {
        List<Professeur> professeurs = professeurService.listerProfesseursActifs();
        professeurFilterBox.setItems(FXCollections.observableArrayList(professeurs));
        professeurFilterBox.getItems().add(0, null);
        professeurFilterBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Professeur prof, boolean empty) {
                super.updateItem(prof, empty);
                setText(empty ? null : prof == null ? "Tous les professeurs" : prof.getNomComplet());
            }
        });
        professeurFilterBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Professeur prof, boolean empty) {
                super.updateItem(prof, empty);
                setText(empty ? null : prof == null ? "Tous les professeurs" : prof.getNomComplet());
            }
        });
        professeurFilterBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void refresh() {
        LocalDate debut = dateDebutPicker.getValue();
        LocalDate fin = dateFinPicker.getValue();

        if (debut == null || fin == null) {
            return;
        }

        if (debut.isAfter(fin)) {
            showAlert("Erreur", "La date de début doit être avant la date de fin.");
            return;
        }

        List<Pointage> retards;
        Professeur professeurFilter = professeurFilterBox.getValue();

        if (professeurFilter == null) {
            retards = absenceRetardService.listerRetardsPeriode(debut, fin);
        } else {
            retards = absenceRetardService.listerRetardsProfesseur(professeurFilter.getId(), debut, fin);
        }

        absenceTable.setItems(FXCollections.observableArrayList(retards));
        updateStatistiques(retards, debut, fin);
    }

    private void updateStatistiques(List<Pointage> retards, LocalDate debut, LocalDate fin) {
        int total = retards.size();
        long retardsCount = retards.stream()
                .filter(p -> p.getStatut().name().equals("EN_RETARD"))
                .count();
        long inactiviteCount = retards.stream()
                .filter(p -> p.getStatut().name().equals("PROF_INACTIF"))
                .count();

        String stats = String.format(
                "Période: %s à %s | Total: %d | Retards: %d | Inactivités: %d",
                debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                total,
                retardsCount,
                inactiviteCount
        );
        statistiquesLabel.setText(stats);
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
