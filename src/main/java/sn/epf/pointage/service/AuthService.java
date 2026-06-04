package sn.epf.pointage.service;

import sn.epf.pointage.config.SecurityUtils;
import sn.epf.pointage.config.SessionContext;
import sn.epf.pointage.dao.AuditConnexionDAO;
import sn.epf.pointage.dao.UtilisateurDAO;
import sn.epf.pointage.model.AuditConnexion;
import sn.epf.pointage.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.Optional;

public class AuthService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final AuditConnexionDAO auditConnexionDAO = new AuditConnexionDAO();

    public boolean login(String login, String motDePasse, String ip) {
        Optional<Utilisateur> optionalUser = utilisateurDAO.findByLogin(login);
        if (optionalUser.isEmpty()) return false;

        Utilisateur user = optionalUser.get();
        if (!SecurityUtils.checkPassword(motDePasse, user.getMotDePasseHash())) return false;

        SessionContext.getInstance().login(user);
        auditConnexionDAO.save(new AuditConnexion(login, LocalDateTime.now(), ip, "CONNEXION"));
        return true;
    }

    public void logout(String ip) {
        Utilisateur current = SessionContext.getInstance().currentUser();
        if (current != null) {
            auditConnexionDAO.save(new AuditConnexion(current.getLogin(), LocalDateTime.now(), ip, "DECONNEXION"));
        }
        SessionContext.getInstance().logout();
    }

    public Utilisateur currentUser() {
        return SessionContext.getInstance().currentUser();
    }

    public boolean hasRole(sn.epf.pointage.model.enums.Role role) {
        return SessionContext.getInstance().hasRole(role);
    }
}
