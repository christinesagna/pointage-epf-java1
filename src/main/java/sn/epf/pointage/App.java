package sn.epf.pointage;

import javafx.application.Application;
import javafx.stage.Stage;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.ui.Router;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Router.init(stage);
        Router.showLogin();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        launch(args);
    }
}
