package sn.epf.pointage.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_connexions")
public class AuditConnexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private LocalDateTime horodatage;
    private String adresseIp;
    private String action; // CONNEXION / DECONNEXION

    public AuditConnexion() {}

    public AuditConnexion(String login, LocalDateTime horodatage, String adresseIp, String action) {
        this.login = login;
        this.horodatage = horodatage;
        this.adresseIp = adresseIp;
        this.action = action;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public LocalDateTime getHorodatage() { return horodatage; }
    public void setHorodatage(LocalDateTime horodatage) { this.horodatage = horodatage; }
    public String getAdresseIp() { return adresseIp; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
