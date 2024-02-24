package fr.esisar.bridgemonitor.model.releve;

import fr.esisar.bridgemonitor.model.capteur.CapteurAnalogique;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReleveAnalogique extends Releve{
    @Column(nullable = false)
    public Float valeur;

    @ManyToOne
    @JoinColumn(nullable = false)
    public CapteurAnalogique capteurAnalogique;
}
