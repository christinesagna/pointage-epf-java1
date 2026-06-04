package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.StatutPointage;
import sn.epf.pointage.model.enums.TypePointage;

import java.time.LocalDateTime;

@Entity
@Table(name = "pointages")
public class Pointage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seance_id")
    private SeancePlanifiee seance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @Column(nullable = false)
    private LocalDateTime heurePointage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePointage typePointage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPointage statut;

    private String observations;

    public Pointage() {}

    public Long getId() { return id; }
    public SeancePlanifiee getSeance() { return seance; }
    public void setSeance(SeancePlanifiee seance) { this.seance = seance; }
    public Professeur getProfesseur() { return professeur; }
    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }
    public LocalDateTime getHeurePointage() { return heurePointage; }
    public void setHeurePointage(LocalDateTime heurePointage) { this.heurePointage = heurePointage; }
    public TypePointage getTypePointage() { return typePointage; }
    public void setTypePointage(TypePointage typePointage) { this.typePointage = typePointage; }
    public StatutPointage getStatut() { return statut; }
    public void setStatut(StatutPointage statut) { this.statut = statut; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
