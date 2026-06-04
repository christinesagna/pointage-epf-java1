package sn.epf.pointage.dao;

import org.hibernate.Session;
import sn.epf.pointage.config.HibernateUtil;
import sn.epf.pointage.model.Professeur;

import java.util.List;
import java.util.Optional;

public class ProfesseurDAO extends AbstractGenericDAO<Professeur, Long> {
    public ProfesseurDAO() {
        super(Professeur.class);
    }

    public Optional<Professeur> findByMatricule(String matricule) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Professeur p where p.matricule = :matricule", Professeur.class)
                    .setParameter("matricule", matricule)
                    .uniqueResultOptional();
        }
    }

    public List<Professeur> findActifs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Professeur p where p.actif = true order by p.nom, p.prenom", Professeur.class)
                    .list();
        }
    }
}
