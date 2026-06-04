package sn.epf.pointage.config;

import sn.epf.pointage.model.Utilisateur;
import sn.epf.pointage.model.enums.Role;

import java.time.LocalDateTime;

public class SessionContext {

    private static final SessionContext INSTANCE = new SessionContext();
    private Utilisateur currentUser;
    private LocalDateTime lastActivity;

    private SessionContext() {}

    public static SessionContext getInstance() {
        return INSTANCE;
    }

    public void login(Utilisateur utilisateur) {
        this.currentUser = utilisateur;
        touch();
    }

    public void logout() {
        this.currentUser = null;
        this.lastActivity = null;
    }

    public Utilisateur currentUser() {
        return currentUser;
    }

    public boolean hasRole(Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    public void touch() {
        this.lastActivity = LocalDateTime.now();
    }

    public boolean isExpired() {
        return lastActivity != null && lastActivity.plusMinutes(30).isBefore(LocalDateTime.now());
    }
}
