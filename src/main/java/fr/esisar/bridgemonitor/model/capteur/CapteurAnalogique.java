package fr.esisar.bridgemonitor.model.capteur;

import java.util.Set;
import java.util.stream.Collectors;

import fr.esisar.bridgemonitor.model.capteur.unite.UniteAnalogique;
import fr.esisar.bridgemonitor.model.releve.ReleveAnalogique;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

@Entity
@NamedQueries(
    @NamedQuery(name = "CapteurAnalogique.getByPontId",query = "SELECT c FROM CapteurAnalogique c WHERE c.pont.id = :pontId")
)
public class CapteurAnalogique extends Capteur {
    public Long periodicite;

    @ManyToOne
    public UniteAnalogique uniteAnalogique;

    @OneToMany(mappedBy = "capteurAnalogique")
    public Set<ReleveAnalogique> releveAnalogiques;

    public static Set<Capteur> getByPontId(Long pontId){

        PanacheQuery<CapteurAnalogique> panacheQuery = find("#CapteurAnalogique.getByPontId", Parameters.with("pontId", pontId));
        return panacheQuery.list().stream().collect(Collectors.toSet());
    }
}
