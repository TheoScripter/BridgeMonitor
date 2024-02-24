<template>
  <v-container fluid>
    <v-row>
      <v-col cols="8">
        <CarteFrance></CarteFrance>
      </v-col>
      <v-col cols="3">
        <ListeRegions></ListeRegions>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import CarteFrance from "@/components/Region/CarteFrance.vue";
import ListeRegions from "@/components/Region/ListeRegions.vue";

export default {
  components: { CarteFrance, ListeRegions },
};
</script>

<style scoped>
.carte-container {
  height: 100%;
}
</style>