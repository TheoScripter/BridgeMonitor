<template>
  <v-container>
    <v-row>
      <v-col>
        <v-btn @click="openDialog">Filtrer</v-btn>
        <v-dialog v-model="isDialogOpen" max-width="500px">
          <v-card>
            <v-card-title class="text-center" style="color: #3e50b5;">
              <span class="headline">Sélectionner une période</span>
            </v-card-title>
            <v-card-text class="text-center">
              <v-btn @click="applyFilter('today')" color="#3e50b5" text block :class="{'selected' : currentFilter === 'today'}">Aujourd'hui</v-btn>
              <v-btn @click="applyFilter('thisWeek')" color="#3e50b5" text block :class="{'selected' : currentFilter === 'thisWeek'}">Cette semaine</v-btn>
              <v-btn @click="applyFilter('thisMonth')" color="#3e50b5" text block :class="{'selected' : currentFilter === 'thisMonth'}">Ce mois</v-btn>
              <v-btn @click="applyFilter('thisYear')" color="#3e50b5" text block :class="{'selected' : currentFilter === 'thisYear'}">Cette année</v-btn>
            </v-card-text>
          </v-card>
        </v-dialog>
      </v-col>
    </v-row>
  </v-container>
</template>



<script>
export default {
  data() {
    return {
      isDialogOpen: false,
      currentFilter : 'today',
    };
  },
  methods: {
    openDialog() {
      this.isDialogOpen = true;
    },
    closeDialog() {
      this.isDialogOpen = false;
    },
    applyFilter(filter) {
      // Appeler la méthode pour appliquer le filtre dans votre composant parent
      this.$emit('apply-filter', filter);
      this.currentFilter = filter;
      this.closeDialog();
    },
  },
};
</script>

<style>
.selected {
  border: 2px solid #3e50b5;
  padding: 0.2rem 0.4rem;
  background-color: #eee;
}
</style>
