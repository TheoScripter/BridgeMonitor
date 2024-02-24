package fr.esisar.bridgemonitor.model.releve;

import fr.esisar.bridgemonitor.model.capteur.CapteurNumerique;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReleveNumerique extends Releve{
    @Column(nullable = false)
    public Boolean valeur;

    @ManyToOne
    @JoinColumn(nullable = false)
    public CapteurNumerique capteurNumerique;
}
