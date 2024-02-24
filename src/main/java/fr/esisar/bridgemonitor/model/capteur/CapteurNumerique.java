package fr.esisar.bridgemonitor.model.capteur;

import java.util.Set;
import java.util.stream.Collectors;

import fr.esisar.bridgemonitor.model.capteur.unite.UniteNumerique;
import fr.esisar.bridgemonitor.model.releve.ReleveNumerique;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

@Entity
@NamedQueries(
    @NamedQuery(name = "CapteurNumerique.getByPontId",query = "SELECT c FROM CapteurNumerique c WHERE c.pont.id = :pontId")
)
public class CapteurNumerique extends Capteur{

    @ManyToOne
    public UniteNumerique uniteNumerique;

    @OneToMany(mappedBy = "capteurNumerique")
    public Set<ReleveNumerique> releveNumeriques;

    public static Set<Capteur> getByPontId(Long pontId){

        PanacheQuery<CapteurNumerique> panacheQuery = find("#CapteurNumerique.getByPontId", Parameters.with("pontId", pontId));
        return panacheQuery.list().stream().collect(Collectors.toSet());
    }
}
