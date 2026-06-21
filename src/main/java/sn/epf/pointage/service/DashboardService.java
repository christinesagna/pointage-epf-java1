package sn.epf.pointage.service;

import sn.epf.pointage.dao.PointageDAO;
import sn.epf.pointage.dao.ProfesseurDAO;
import sn.epf.pointage.dao.SeancePlanifieeDAO;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.enums.StatutPointage;
import sn.epf.pointage.model.enums.StatutSeance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

public class DashboardService {

    private final ProfesseurDAO professeurDAO = new ProfesseurDAO();
    private final SeancePlanifieeDAO seanceDAO = new SeancePlanifieeDAO();
    private final PointageDAO pointageDAO = new PointageDAO();

    public DashboardSnapshot buildSnapshot() {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        List<SeancePlanifiee> seances = seanceDAO.findAll();

        long seancesAujourdHui = seances.stream()
                .filter(s -> s.getDateHeure() != null && s.getDateHeure().toLocalDate().equals(today))
                .count();

        long seancesRealiseesDuMois = seances.stream()
                .filter(s -> s.getDateHeure() != null)
                .filter(s -> YearMonth.from(s.getDateHeure()).equals(currentMonth))
                .filter(s -> s.getStatut() == StatutSeance.REALISEE)
                .count();

        long retardsDuMois = pointageDAO.findAll().stream()
                .filter(p -> p.getHeurePointage() != null)
                .filter(p -> YearMonth.from(p.getHeurePointage()).equals(currentMonth))
                .filter(p -> p.getStatut() == StatutPointage.EN_RETARD)
                .count();

        List<UpcomingSeance> prochainesSeances = seances.stream()
                .filter(s -> s.getDateHeure() != null)
                .filter(s -> !s.getDateHeure().isBefore(LocalDateTime.now().minusHours(2)))
                .sorted(Comparator.comparing(SeancePlanifiee::getDateHeure))
                .limit(8)
                .map(s -> new UpcomingSeance(
                        s.getAssignation().getProfesseur().getNomComplet(),
                        s.getAssignation().getCours().getIntitule(),
                        s.getDateHeure(),
                        s.getAssignation().getSalle().getNom(),
                        s.getStatut().name()))
                .toList();

        long planifiees = seances.stream().filter(s -> s.getStatut() == StatutSeance.PLANIFIEE).count();
        long realisees = seances.stream().filter(s -> s.getStatut() == StatutSeance.REALISEE).count();
        long annulees = seances.stream().filter(s -> s.getStatut() == StatutSeance.ANNULEE).count();
        long reportees = seances.stream().filter(s -> s.getStatut() == StatutSeance.REPORTEE).count();

        return new DashboardSnapshot(
                professeurDAO.findActifs().size(),
                seancesAujourdHui,
                seancesRealiseesDuMois,
                retardsDuMois,
                planifiees,
                realisees,
                annulees,
                reportees,
                prochainesSeances
        );
    }

    public record UpcomingSeance(String professeur, String cours, LocalDateTime dateHeure, String salle, String statut) {}

    public record DashboardSnapshot(
            int professeursActifs,
            long seancesAujourdHui,
            long seancesRealiseesDuMois,
            long retardsDuMois,
            long totalPlanifiees,
            long totalRealisees,
            long totalAnnulees,
            long totalReportees,
            List<UpcomingSeance> prochainesSeances
    ) {}
}
