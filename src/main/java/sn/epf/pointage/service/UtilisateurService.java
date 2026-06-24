package sn.epf.pointage.service;

import sn.epf.pointage.config.SecurityUtils;
import sn.epf.pointage.dao.UtilisateurDAO;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;

import java.util.List;
import java.util.Optional;

public class UtilisateurService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<Utilisateur> listerTousUtilisateurs() {
        return utilisateurDAO.findAll();
    }

    public Optional<Utilisateur> trouverParId(Long id) {
        return utilisateurDAO.findById(id);
    }

    public Optional<Utilisateur> trouverParLogin(String login) {
        return utilisateurDAO.findByLogin(login);
    }

    public Utilisateur creerUtilisateur(String login, String motDePasse, Role role) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(login);
        utilisateur.setMotDePasseHash(SecurityUtils.hashPassword(motDePasse));
        utilisateur.setRole(role);
        return utilisateurDAO.save(utilisateur);
    }

    public Utilisateur sauvegarderUtilisateur(Utilisateur utilisateur, String motDePasse) {
        if (motDePasse != null && !motDePasse.isBlank()) {
            utilisateur.setMotDePasseHash(SecurityUtils.hashPassword(motDePasse));
        }
        return utilisateurDAO.update(utilisateur);
    }

    public void supprimerUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.delete(utilisateur);
    }
}
