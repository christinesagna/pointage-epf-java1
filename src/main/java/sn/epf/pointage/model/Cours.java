package sn.epf.pointage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String intitule;

    private Integer volumeHoraireTotal;
    private String niveauEtude;
    private String filiere;
    private String semestre;

    public Cours() {}

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public Integer getVolumeHoraireTotal() { return volumeHoraireTotal; }
    public void setVolumeHoraireTotal(Integer volumeHoraireTotal) { this.volumeHoraireTotal = volumeHoraireTotal; }
    public String getNiveauEtude() { return niveauEtude; }
    public void setNiveauEtude(String niveauEtude) { this.niveauEtude = niveauEtude; }
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    @Override
    public String toString() {
        return code + " - " + intitule;
    }
}
