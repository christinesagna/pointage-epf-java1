package sn.epf.pointage.service;

import sn.epf.pointage.dao.CoursDAO;
import sn.epf.pointage.model.Cours;

import java.util.List;

public class CoursService {

    private final CoursDAO coursDAO = new CoursDAO();

    public List<Cours> listerTousCours() {
        return coursDAO.findAll();
    }

    public Cours sauvegarderCours(Cours cours) {
        return coursDAO.save(cours);
    }

    public Cours mettreAJourCours(Cours cours) {
        return coursDAO.update(cours);
    }

    public void supprimerCours(Cours cours) {
        coursDAO.delete(cours);
    }
}
