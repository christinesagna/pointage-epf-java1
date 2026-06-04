package sn.epf.pointage.service;

import sn.epf.pointage.dao.SeancePlanifieeDAO;
import sn.epf.pointage.model.Assignation;
import sn.epf.pointage.model.PeriodiciteCours;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.enums.StatutSeance;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningService {

    private final SeancePlanifieeDAO seanceDAO = new SeancePlanifieeDAO();

    public List<SeancePlanifiee> genererSeancesSemestre(Assignation assignation,
                                                        PeriodiciteCours periodicite,
                                                        LocalDate dateDebut,
                                                        LocalDate dateFin,
                                                        int dureeMinutes) {
        List<SeancePlanifiee> resultat = new ArrayList<>();
        LocalDate current = dateDebut;

        while (!current.isAfter(dateFin)) {
            if (correspondJour(current, periodicite)) {
                SeancePlanifiee s = new SeancePlanifiee();
                s.setAssignation(assignation);
                s.setDateHeure(LocalDateTime.of(current, periodicite.getHeureDebut()));
                s.setDureeMinutes(dureeMinutes);
                s.setStatut(StatutSeance.PLANIFIEE);
                resultat.add(seanceDAO.save(s));
            }
            current = current.plusDays(1);
        }
        return resultat;
    }

    private boolean correspondJour(LocalDate date, PeriodiciteCours periodicite) {
        return switch (periodicite.getJourSemaine()) {
            case LUNDI -> date.getDayOfWeek() == DayOfWeek.MONDAY;
            case MARDI -> date.getDayOfWeek() == DayOfWeek.TUESDAY;
            case MERCREDI -> date.getDayOfWeek() == DayOfWeek.WEDNESDAY;
            case JEUDI -> date.getDayOfWeek() == DayOfWeek.THURSDAY;
            case VENDREDI -> date.getDayOfWeek() == DayOfWeek.FRIDAY;
            case SAMEDI -> date.getDayOfWeek() == DayOfWeek.SATURDAY;
            case DIMANCHE -> date.getDayOfWeek() == DayOfWeek.SUNDAY;
        };
    }
}
