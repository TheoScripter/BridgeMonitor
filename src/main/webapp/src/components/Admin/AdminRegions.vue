<template>
  <v-navigation-drawer permanent class="drawer elevate" dark>
    <v-list>
      <v-spacer></v-spacer>
      <v-list-item class="text-h6 font-weight-black custom-color">
        Regions
      </v-list-item>
      <v-spacer></v-spacer>
      <v-divider></v-divider>
      <v-list-item
          link
          v-for="region in regions"
          :key="region.id"
          :to="{ name: 'AdminListePonts', params: { id_region: region.id } }"
          class="region-item"
      >
        <v-list-item-content class="d-flex align-center">
          <v-list-item-title class="region-link" @mouseover="onMouseOver(region.id)" @mouseleave="onMouseLeave">
            <v-row align="center" justify="space-between" class="w-100">
              <v-col>{{ region.nom }}</v-col>
            </v-row>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: "AdminRegions",
  data() {
    return {
      regions: [],
      highlightedRegion: null,
    };
  },
  computed: {
    regions1() {
      return this.$store.getters['regions/getAllRegions'];
    },
  },
  methods: {
    onMouseOver(regionId) {
      this.highlightedRegion = regionId;
    },
    onMouseLeave() {
      this.highlightedRegion = null;
    },
  },
  watch: {
    regions1: function (regions1) {
      this.regions = regions1;
    },
  },
  mounted() {
    this.$store.dispatch('regions/getRegions');
  },
};
</script>

<style scoped>

.custom-color {
  background-color: #3e51b4;
  color: #ffffff;
}

.drawer {
  color: #ffffff;
}

.region-item {
  transition: background-color 0.3s ease;
}

.region-item:hover {
  background-color: #012979;
}

.region-link {
  color: #ffffff;
  transition: color 0.3s ease;
}

.region-link:hover {
  color: #64B5F6;
}

.align-center {
  align-items: center;
}

.elevate {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}
</style>
