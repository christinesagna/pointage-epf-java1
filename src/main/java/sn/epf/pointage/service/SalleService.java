package sn.epf.pointage.service;

import sn.epf.pointage.dao.SalleDAO;
import sn.epf.pointage.model.Salle;

import java.util.List;

public class SalleService {

    private final SalleDAO salleDAO = new SalleDAO();

    public List<Salle> listerToutesSalles() {
        return salleDAO.findAll();
    }

    public Salle sauvegarderSalle(Salle salle) {
        return salleDAO.save(salle);
    }

    public Salle mettreAJourSalle(Salle salle) {
        return salleDAO.update(salle);
    }

    public void supprimerSalle(Salle salle) {
        salleDAO.delete(salle);
    }
}
