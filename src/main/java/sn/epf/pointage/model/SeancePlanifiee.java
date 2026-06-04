package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.StatutSeance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seances_planifiees")
public class SeancePlanifiee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignation_id")
    private Assignation assignation;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(nullable = false)
    private Integer dureeMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSeance statut = StatutSeance.PLANIFIEE;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pointage> pointages = new ArrayList<>();

    public SeancePlanifiee() {}

    public Long getId() { return id; }
    public Assignation getAssignation() { return assignation; }
    public void setAssignation(Assignation assignation) { this.assignation = assignation; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    public Integer getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public StatutSeance getStatut() { return statut; }
    public void setStatut(StatutSeance statut) { this.statut = statut; }
    public List<Pointage> getPointages() { return pointages; }
    public void setPointages(List<Pointage> pointages) { this.pointages = pointages; }
}
