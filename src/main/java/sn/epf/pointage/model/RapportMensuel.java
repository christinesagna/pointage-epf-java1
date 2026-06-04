package sn.epf.pointage.model;

import jakarta.persistence.*;
import sn.epf.pointage.model.enums.StatutRapport;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "rapports_mensuels",
        uniqueConstraints = @UniqueConstraint(columnNames = {"professeur_id", "mois", "annee"}))
public class RapportMensuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professeur_id")
    private Professeur professeur;

    @Column(nullable = false)
    private int mois;

    @Column(nullable = false)
    private int annee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal heuresRealisees = BigDecimal.ZERO;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal montantXOF = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRapport statut = StatutRapport.EN_ATTENTE;

    public RapportMensuel() {}

    public Long getId() { return id; }
    public Professeur getProfesseur() { return professeur; }
    public void setProfesseur(Professeur professeur) { this.professeur = professeur; }
    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    public BigDecimal getHeuresRealisees() { return heuresRealisees; }
    public void setHeuresRealisees(BigDecimal heuresRealisees) { this.heuresRealisees = heuresRealisees; }
    public BigDecimal getMontantXOF() { return montantXOF; }
    public void setMontantXOF(BigDecimal montantXOF) { this.montantXOF = montantXOF; }
    public StatutRapport getStatut() { return statut; }
    public void setStatut(StatutRapport statut) { this.statut = statut; }
}
