<template>
  <div class="container-fluid">
    <v-row>
      <div class="col-lg-11 col-md-12 col-sm-12 mx-auto">
        <v-card class="map-container">
          <div ref="map" class="leaflet-map"></div>
        </v-card>
      </div>
    </v-row>
  </div>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import geojson from '@/assets/regions.geojson';

export default {
  mounted() {
    this.initializeMap();
  },

  methods: {
    initializeMap() {
      const map = L.map(this.$refs.map, {
        center: [46.603354, 2.468930],
        zoom: 6,
        zoomControl: false,
        scrollWheelZoom: false,
        dragging: false,
        tap: false,
      });

      L.tileLayer('https://tiles.stadiamaps.com/tiles/alidade_smooth/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      }).addTo(map);

      L.geoJSON(geojson, {
        style: {
          color: 'blue',
          weight: 1,
          fillOpacity: 0.1,
        },
        onEachFeature: this.onEachRegion,
      }).addTo(map);
    },

    onEachRegion(feature, layer) {
      const regionName = feature.properties.nom;
      const regionCode = feature.properties.code;

      const popupContent = `
        <div>
          <h3>${regionName} : ${regionCode}</h3>
        </div>
      `;

      layer.bindPopup(popupContent);

      layer.on({
        mouseover: () => {
          layer.openPopup();
          const center = layer.getBounds().getCenter();
          const popup = layer.getPopup();
          popup.setLatLng(center);
        },
        mouseout: () => {
          layer.closePopup();
        },
        click: () => {
          // Lorsqu'on clique, vous pouvez ajouter ici le code pour rediriger vers un autre composant ou effectuer une action
          console.log(`Clicked on ${regionName}`);
        },
      });
    },
  },
};
</script>

<style scoped>
.map-container {
  height: 75vh; 
  margin: 0 10px;
  border: 2px solid #ddd; 
  border-radius: 20px; 

}

.leaflet-map {
  height: 100%;
}
</style>
