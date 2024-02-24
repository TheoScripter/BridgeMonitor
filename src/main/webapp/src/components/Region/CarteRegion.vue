<template>
  <v-card class="map-container mx-auto">
    <div ref="map" class="leaflet-map"></div>
  </v-card>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import geojson from '@/assets/regions.geojson';
import logo from '@/assets/icons8-bridge-50.png';

export default {
  name: "CarteRegion",
  data() {
    return {
      region_ID: 0,
      region_code: 0,
      ponts: [], //Recevoir les données des ponts du fils
      mapInitialized: false
    };
  },

  mounted() {
    this.region_ID = this.$route.params.id_region;
    this.$store.dispatch('regions/getRegion', this.region_ID);
    this.$store.dispatch('regions/getPonts', this.region_ID);
    
  },

  computed: {
    region1(){
      return this.$store.getters['regions/getRegionUnique'];
    },
    ponts1() {
      return this.$store.getters['regions/getPontsInRegion'];
    }
  },

  watch: {

    region1:function(region){
      this.region_ID = region.id;
      this.region_code = region.codeRegion;
      if (this.ponts.length > 0 && this.region_code !== 0) {
        this.initializeMap();
      }
    },
    ponts1:function(ponts) {
      this.ponts = ponts;
      if (this.region_code !== 0 && ponts.length > 0) {
         this.initializeMap();
      }
    }
  },

  methods: {

    initializeMap() {
      if (this.mapInitialized ) {
        // Map deja initialisée
        return;
      }

      //Initialisatier la carte et centrer sur la région
      const specificRegion = geojson.features.find(feature => feature.properties.code === this.region_code.toString());

      if (!specificRegion) console.log("Région non trouvée dans le geojson");

      const centerCoordinates = L.geoJSON(specificRegion).getBounds().getCenter();

      const map = L.map(this.$refs.map, {
        center: [centerCoordinates.lat, centerCoordinates.lng],
        zoom: 7,
        tap: false,
      });


      //TileLayer
      L.tileLayer('https://tiles.stadiamaps.com/tiles/alidade_smooth/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(map);

      //Contour de la région
      geojson.features.forEach(feature => {
        const regionCode = feature.properties.code;
        if (regionCode === this.region_code.toString()) {
          L.geoJSON(feature, {
            style: {
              color: 'blue',
              weight: 2,
              fillOpacity: 0.1,
            },
          }).addTo(map);
        }
      });

      //Choix de l'icone pour le pont
      var customIcon = L.icon({
      iconUrl: logo,
      iconSize: [30, 30], 
      });
      console.log("ponts :",this.ponts);

      //Placement des marqueurs aux positions des ponts
      for (let index = 0; index < this.ponts.length; index++) {

        const pont = this.ponts[index];
        const marker = L.marker([pont.latitude, pont.longitude], { icon: customIcon }).addTo(map);
        console.log(marker);

        // Pop-up
        marker.bindPopup(`${pont.nom}`);

        marker.on('mouseover', function () {
          this.openPopup();
        });

        marker.on('mouseout', function () {
          this.closePopup();
        });
      }

      //Fin de l'initialisation
       this.mapInitialized = true;
    },
  },
};
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 500px; /* Ajustez la hauteur selon vos besoins */
  border-radius: 10px;
  margin: 20px;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}

.leaflet-map {
  height: 100%;
}

</style>
