package sn.epf.pointage.service;

import sn.epf.pointage.config.SecurityUtils;
import sn.epf.pointage.dao.ProfesseurDAO;
import sn.epf.pointage.dao.UtilisateurDAO;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.util.MatriculeGenerator;

import java.util.List;

public class ProfesseurService {

    private final ProfesseurDAO professeurDAO = new ProfesseurDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public Professeur enrôlerProfesseur(Professeur professeur, String login, String motDePasse) {
        professeur.setMatricule(MatriculeGenerator.generate(
                professeur.getNom(), professeur.getPrenom()));

        Professeur saved = professeurDAO.save(professeur);

        Utilisateur user = new Utilisateur();
        user.setLogin(login);
        user.setMotDePasseHash(SecurityUtils.hashPassword(motDePasse));
        user.setRole(Role.PROFESSEUR);
        user.setProfesseurLie(saved);
        utilisateurDAO.save(user);

        return saved;
    }

    public List<Professeur> listerTousProfesseurs() {
        return professeurDAO.findAll();
    }

    public List<Professeur> listerProfesseursActifs() {
        return professeurDAO.findActifs();
    }

    public void desactiverProfesseur(Professeur professeur) {
        professeur.setActif(false);
        professeurDAO.update(professeur);
    }

    public void activerProfesseur(Professeur professeur) {
        professeur.setActif(true);
        professeurDAO.update(professeur);
    }
}
