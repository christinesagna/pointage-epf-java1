package sn.epf.pointage.config;

import sn.epf.pointage.dao.ProfesseurDAO;
import sn.epf.pointage.dao.UtilisateurDAO;
import sn.epf.pointage.model.Professeur;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;
import sn.epf.pointage.model.enums.TypeContrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DevSeeder {

    public static void seedIfNeeded() {
        ProfesseurDAO professeurDAO = new ProfesseurDAO();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        List<Professeur> professeurs = professeurDAO.findAll();
        if (professeurs.isEmpty()) {
            Professeur p1 = new Professeur();
            p1.setMatricule("EPF-2026-ND-001");
            p1.setNom("Ndiaye");
            p1.setPrenom("Aminata");
            p1.setEmail("aminata.ndiaye@epf.sn");
            p1.setTelephone("770000001");
            p1.setTypeContrat(TypeContrat.PERMANENT);
            p1.setTauxHoraireXOF(new BigDecimal("12000.00"));
            p1.setDateEmbauche(LocalDate.of(2024, 10, 1));
            p1.setActif(true);
            professeurDAO.save(p1);

            Professeur p2 = new Professeur();
            p2.setMatricule("EPF-2026-FD-002");
            p2.setNom("Fall");
            p2.setPrenom("Demba");
            p2.setEmail("demba.fall@epf.sn");
            p2.setTelephone("770000002");
            p2.setTypeContrat(TypeContrat.VACATAIRE);
            p2.setTauxHoraireXOF(new BigDecimal("15000.00"));
            p2.setDateEmbauche(LocalDate.of(2025, 1, 15));
            p2.setActif(true);
            professeurDAO.save(p2);

            professeurs = professeurDAO.findAll();
        }

        List<Utilisateur> utilisateurs = utilisateurDAO.findAll();
        if (utilisateurs.isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setLogin("admin");
            admin.setMotDePasseHash(SecurityUtils.hashPassword("admin123"));
            admin.setRole(Role.ADMIN);
            utilisateurDAO.save(admin);

            Utilisateur scolarite = new Utilisateur();
            scolarite.setLogin("scolarite");
            scolarite.setMotDePasseHash(SecurityUtils.hashPassword("scolarite123"));
            scolarite.setRole(Role.SCOLARITE);
            utilisateurDAO.save(scolarite);

            if (!professeurs.isEmpty()) {
                Utilisateur profUser = new Utilisateur();
                profUser.setLogin("aminata");
                profUser.setMotDePasseHash(SecurityUtils.hashPassword("prof123"));
                profUser.setRole(Role.PROFESSEUR);
                profUser.setProfesseurLie(professeurs.get(0));
                utilisateurDAO.save(profUser);
            }

            System.out.println("=== SEED OK : utilisateurs et professeurs insérés ===");
        } else {
            System.out.println("=== SEED SKIP : des utilisateurs existent déjà ===");
        }
    }
}
