package fr.esisar.bridgemonitor.model.pont;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Etat extends PanacheEntity{
    @Column(nullable = false)
    public String nom;
}
