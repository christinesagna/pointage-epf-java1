package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Utilisateur;

import java.util.Optional;

public class UtilisateurDAO extends AbstractGenericDAO<Utilisateur, Long> {
    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Optional<Utilisateur> findByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Utilisateur u where u.login = :login", Utilisateur.class)
                    .setParameter("login", login)
                    .uniqueResultOptional();
        }
    }
}
