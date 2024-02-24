package fr.esisar.bridgemonitor.model.capteur;

import fr.esisar.bridgemonitor.model.pont.Pont;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Capteur extends PanacheEntity {
    public String nom;
    public String description;
    public String numeroSerie;
    
    @ManyToOne
    @JoinColumn(nullable = true, unique = false)
    public Pont pont;

}
