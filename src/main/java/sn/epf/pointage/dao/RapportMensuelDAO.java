package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.RapportMensuel;
import sn.epf.pointage.model.enums.StatutRapport;

import java.util.List;
import java.util.Optional;

public class RapportMensuelDAO extends AbstractGenericDAO<RapportMensuel, Long> {

    public RapportMensuelDAO() {
        super(RapportMensuel.class);
    }

    public Optional<RapportMensuel> findByProfesseurAndMoisAnnee(Long professeurId,
                                                                 int mois,
                                                                 int annee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select r from RapportMensuel r
                    where r.professeur.id = :professeurId
                      and r.mois = :mois
                      and r.annee = :annee
                    """, RapportMensuel.class)
                    .setParameter("professeurId", professeurId)
                    .setParameter("mois", mois)
                    .setParameter("annee", annee)
                    .uniqueResultOptional();
        }
    }

    public List<RapportMensuel> findByProfesseur(Long professeurId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select r from RapportMensuel r
                    where r.professeur.id = :professeurId
                    order by r.annee desc, r.mois desc
                    """, RapportMensuel.class)
                    .setParameter("professeurId", professeurId)
                    .list();
        }
    }

    public List<RapportMensuel> findByStatut(StatutRapport statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select r from RapportMensuel r
                    where r.statut = :statut
                    order by r.annee desc, r.mois desc
                    """, RapportMensuel.class)
                    .setParameter("statut", statut)
                    .list();
        }
    }

    public List<RapportMensuel> findByMoisAnnee(int mois, int annee) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("""
                    select r from RapportMensuel r
                    where r.mois = :mois and r.annee = :annee
                    order by r.professeur.nom, r.professeur.prenom
                    """, RapportMensuel.class)
                    .setParameter("mois", mois)
                    .setParameter("annee", annee)
                    .list();
        }
    }
}