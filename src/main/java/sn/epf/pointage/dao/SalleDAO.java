package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Salle;

import java.util.List;
import java.util.Optional;

public class SalleDAO extends AbstractGenericDAO<Salle, Long> {

    public SalleDAO() {
        super(Salle.class);
    }

    public Optional<Salle> findByNom(String nom) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s from Salle s where s.nom = :nom
                    """, Salle.class)
                    .setParameter("nom", nom)
                    .uniqueResultOptional();
        }
    }

    public List<Salle> findByBatiment(String batiment) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s from Salle s where s.batiment = :batiment
                    order by s.nom
                    """, Salle.class)
                    .setParameter("batiment", batiment)
                    .list();
        }
    }

    public List<Salle> findByCapaciteMin(int capaciteMin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select s from Salle s where s.capacite >= :capaciteMin
                    order by s.capacite
                    """, Salle.class)
                    .setParameter("capaciteMin", capaciteMin)
                    .list();
        }
    }
}