package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.epf.pointage.model.Pointage;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.service.AuthService;
import sn.epf.pointage.service.PlanningService;
import sn.epf.pointage.service.PointageService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PointageController {

    @FXML private Label contextLabel;
    @FXML private TableView<SeancePlanifiee> seancesTable;
    @FXML private TableColumn<SeancePlanifiee, String> dateColumn;
    @FXML private TableColumn<SeancePlanifiee, String> coursColumn;
    @FXML private TableColumn<SeancePlanifiee, Integer> dureeColumn;
    @FXML private TableColumn<SeancePlanifiee, String> statutColumn;

    private final AuthService authService = new AuthService();
    private final PlanningService planningService = new PlanningService();
    private final PointageService pointageService = new PointageService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDateHeure().format(formatter)));
        coursColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAssignation().getCours().getIntitule()));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("dureeMinutes"));
        statutColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatut().name()));
        refresh();
    }

    @FXML
    public void refresh() {
        Utilisateur user = authService.currentUser();
        if (user == null || user.getRole() != Role.PROFESSEUR || user.getProfesseurLie() == null) {
            contextLabel.setText("Le module de pointage est destiné aux professeurs connectés.");
            seancesTable.getItems().clear();
            return;
        }
        Professeur professeur = user.getProfesseurLie();
        contextLabel.setText("Pointage du jour pour " + professeur.getNomComplet());
        seancesTable.setItems(FXCollections.observableArrayList(planningService.listerSeancesDuJour(professeur.getId())));
    }

    @FXML
    public void pointerDebut() {
        pointe(true);
    }

    @FXML
    public void pointerFin() {
        pointe(false);
    }

    private void pointe(boolean debut) {
        Utilisateur user = authService.currentUser();
        if (user == null || user.getProfesseurLie() == null) {
            AlertUtils.warning("Accès refusé", "Aucun professeur lié à cette session.");
            return;
        }
        SeancePlanifiee selected = seancesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez une séance.");
            return;
        }
        Pointage pointage = debut
                ? pointageService.pointerDebut(user.getProfesseurLie(), selected, LocalDateTime.now())
                : pointageService.pointerFin(user.getProfesseurLie(), selected, LocalDateTime.now());
        AlertUtils.info("Résultat du pointage", pointage.getStatut().name() + (pointage.getObservations() != null ? " - " + pointage.getObservations() : ""));
        refresh();
    }
}
