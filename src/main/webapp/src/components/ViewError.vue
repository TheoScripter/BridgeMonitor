<!-- OverViewError.vue -->

<template>
  <v-container fluid>
    <v-layout align-center justify-center>
      <v-flex>
        <v-card class="error-card" elevation="12">
          <v-card-title class="error-title">
            <v-icon class="mr-2" color="white">mdi-alert</v-icon>
            <h2 class="white--text">Erreur</h2>
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text class="error-message">
            <p class="grey--text text--darken-3">
              Désolé, la page que vous recherchez n'existe pas.
            </p>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {};
</script>

<style scoped>
.error-card {
  max-width: 400px;
  margin: auto;
}

.error-title {
  background-color: #3e50b5;
  color: white;
  text-align: center;
}

.error-message {
  text-align: center;
}
</style>
