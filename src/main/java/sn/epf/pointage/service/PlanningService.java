package sn.epf.pointage.service;

import sn.epf.pointage.dao.*;
import sn.epf.pointage.model.*;
import sn.epf.pointage.model.enums.StatutSeance;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class PlanningService {

    private final SeancePlanifieeDAO seanceDAO = new SeancePlanifieeDAO();
    private final AssignationDAO assignationDAO = new AssignationDAO();
    private final CoursDAO coursDAO = new CoursDAO();
    private final SalleDAO salleDAO = new SalleDAO();

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

    public List<SeancePlanifiee> listerSeancesDuJour(Long professeurId) {
        LocalDate today = LocalDate.now();
        return seanceDAO.findSeancesDuJour(professeurId, today.atStartOfDay(), today.atTime(LocalTime.MAX));
    }

    public List<SeancePlanifiee> listerSeancesDuMois(Long professeurId, int mois, int annee) {
        return seanceDAO.findByProfesseurAndMois(professeurId, mois, annee);
    }

    public List<Cours> listerTousCours() {
        return coursDAO.findAll();
    }

    public List<Salle> listerToutesSalles() {
        return salleDAO.findAll();
    }

    public void creerAssignationEtSeance(Professeur professeur,
                                         Cours cours,
                                         Salle salle,
                                         String anneeAcademique,
                                         BigDecimal heuresPrevues,
                                         LocalDateTime dateHeure,
                                         int dureeMinutes) {
        Assignation assignation = new Assignation();
        assignation.setProfesseur(professeur);
        assignation.setCours(cours);
        assignation.setSalle(salle);
        assignation.setAnneeAcademique(anneeAcademique);
        assignation.setHeuresPrevues(heuresPrevues);
        assignationDAO.save(assignation);

        SeancePlanifiee seance = new SeancePlanifiee();
        seance.setAssignation(assignation);
        seance.setDateHeure(dateHeure);
        seance.setDureeMinutes(dureeMinutes);
        seance.setStatut(StatutSeance.PLANIFIEE);
        seanceDAO.save(seance);
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