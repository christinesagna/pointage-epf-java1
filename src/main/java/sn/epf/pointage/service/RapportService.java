package sn.epf.pointage.service;

import sn.epf.pointage.dao.RapportMensuelDAO;
import sn.epf.pointage.dao.SeancePlanifieeDAO;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.RapportMensuel;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.enums.StatutRapport;
import sn.epf.pointage.model.enums.StatutSeance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class RapportService {

    private final SeancePlanifieeDAO seanceDAO = new SeancePlanifieeDAO();
    private final RapportMensuelDAO rapportDAO = new RapportMensuelDAO();

    public RapportMensuel genererRapportMensuel(Professeur professeur, int mois, int annee) {
        List<SeancePlanifiee> restantes = seanceDAO.findPlanifieesDuMois(professeur.getId(), mois, annee);
        if (!restantes.isEmpty()) {
            throw new IllegalStateException("Impossible de générer le rapport : des séances restent PLANIFIEES.");
        }

        List<SeancePlanifiee> seances = seanceDAO.findByProfesseurAndMois(professeur.getId(), mois, annee);

        int totalMinutes = seances.stream()
                .filter(s -> s.getStatut() == StatutSeance.REALISEE)
                .mapToInt(SeancePlanifiee::getDureeMinutes)
                .sum();

        int quartHeureSuperieur = ((totalMinutes + 14) / 15) * 15;

        BigDecimal heures = BigDecimal.valueOf(quartHeureSuperieur)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal montant = heures.multiply(professeur.getTauxHoraireXOF());

        RapportMensuel rapport = new RapportMensuel();
        rapport.setProfesseur(professeur);
        rapport.setMois(mois);
        rapport.setAnnee(annee);
        rapport.setHeuresRealisees(heures);
        rapport.setMontantXOF(montant);
        rapport.setStatut(StatutRapport.EN_ATTENTE);

        return rapportDAO.save(rapport);
    }

    public List<RapportMensuel> listerRapportsParProfesseur(Long professeurId) {
        return rapportDAO.findByProfesseur(professeurId);
    }

    public List<RapportMensuel> listerRapportsDuMois(int mois, int annee) {
        return rapportDAO.findByMoisAnnee(mois, annee);
    }

    public Optional<RapportMensuel> findRapport(Long professeurId, int mois, int annee) {
        return rapportDAO.findByProfesseurAndMoisAnnee(professeurId, mois, annee);
    }

    public RapportMensuel validerRapport(RapportMensuel rapport) {
        rapport.setStatut(StatutRapport.VALIDE);
        return rapportDAO.update(rapport);
    }
}
