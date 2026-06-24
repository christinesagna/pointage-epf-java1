package sn.epf.pointage.service;

import sn.epf.pointage.dao.AuditConnexionDAO;
import sn.epf.pointage.model.AuditConnexion;

import java.time.LocalDateTime;
import java.util.List;

public class AuditConnexionService {

    private final AuditConnexionDAO auditConnexionDAO = new AuditConnexionDAO();

    public List<AuditConnexion> listerTousAudits() {
        return auditConnexionDAO.findAll();
    }

    public List<AuditConnexion> chercherParLogin(String login) {
        return auditConnexionDAO.findByLogin(login);
    }

    public List<AuditConnexion> chercherParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return auditConnexionDAO.findByPeriode(debut, fin);
    }

    public List<AuditConnexion> chercherParAction(String action) {
        return auditConnexionDAO.findByAction(action);
    }
}
