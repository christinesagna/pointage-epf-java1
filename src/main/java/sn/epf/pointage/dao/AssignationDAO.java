package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Assignation;

import java.util.List;

public class AssignationDAO extends AbstractGenericDAO<Assignation, Long> {

    public AssignationDAO() {
        super(Assignation.class);
    }

    public List<Assignation> findByProfesseur(Long professeurId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from Assignation a
                    where a.professeur.id = :professeurId
                    order by a.anneeAcademique desc
                    """, Assignation.class)
                    .setParameter("professeurId", professeurId)
                    .list();
        }
    }

    public List<Assignation> findByAnneeAcademique(String anneeAcademique) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from Assignation a
                    where a.anneeAcademique = :anneeAcademique
                    order by a.professeur.nom
                    """, Assignation.class)
                    .setParameter("anneeAcademique", anneeAcademique)
                    .list();
        }
    }

    public List<Assignation> findByProfesseurAndAnnee(Long professeurId, String anneeAcademique) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from Assignation a
                    where a.professeur.id = :professeurId
                      and a.anneeAcademique = :anneeAcademique
                    """, Assignation.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("anneeAcademique", anneeAcademique)
                    .list();
        }
    }

    public List<Assignation> findByCours(Long coursId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select a from Assignation a
                    where a.cours.id = :coursId
                    """, Assignation.class)
                    .setParameter("coursId", coursId)
                    .list();
        }
    }
}