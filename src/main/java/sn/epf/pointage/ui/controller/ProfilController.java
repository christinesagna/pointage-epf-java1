package sn.epf.pointage.ui.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.File;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.service.AuthService;
import sn.epf.pointage.ui.util.AlertUtils;

public class ProfilController {

    @FXML private ImageView photoView;
    @FXML private Label nomLabel;
    @FXML private Label matriculeLabel;
    @FXML private VBox profilContainer;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        afficherProfil();
    }

    private void afficherProfil() {
        Utilisateur user = authService.currentUser();
        if (user == null) {
            AlertUtils.warning("Erreur", "Aucun utilisateur connecté.");
            return;
        }
        Professeur prof = user.getProfesseurLie();

        // Titre et en-têtes du bloc photo — utiliser le login si pas de Professeur lié
        nomLabel.setText(prof != null ? prof.getNomComplet() : user.getLogin());
        matriculeLabel.setText(prof != null && prof.getMatricule() != null ? prof.getMatricule() : "");
        // Charger la photo si disponible
        if (prof.getPhoto() != null && !prof.getPhoto().isBlank()) {
            try {
                File f = new File(prof.getPhoto());
                Image img = f.exists() ? new Image(f.toURI().toString()) : new Image(getClass().getResourceAsStream(prof.getPhoto()));
                photoView.setImage(img);
            } catch (Exception e) {
                // ignore; laisser image vide
            }
        }

        // Effacer le contenu précédent
        profilContainer.getChildren().clear();

        // Titre
        Label titre = new Label("Mon Profil");
        titre.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        profilContainer.getChildren().add(titre);

        // Si aucun professeur lié, afficher message informatif et quitter après avoir rempli les en-têtes
        if (prof == null) {
            Label info = new Label("Aucun professeur lié à ce compte.");
            info.setStyle("-fx-text-fill: #666;");
            profilContainer.getChildren().add(info);
            profilContainer.setSpacing(20);
            return;
        }

        // Grille avec les informations
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 20;");

        int row = 0;

        // Matricule
        ajouterLigneInfo(grid, "Matricule:", prof.getMatricule(), row++);

        // Nom
        ajouterLigneInfo(grid, "Nom:", prof.getNom(), row++);

        // Prénom
        ajouterLigneInfo(grid, "Prénom:", prof.getPrenom(), row++);

        // Email
        ajouterLigneInfo(grid, "Email:", prof.getEmail() != null ? prof.getEmail() : "Non renseigné", row++);

        // Téléphone
        ajouterLigneInfo(grid, "Téléphone:", prof.getTelephone() != null ? prof.getTelephone() : "Non renseigné", row++);

        // Type de contrat
        ajouterLigneInfo(grid, "Type de contrat:", prof.getTypeContrat() != null ? prof.getTypeContrat().name() : "Non défini", row++);

        // Statut
        String statut = prof.isActif() ? "✓ Actif" : "✕ Inactif";
        ajouterLigneInfo(grid, "Statut:", statut, row++);

        // Taux horaire
        ajouterLigneInfo(grid, "Taux horaire (XOF):", prof.getTauxHoraireXOF() != null ? prof.getTauxHoraireXOF().toPlainString() : "—", row++);

        profilContainer.getChildren().add(grid);

        // Espace
        profilContainer.setSpacing(20);
    }

    private void ajouterLigneInfo(GridPane grid, String label, String valeur, int row) {
        Label labelCtrl = new Label(label);
        labelCtrl.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        Label valueCtrl = new Label(valeur != null ? valeur : "—");
        valueCtrl.setStyle("-fx-text-fill: #666;");
        valueCtrl.setWrapText(true);

        grid.add(labelCtrl, 0, row);
        grid.add(valueCtrl, 1, row);
    }
}
