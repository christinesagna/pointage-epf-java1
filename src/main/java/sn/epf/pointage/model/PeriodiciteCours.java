package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.Frequence;
import sn.epf.pointage.model.enums.JourSemaineCours;

import java.time.LocalTime;

@Entity
@Table(name = "periodicites_cours")
public class PeriodiciteCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignation_id")
    private Assignation assignation;

    @Enumerated(EnumType.STRING)
    private JourSemaineCours jourSemaine;

    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private Frequence frequence;

    public PeriodiciteCours() {}

    public Long getId() { return id; }
    public Assignation getAssignation() { return assignation; }
    public void setAssignation(Assignation assignation) { this.assignation = assignation; }
    public JourSemaineCours getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(JourSemaineCours jourSemaine) { this.jourSemaine = jourSemaine; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    public Frequence getFrequence() { return frequence; }
    public void setFrequence(Frequence frequence) { this.frequence = frequence; }
}
