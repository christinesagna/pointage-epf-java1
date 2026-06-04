package sn.epf.pointage.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignations")
public class Assignation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cours_id")
    private Cours cours;

    @ManyToOne(optional = false)
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @Column(nullable = false)
    private String anneeAcademique;

    @Column(precision = 10, scale = 2)
    private BigDecimal heuresPrevues;

    @OneToMany(mappedBy = "assignation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeancePlanifiee> seances = new ArrayList<>();

    public Assignation() {}

    public Long getId() { return id; }
    public Professeur getProfesseur() { return professeur; }
    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }
    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }
    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }
    public String getAnneeAcademique() { return anneeAcademique; }
    public void setAnneeAcademique(String anneeAcademique) { this.anneeAcademique = anneeAcademique; }
    public BigDecimal getHeuresPrevues() { return heuresPrevues; }
    public void setHeuresPrevues(BigDecimal heuresPrevues) { this.heuresPrevues = heuresPrevues; }
}
