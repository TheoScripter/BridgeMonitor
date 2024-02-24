<template>
  <v-container fluid>
    <v-row>
      <v-col cols="4">
        <CarteRegion></CarteRegion>
      </v-col>
      <v-col cols="8">
        <ListePonts></ListePonts>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import CarteRegion from '@/components/Region/CarteRegion.vue';
import ListePonts from '@/components/Region/ListePonts.vue';

export default {
  components: { CarteRegion, ListePonts },
};
</script>

<style scoped>
.carte-container {
  height: 100%;
}
</style>