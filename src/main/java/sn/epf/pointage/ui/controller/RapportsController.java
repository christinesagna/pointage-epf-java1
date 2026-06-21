package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.RapportMensuel;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.service.AuthService;
import sn.epf.pointage.service.ProfesseurService;
import sn.epf.pointage.service.RapportService;
import sn.epf.pointage.service.export.RapportPdfExportService;
import sn.epf.pointage.ui.util.AlertUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class RapportsController {

    @FXML private ComboBox<Professeur> professeurBox;
    @FXML private Spinner<Integer> moisSpinner;
    @FXML private Spinner<Integer> anneeSpinner;
    @FXML private TableView<RapportMensuel> rapportTable;
    @FXML private TableColumn<RapportMensuel, Integer> moisColumn;
    @FXML private TableColumn<RapportMensuel, Integer> anneeColumn;
    @FXML private TableColumn<RapportMensuel, String> professeurColumn;
    @FXML private TableColumn<RapportMensuel, String> heuresColumn;
    @FXML private TableColumn<RapportMensuel, String> montantColumn;
    @FXML private TableColumn<RapportMensuel, String> statutColumn;

    private final AuthService authService = new AuthService();
    private final ProfesseurService professeurService = new ProfesseurService();
    private final RapportService rapportService = new RapportService();
    private final RapportPdfExportService exportService = new RapportPdfExportService();

    @FXML
    public void initialize() {
        moisSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue()));
        anneeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2024, 2035, LocalDate.now().getYear()));
        moisColumn.setCellValueFactory(new PropertyValueFactory<>("mois"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("annee"));
        professeurColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getProfesseur().getNomComplet()));
        heuresColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getHeuresRealisees().toPlainString()));
        montantColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMontantXOF().toPlainString()));
        statutColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatut().name()));
        setupProfesseurSelector();
        refresh();
    }

    private void setupProfesseurSelector() {
        Utilisateur user = authService.currentUser();
        if (user != null && user.getRole() == Role.PROFESSEUR && user.getProfesseurLie() != null) {
            professeurBox.getItems().setAll(user.getProfesseurLie());
            professeurBox.getSelectionModel().selectFirst();
            professeurBox.setDisable(true);
        } else {
            professeurBox.setItems(FXCollections.observableArrayList(professeurService.listerProfesseursActifs()));
            if (!professeurBox.getItems().isEmpty()) {
                professeurBox.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    public void refresh() {
        Professeur professeur = professeurBox.getValue();
        if (professeur == null) {
            rapportTable.getItems().clear();
            return;
        }
        List<RapportMensuel> rapports = rapportService.listerRapportsParProfesseur(professeur.getId());
        rapportTable.setItems(FXCollections.observableArrayList(rapports));
    }

    @FXML
    public void genererRapport() {
        try {
            Professeur professeur = selectedProfesseur();
            rapportService.genererRapportMensuel(professeur, moisSpinner.getValue(), anneeSpinner.getValue());
            refresh();
            AlertUtils.info("Succès", "Rapport généré avec succès.");
        } catch (Exception e) {
            AlertUtils.error("Erreur génération", e.getMessage());
        }
    }

    @FXML
    public void validerRapport() {
        RapportMensuel rapport = rapportTable.getSelectionModel().getSelectedItem();
        if (rapport == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un rapport.");
            return;
        }
        rapportService.validerRapport(rapport);
        refresh();
        AlertUtils.info("Succès", "Rapport validé.");
    }

    @FXML
    public void exporterPdf() {
        RapportMensuel rapport = rapportTable.getSelectionModel().getSelectedItem();
        if (rapport == null) {
            AlertUtils.warning("Sélection requise", "Sélectionnez un rapport.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exporter le rapport PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("rapport_" + rapport.getProfesseur().getMatricule() + "_" + rapport.getMois() + "_" + rapport.getAnnee() + ".pdf");
        File file = chooser.showSaveDialog(rapportTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            exportService.exporter(rapport, file.toPath());
            AlertUtils.info("Export terminé", "PDF généré : " + file.getAbsolutePath());
        } catch (Exception e) {
            AlertUtils.error("Erreur export", e.getMessage());
        }
    }

    private Professeur selectedProfesseur() {
        Professeur professeur = professeurBox.getValue();
        if (professeur == null) {
            throw new IllegalStateException("Sélectionnez un professeur.");
        }
        return professeur;
    }
}
