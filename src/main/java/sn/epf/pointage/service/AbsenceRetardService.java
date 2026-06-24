package sn.epf.pointage.service;

import sn.epf.pointage.dao.PointageDAO;
import sn.epf.pointage.model.Pointage;
import sn.epf.pointage.model.enums.StatutPointage;
import sn.epf.pointage.model.enums.TypePointage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AbsenceRetardService {

    private final PointageDAO pointageDAO = new PointageDAO();

    /**
     * Récupère tous les retards enregistrés pour une période donnée
     */
    public List<Pointage> listerRetardsPeriode(LocalDate debut, LocalDate fin) {
        List<Pointage> pointages = pointageDAO.findAll();
        return pointages.stream()
                .filter(p -> p.getTypePointage() == TypePointage.DEBUT)
                .filter(p -> p.getStatut() == StatutPointage.EN_RETARD)
                .filter(p -> {
                    LocalDate date = p.getHeurePointage().toLocalDate();
                    return !date.isBefore(debut) && !date.isAfter(fin);
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère les retards d'un professeur spécifique
     */
    public List<Pointage> listerRetardsProfesseur(Long professeurId, LocalDate debut, LocalDate fin) {
        // Si l'utilisateur courant est PROFESSEUR, restreindre à son propre id
        try {
            sn.epf.pointage.model.Utilisateur current = sn.epf.pointage.config.SessionContext.getInstance().currentUser();
            if (current != null && current.getRole() == sn.epf.pointage.model.enums.Role.PROFESSEUR) {
                if (current.getProfesseurLie() == null || !current.getProfesseurLie().getId().equals(professeurId)) {
                    throw new SecurityException("Accès refusé: consultation non autorisée.");
                }
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception ignored) {}

        return listerRetardsPeriode(debut, fin).stream()
                .filter(p -> p.getProfesseur().getId().equals(professeurId))
                .collect(Collectors.toList());
    }

    /**
     * Compte les retards par professeur dans une période
     */
    public long compterRetardsProfesseur(Long professeurId, LocalDate debut, LocalDate fin) {
        return listerRetardsProfesseur(professeurId, debut, fin).size();
    }

    /**
     * Récupère les professeurs inactifs à la connexion
     */
    public List<Pointage> listerInactiviteProfesseurs(LocalDate debut, LocalDate fin) {
        List<Pointage> pointages = pointageDAO.findAll();
        return pointages.stream()
                .filter(p -> p.getStatut() == StatutPointage.PROF_INACTIF)
                .filter(p -> {
                    LocalDate date = p.getHeurePointage().toLocalDate();
                    return !date.isBefore(debut) && !date.isAfter(fin);
                })
                .collect(Collectors.toList());
    }

    /**
     * Synthèse : retards + absences (pas de pointage du tout) pour une période
     */
    public class SyntheseAbsenceRetard {
        public Pointage pointage;
        public String typeSynthese; // "RETARD" ou "INACTIVITE"

        public SyntheseAbsenceRetard(Pointage pointage, String typeSynthese) {
            this.pointage = pointage;
            this.typeSynthese = typeSynthese;
        }
    }

    public List<SyntheseAbsenceRetard> synthesesAbsenceRetard(LocalDate debut, LocalDate fin) {
        List<SyntheseAbsenceRetard> syntheses = new java.util.ArrayList<>();

        // Ajouter les retards
        listerRetardsPeriode(debut, fin).forEach(p ->
                syntheses.add(new SyntheseAbsenceRetard(p, "RETARD"))
        );

        // Ajouter les inactivités (professeur non actif)
        listerInactiviteProfesseurs(debut, fin).forEach(p ->
                syntheses.add(new SyntheseAbsenceRetard(p, "INACTIVITE"))
        );

        return syntheses;
    }
}
