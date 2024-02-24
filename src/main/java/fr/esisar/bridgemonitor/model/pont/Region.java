package fr.esisar.bridgemonitor.model.pont;

import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Region extends PanacheEntity{
    @Column(nullable = false)
    public String nom;
    @Column(unique = true)
    public Long codeRegion;

    @OneToMany(mappedBy = "region")
    public Set<Pont> ponts;
}
