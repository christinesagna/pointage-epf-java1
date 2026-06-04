package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.SeancePlanifiee;
import sn.epf.pointage.model.enums.StatutSeance;

import java.time.LocalDateTime;
import java.util.List;

public class SeancePlanifieeDAO extends AbstractGenericDAO<SeancePlanifiee, Long> {
    public SeancePlanifieeDAO() {
        super(SeancePlanifiee.class);
    }

    public List<SeancePlanifiee> findByProfesseurAndMois(Long professeurId, int mois, int annee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s
                    from SeancePlanifiee s
                    where s.assignation.professeur.id = :professeurId
                      and month(s.dateHeure) = :mois
                      and year(s.dateHeure) = :annee
                    """, SeancePlanifiee.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("mois", mois)
                    .setParameter("annee", annee)
                    .list();
        }
    }

    public List<SeancePlanifiee> findPlanifieesDuMois(Long professeurId, int mois, int annee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s
                    from SeancePlanifiee s
                    where s.assignation.professeur.id = :professeurId
                      and month(s.dateHeure) = :mois
                      and year(s.dateHeure) = :annee
                      and s.statut = :statut
                    """, SeancePlanifiee.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("mois", mois)
                    .setParameter("annee", annee)
                    .setParameter("statut", StatutSeance.PLANIFIEE)
                    .list();
        }
    }

    public List<SeancePlanifiee> findSeancesDuJour(Long professeurId, LocalDateTime debut, LocalDateTime fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s
                    from SeancePlanifiee s
                    where s.assignation.professeur.id = :professeurId
                      and s.dateHeure between :debut and :fin
                    order by s.dateHeure
                    """, SeancePlanifiee.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }
}
