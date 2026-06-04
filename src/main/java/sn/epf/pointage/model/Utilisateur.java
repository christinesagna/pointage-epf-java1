package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.Role;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "mot_de_passe_hash", nullable = false)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(name = "professeur_id")
    private Professeur professeurLie;

    public Utilisateur() {}

    public Utilisateur(String login, String motDePasseHash, Role role) {
        this.login = login;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Professeur getProfesseurLie() { return professeurLie; }
    public void setProfesseurLie(Professeur professeurLie) { this.professeurLie = professeurLie; }
}
