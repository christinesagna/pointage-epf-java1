package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Pointage;
import sn.epf.pointage.model.enums.TypePointage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PointageDAO extends AbstractGenericDAO<Pointage, Long> {

    public PointageDAO() {
        super(Pointage.class);
    }

    public Optional<Pointage> findBySeanceAndType(Long seanceId, TypePointage type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from Pointage p
                    where p.seance.id = :seanceId
                      and p.typePointage = :type
                    """, Pointage.class)
                    .setParameter("seanceId", seanceId)
                    .setParameter("type", type)
                    .uniqueResultOptional();
        }
    }

    public List<Pointage> findByProfesseurAndPeriode(Long professeurId,
                                                     LocalDateTime debut,
                                                     LocalDateTime fin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from Pointage p
                    where p.professeur.id = :professeurId
                      and p.heurePointage between :debut and :fin
                    order by p.heurePointage
                    """, Pointage.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    public List<Pointage> findBySeance(Long seanceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from Pointage p
                    where p.seance.id = :seanceId
                    order by p.heurePointage
                    """, Pointage.class)
                    .setParameter("seanceId", seanceId)
                    .list();
        }
    }

    public List<Pointage> findByProfesseurAndMois(Long professeurId, int mois, int annee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select p from Pointage p
                    where p.professeur.id = :professeurId
                      and month(p.heurePointage) = :mois
                      and year(p.heurePointage) = :annee
                    order by p.heurePointage
                    """, Pointage.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("mois", mois)
                    .setParameter("annee", annee)
                    .list();
        }
    }
}