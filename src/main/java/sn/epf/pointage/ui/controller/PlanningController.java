package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import sn.epf.pointage.model.*;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PlanningController {

    @FXML private ComboBox<Professeur> professeurBox;
    @FXML private Spinner<Integer> moisSpinner;
    @FXML private Spinner<Integer> anneeSpinner;
    @FXML private TableView<SeancePlanifiee> planningTable;
    @FXML private TableColumn<SeancePlanifiee, String> dateColumn;
    @FXML private TableColumn<SeancePlanifiee, String> coursColumn;
    @FXML private TableColumn<SeancePlanifiee, String> salleColumn;
    @FXML private TableColumn<SeancePlanifiee, Integer> dureeColumn;
    @FXML private TableColumn<SeancePlanifiee, String> statutColumn;
    @FXML private Button btnNouvelleSeance;

    private final PlanningService planningService = new PlanningService();
    private final ProfesseurService professeurService = new ProfesseurService();
    private final AuthService authService = new AuthService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        moisSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, java.time.LocalDate.now().getMonthValue()));
        anneeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2024, 2035, java.time.LocalDate.now().getYear()));

        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDateHeure().format(formatter)));
        coursColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAssignation().getCours().getIntitule()));
        salleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAssignation().getSalle().getNom()));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("dureeMinutes"));
        statutColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatut().name()));

        setupProfesseurSelector();
        refresh();
    }

    private void setupProfesseurSelector() {
        Utilisateur currentUser = authService.currentUser();
        if (currentUser != null && currentUser.getRole() == Role.PROFESSEUR && currentUser.getProfesseurLie() != null) {
            professeurBox.getItems().setAll(currentUser.getProfesseurLie());
            professeurBox.getSelectionModel().selectFirst();
            professeurBox.setDisable(true);
            btnNouvelleSeance.setVisible(false);
        } else {
            List<Professeur> professeurs = professeurService.listerProfesseursActifs();
            professeurBox.getItems().setAll(professeurs);
            professeurBox.getItems().add(0, null);
            professeurBox.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(Professeur prof, boolean empty) {
                    super.updateItem(prof, empty);
                    setText(empty ? null : prof == null ? "Tout" : prof.toString());
                }
            });
            professeurBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Professeur prof, boolean empty) {
                    super.updateItem(prof, empty);
                    setText(empty ? null : prof == null ? "Tout" : prof.toString());
                }
            });
            professeurBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void refresh() {
        Professeur professeur = professeurBox.getValue();
        Long professeurId = professeur != null ? professeur.getId() : null;
        List<SeancePlanifiee> seances = planningService.listerSeancesDuMois(
                professeurId, moisSpinner.getValue(), anneeSpinner.getValue()
        );
        planningTable.setItems(FXCollections.observableArrayList(seances));
    }

    @FXML
    public void ouvrirFormulaireSeance() {
        List<Professeur> profs = professeurService.listerProfesseursActifs();
        List<Cours> cours = planningService.listerTousCours();
        List<Salle> salles = planningService.listerToutesSalles();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Séance");
        dialog.setHeaderText("Créer une assignation et une séance");

        ButtonType confirmerBtn = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmerBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Professeur> profBox = new ComboBox<>(FXCollections.observableArrayList(profs));
        ComboBox<Cours> coursBox = new ComboBox<>(FXCollections.observableArrayList(cours));
        ComboBox<Salle> salleBox = new ComboBox<>(FXCollections.observableArrayList(salles));
        TextField anneeField = new TextField("2025-2026");
        TextField heuresPrevuesField = new TextField("45");
        DatePicker datePicker = new DatePicker(java.time.LocalDate.now());
        TextField heureField = new TextField("08:00");
        TextField dureeField = new TextField("90");

        if (professeurBox.getValue() != null) {
            profBox.setValue(professeurBox.getValue());
        }

        grid.add(new Label("Professeur:"), 0, 0);
        grid.add(profBox, 1, 0);
        grid.add(new Label("Cours:"), 0, 1);
        grid.add(coursBox, 1, 1);
        grid.add(new Label("Salle:"), 0, 2);
        grid.add(salleBox, 1, 2);
        grid.add(new Label("Année académique:"), 0, 3);
        grid.add(anneeField, 1, 3);
        grid.add(new Label("Heures prévues:"), 0, 4);
        grid.add(heuresPrevuesField, 1, 4);
        grid.add(new Label("Date séance:"), 0, 5);
        grid.add(datePicker, 1, 5);
        grid.add(new Label("Heure (HH:mm):"), 0, 6);
        grid.add(heureField, 1, 6);
        grid.add(new Label("Durée (min):"), 0, 7);
        grid.add(dureeField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == confirmerBtn) {
            try {
                if (profBox.getValue() == null || coursBox.getValue() == null || salleBox.getValue() == null) {
                    showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                    return;
                }

                String[] heureParts = heureField.getText().split(":");
                LocalDateTime dateHeure = datePicker.getValue().atTime(
                        Integer.parseInt(heureParts[0]),
                        Integer.parseInt(heureParts[1])
                );

                planningService.creerAssignationEtSeance(
                        profBox.getValue(),
                        coursBox.getValue(),
                        salleBox.getValue(),
                        anneeField.getText(),
                        new BigDecimal(heuresPrevuesField.getText()),
                        dateHeure,
                        Integer.parseInt(dureeField.getText())
                );

                showAlert("Succès", "Séance créée avec succès !");
                refresh();

            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la création : " + e.getMessage());
            }
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(titre.equals("Erreur") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}