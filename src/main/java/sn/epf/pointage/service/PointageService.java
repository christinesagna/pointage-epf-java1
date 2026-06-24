package sn.epf.pointage.service;

import sn.epf.pointage.config.SecurityUtils;
import sn.epf.pointage.dao.PointageDAO;
import sn.epf.pointage.dao.SeancePlanifieeDAO;
import sn.epf.pointage.model.Pointage;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.model.enums.StatutPointage;
import sn.epf.pointage.model.enums.StatutSeance;
import sn.epf.pointage.model.enums.TypePointage;

import java.time.LocalDateTime;

public class PointageService {

    private final PointageDAO pointageDAO = new PointageDAO();
    private final SeancePlanifieeDAO seanceDAO = new SeancePlanifieeDAO();

    public Pointage pointerDebut(Professeur professeur, SeancePlanifiee seance, LocalDateTime heure) {
        // Vérification de sécurité : seul un PROFESSEUR peut pointer
        SecurityUtils.checkIsProfesseur();
        // Vérification d'identité : le professeur doit correspondre à l'utilisateur connecté
        try {
            sn.epf.pointage.model.Utilisateur current = sn.epf.pointage.config.SessionContext.getInstance().currentUser();
            if (current != null && current.getProfesseurLie() != null && !current.getProfesseurLie().getId().equals(professeur.getId())) {
                throw new SecurityException("Accès refusé: vous ne pouvez pointer que pour votre propre compte.");
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception ignored) {}
        
        Pointage p = new Pointage();
        p.setProfesseur(professeur);
        p.setSeance(seance);
        p.setHeurePointage(heure);
        p.setTypePointage(TypePointage.DEBUT);

        if (!professeur.isActif()) {
            p.setStatut(StatutPointage.PROF_INACTIF);
            p.setObservations("Professeur inactif");
            return pointageDAO.save(p);
        }

        LocalDateTime debut = seance.getDateHeure();
        LocalDateTime min = debut.minusMinutes(15);
        LocalDateTime max = debut.plusMinutes(5);

        if (heure.isBefore(min)) {
            p.setStatut(StatutPointage.TROP_TOT);
        } else if (heure.isAfter(max)) {
            p.setStatut(StatutPointage.EN_RETARD);
            p.setObservations("Alerte scolarité : retard");
            seance.setStatut(StatutSeance.REALISEE);
            seanceDAO.update(seance);
        } else {
            p.setStatut(StatutPointage.SUCCES);
            seance.setStatut(StatutSeance.REALISEE);
            seanceDAO.update(seance);
        }

        return pointageDAO.save(p);
    }

    public Pointage pointerFin(Professeur professeur, SeancePlanifiee seance, LocalDateTime heure) {
        // Vérification de sécurité : seul un PROFESSEUR peut pointer
        SecurityUtils.checkIsProfesseur();
        
        Pointage p = new Pointage();
        p.setProfesseur(professeur);
        p.setSeance(seance);
        p.setHeurePointage(heure);
        p.setTypePointage(TypePointage.FIN);
        p.setStatut(StatutPointage.SUCCES);
        return pointageDAO.save(p);
    }
}
