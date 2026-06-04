package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Cours;

import java.util.List;
import java.util.Optional;

public class CoursDAO extends AbstractGenericDAO<Cours, Long> {

    public CoursDAO() {
        super(Cours.class);
    }

    public Optional<Cours> findByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select c from Cours c where c.code = :code
                    """, Cours.class)
                    .setParameter("code", code)
                    .uniqueResultOptional();
        }
    }

    public List<Cours> findByFiliere(String filiere) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select c from Cours c where c.filiere = :filiere
                    order by c.intitule
                    """, Cours.class)
                    .setParameter("filiere", filiere)
                    .list();
        }
    }

    public List<Cours> findByNiveauEtude(String niveauEtude) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select c from Cours c where c.niveauEtude = :niveauEtude
                    order by c.intitule
                    """, Cours.class)
                    .setParameter("niveauEtude", niveauEtude)
                    .list();
        }
    }
}