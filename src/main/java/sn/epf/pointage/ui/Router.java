package sn.epf.pointage.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sn.epf.pointage.ui.controller.MainLayoutController;

import java.io.IOException;

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
            mainLayoutController.showDashboard();
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger main-layout.fxml", e);
        }
    }

    public static void setCenter(String fxml) {
        if (mainLayoutController == null) {
            throw new IllegalStateException("MainLayoutController non initialisé");
        }
        mainLayoutController.setContent(load(fxml));
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
            return FXMLLoader.load(Router.class.getResource(fxml));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la vue : " + fxml, e);
        }
    }

    private static void applyCss(Scene scene) {
        String css = Router.class.getResource("/css/app.css").toExternalForm();
        scene.getStylesheets().setAll(css);
    }
}
