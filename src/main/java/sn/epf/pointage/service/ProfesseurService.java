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
        // Vérification de sécurité : seul un ADMIN ou SCOLARITE peut enrôler un professeur
        SecurityUtils.checkAnyRole(Role.ADMIN, Role.SCOLARITE);
        
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
        // Vérification de sécurité : seul un ADMIN ou SCOLARITE peut désactiver
        SecurityUtils.checkAnyRole(Role.ADMIN, Role.SCOLARITE);
        
        professeur.setActif(false);
        professeurDAO.update(professeur);
    }

    public void activerProfesseur(Professeur professeur) {
        // Vérification de sécurité : seul un ADMIN ou SCOLARITE peut activer
        SecurityUtils.checkAnyRole(Role.ADMIN, Role.SCOLARITE);
        
        professeur.setActif(true);
        professeurDAO.update(professeur);
    }
}
