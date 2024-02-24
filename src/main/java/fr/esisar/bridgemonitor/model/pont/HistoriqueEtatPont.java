package fr.esisar.bridgemonitor.model.pont;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class HistoriqueEtatPont extends PanacheEntity{
    public LocalDateTime dateTimeChangement;

    @ManyToOne
    public Etat etat;
    
    @ManyToOne
    public Pont pont;

}
