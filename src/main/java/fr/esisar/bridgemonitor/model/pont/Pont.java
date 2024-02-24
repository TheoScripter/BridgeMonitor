package fr.esisar.bridgemonitor.model.pont;

import java.time.LocalDate;
import java.util.Set;

import fr.esisar.bridgemonitor.model.capteur.Capteur;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Pont extends PanacheEntity{
    public String nom;
    public Float longueur;
    public Float largeur;
    public String latitude;
    public String longitude;

    @Column(nullable = true)
    public LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Etat etat;

    @OneToMany(mappedBy = "pont")
    public Set<HistoriqueEtatPont> historiqueEtatPonts;
    
    @OneToMany(mappedBy = "pont")
    public Set<Capteur> capteurs;

    @ManyToOne
    @JoinColumn(nullable = false, unique = false)
    public Region region;
}
