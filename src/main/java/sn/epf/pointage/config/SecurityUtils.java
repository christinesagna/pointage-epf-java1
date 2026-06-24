package sn.epf.pointage.config;

import org.mindrot.jbcrypt.BCrypt;
import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;

public class SecurityUtils {

    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    /**
     * Vérifie que l'utilisateur actuel a le rôle spécifié
     * Lève une exception si le rôle ne correspond pas
     */
    public static void checkRole(Role requiredRole) {
        Utilisateur user = SessionContext.getInstance().currentUser();
        if (user == null || user.getRole() != requiredRole) {
            throw new SecurityException("Accès refusé : rôle " + requiredRole.name() + " requis.");
        }
    }

    /**
     * Vérifie que l'utilisateur actuel a l'un des rôles spécifiés
     */
    public static void checkAnyRole(Role... allowedRoles) {
        Utilisateur user = SessionContext.getInstance().currentUser();
        if (user == null) {
            throw new SecurityException("Utilisateur non authentifié.");
        }
        for (Role role : allowedRoles) {
            if (user.getRole() == role) {
                return;
            }
        }
        throw new SecurityException("Accès refusé : rôle non autorisé.");
    }

    /**
     * Vérifie que l'utilisateur actuel n'est pas un PROFESSEUR
     * (i.e., c'est un ADMIN ou SCOLARITE)
     */
    public static void checkNotProfesseur() {
        Utilisateur user = SessionContext.getInstance().currentUser();
        if (user == null || user.getRole() == Role.PROFESSEUR) {
            throw new SecurityException("Accès refusé : cette opération n'est pas autorisée pour les professeurs.");
        }
    }

    /**
     * Vérifie que l'utilisateur actuel est un ADMIN
     */
    public static void checkIsAdmin() {
        checkRole(Role.ADMIN);
    }

    /**
     * Vérifie que l'utilisateur actuel est un SCOLARITE
     */
    public static void checkIsScolarite() {
        checkRole(Role.SCOLARITE);
    }

    /**
     * Vérifie que l'utilisateur actuel est un PROFESSEUR
     */
    public static void checkIsProfesseur() {
        checkRole(Role.PROFESSEUR);
    }
}
