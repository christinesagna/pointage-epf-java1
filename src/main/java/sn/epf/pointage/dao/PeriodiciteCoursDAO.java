package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.PeriodiciteCours;
import sn.epf.pointage.model.enums.JourSemaineCours;

import java.util.List;

public class PeriodiciteCoursDAO extends AbstractGenericDAO<PeriodiciteCours, Long> {

    public PeriodiciteCoursDAO() {
        super(PeriodiciteCours.class);
    }

    public List<PeriodiciteCours> findByAssignation(Long assignationId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from PeriodiciteCours p
                    where p.assignation.id = :assignationId
                    order by p.jourSemaine, p.heureDebut
                    """, PeriodiciteCours.class)
                    .setParameter("assignationId", assignationId)
                    .list();
        }
    }

    public List<PeriodiciteCours> findByJour(JourSemaineCours jour) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from PeriodiciteCours p
                    where p.jourSemaine = :jour
                    order by p.heureDebut
                    """, PeriodiciteCours.class)
                    .setParameter("jour", jour)
                    .list();
        }
    }

    public List<PeriodiciteCours> findByProfesseur(Long professeurId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from PeriodiciteCours p
                    where p.assignation.professeur.id = :professeurId
                    order by p.jourSemaine, p.heureDebut
                    """, PeriodiciteCours.class)
                    .setParameter("professeurId", professeurId)
                    .list();
        }
    }
}