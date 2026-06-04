package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.AuditConnexion;

import java.time.LocalDateTime;
import java.util.List;

public class AuditConnexionDAO extends AbstractGenericDAO<AuditConnexion, Long> {

    public AuditConnexionDAO() {
        super(AuditConnexion.class);
    }

    public List<AuditConnexion> findByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from AuditConnexion a
                    where a.login = :login
                    order by a.horodatage desc
                    """, AuditConnexion.class)
                    .setParameter("login", login)
                    .list();
        }
    }

    public List<AuditConnexion> findByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from AuditConnexion a
                    where a.horodatage between :debut and :fin
                    order by a.horodatage desc
                    """, AuditConnexion.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    public List<AuditConnexion> findByAction(String action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from AuditConnexion a
                    where a.action = :action
                    order by a.horodatage desc
                    """, AuditConnexion.class)
                    .setParameter("action", action)
                    .list();
        }
    }

    public AuditConnexion logConnexion(String login, String adresseIp) {
        AuditConnexion audit = new AuditConnexion(login, LocalDateTime.now(), adresseIp, "CONNEXION");
        return save(audit);
    }

    public AuditConnexion logDeconnexion(String login, String adresseIp) {
        AuditConnexion audit = new AuditConnexion(login, LocalDateTime.now(), adresseIp, "DECONNEXION");
        return save(audit);
    }
}