package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.TypeContrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professeurs")
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true)
    private String email;

    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeContrat typeContrat;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal tauxHoraireXOF;

    private LocalDate dateEmbauche;
    private String photo;
    private boolean actif = true;

    @OneToMany(mappedBy = "professeur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignation> assignations = new ArrayList<>();

    public Professeur() {}

    public Long getId() { return id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }
    public BigDecimal getTauxHoraireXOF() { return tauxHoraireXOF; }
    public void setTauxHoraireXOF(BigDecimal tauxHoraireXOF) { this.tauxHoraireXOF = tauxHoraireXOF; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public List<Assignation> getAssignations() { return assignations; }
    public void setAssignations(List<Assignation> assignations) { this.assignations = assignations; }
}
