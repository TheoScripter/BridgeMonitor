package fr.esisar.bridgemonitor.model.capteur.unite;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class UniteAnalogique extends PanacheEntity{
    @Column(nullable = false)
    public String nom;
    public String symbole;
}
