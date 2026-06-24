package sn.epf.pointage.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sn.epf.pointage.ui.controller.MainLayoutController;

import java.io.IOException;
import java.io.StringWriter;

public final class Router {

    private static Stage primaryStage;
    private static MainLayoutController mainLayoutController;

    private Router() {}

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("EPF Africa - Gestion de pointage");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(760);
    }

    public static void showLogin() {
        Parent root = load("/views/login.fxml");
        Scene scene = new Scene(root, 1100, 760);
        applyCss(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource("/views/main-layout.fxml"));
            Parent root = loader.load();
            mainLayoutController = loader.getController();
            Scene scene = new Scene(root, 1280, 800);
            applyCss(scene);
            primaryStage.setScene(scene);
            primaryStage.show();
            mainLayoutController.showDefaultView();
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger main-layout.fxml", e);
        }
    }

    public static void setCenter(String fxml) {
        if (mainLayoutController == null) {
            throw new IllegalStateException("MainLayoutController non initialisé");
        }
        try {
            mainLayoutController.setContent(load(fxml));
        } catch (RuntimeException e) {
            System.err.println("Erreur lors du chargement de la vue : " + fxml);
            e.printStackTrace();
            javafx.scene.control.Label err = new javafx.scene.control.Label("Impossible de charger la vue : " + fxml);
            err.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold; -fx-padding: 20;");
            mainLayoutController.setContent(err);
        }
    }

    public static Stage openModal(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            applyCss(scene);
            Stage modal = new Stage();
            modal.initOwner(primaryStage);
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle(title);
            modal.setScene(scene);
            return modal;
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la vue : " + fxml, e);
        }
    }

    public static FXMLLoader createLoader(String fxml) {
        return new FXMLLoader(Router.class.getResource(fxml));
    }

    private static Parent load(String fxml) {
        try {
            java.net.URL url = Router.class.getResource(fxml);
            if (url == null) {
                // try with/without leading slash
                String alt = fxml.startsWith("/") ? fxml.substring(1) : "/" + fxml;
                url = Router.class.getResource(alt);
            }
            if (url == null) {
                // try adding .fxml
                String withExt = fxml.endsWith(".fxml") ? fxml : (fxml + ".fxml");
                url = Router.class.getResource(withExt);
            }
            if (url == null) {
                throw new IOException("Ressource introuvable: " + fxml);
            }
            return FXMLLoader.load(url);
        } catch (Exception e) {
            // Build an informative Node with the stacktrace so the user sees the error in the UI
            javafx.scene.layout.VBox box = new javafx.scene.layout.VBox();
            javafx.scene.control.Label title = new javafx.scene.control.Label("Impossible de charger la vue : " + fxml);
            title.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold; -fx-padding: 8;");
            javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            ta.setText(sw.toString());
            ta.setEditable(false);
            ta.setWrapText(false);
            javafx.scene.control.ScrollPane sp = new javafx.scene.control.ScrollPane(ta);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            box.getChildren().addAll(title, sp);
            box.setStyle("-fx-padding: 12;");
            return box;
        }
    }

    private static void applyCss(Scene scene) {
        String css = Router.class.getResource("/css/app.css").toExternalForm();
        scene.getStylesheets().setAll(css);
    }
}
