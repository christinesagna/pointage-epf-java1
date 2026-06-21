package sn.epf.pointage.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sn.epf.pointage.service.DashboardService;

import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML private Label professeursActifsLabel;
    @FXML private Label seancesJourLabel;
    @FXML private Label seancesMoisLabel;
    @FXML private Label retardsLabel;
    @FXML private PieChart statutChart;
    @FXML private TableView<DashboardService.UpcomingSeance> upcomingTable;
    @FXML private TableColumn<DashboardService.UpcomingSeance, String> professeurColumn;
    @FXML private TableColumn<DashboardService.UpcomingSeance, String> coursColumn;
    @FXML private TableColumn<DashboardService.UpcomingSeance, String> dateColumn;
    @FXML private TableColumn<DashboardService.UpcomingSeance, String> salleColumn;
    @FXML private TableColumn<DashboardService.UpcomingSeance, String> statutColumn;

    private final DashboardService dashboardService = new DashboardService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        professeurColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().professeur()));
        coursColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().cours()));
        salleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().salle()));
        statutColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().statut()));
        dateColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().dateHeure().format(formatter)
        ));
        refresh();
    }

    @FXML
    public void refresh() {
        DashboardService.DashboardSnapshot snapshot = dashboardService.buildSnapshot();
        professeursActifsLabel.setText(String.valueOf(snapshot.professeursActifs()));
        seancesJourLabel.setText(String.valueOf(snapshot.seancesAujourdHui()));
        seancesMoisLabel.setText(String.valueOf(snapshot.seancesRealiseesDuMois()));
        retardsLabel.setText(String.valueOf(snapshot.retardsDuMois()));

        statutChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Planifiées", snapshot.totalPlanifiees()),
                new PieChart.Data("Réalisées", snapshot.totalRealisees()),
                new PieChart.Data("Annulées", snapshot.totalAnnulees()),
                new PieChart.Data("Reportées", snapshot.totalReportees())
        ));
        upcomingTable.setItems(FXCollections.observableArrayList(snapshot.prochainesSeances()));
    }
}
