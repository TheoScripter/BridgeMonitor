<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="elevation-12">
          <v-card-title class="text-center">
            <v-icon size="64">mdi-account-circle</v-icon>
            <h2 class="mt-2">Login</h2>
          </v-card-title>

          <v-card-text>
            <v-form @submit.prevent="login">
              <v-text-field v-model="username" label="Username" outlined required></v-text-field>
              <v-text-field v-model="password" label="Password" outlined type="password" required></v-text-field>

              <v-btn type="submit" color="indigo grey--lighten-3" block class="white--text">Login</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      // Ajoutez ici la logique d'authentification
      console.log('Username:', this.username);
      console.log('Password:', this.password);
    },
  },
};
</script>

<style scoped>
.elevation-12 {
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.1);
}
</style>
