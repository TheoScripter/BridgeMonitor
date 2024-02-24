<template>
  <v-card>
    <v-card-text>
      <v-row justify="space-around">
        <v-avatar color="blue">
          <v-icon dark>mdi-account-circle</v-icon>
        </v-avatar>
      </v-row>
      <v-row justify="space-around" class="mt-6">
        <div class="text-h6">{{ $store.state.user.givenName }} {{$store.state.user.familyName }} </div>
      </v-row>
      <v-row justify="space-around">
        <div class="text-caption mt-1">{{ $store.state.user.email }}</div>
      </v-row>
      <v-row justify="space-around">
        <div class="red--text mt-1" v-if="$store.state.user.adminRole">Admin</div>
      </v-row>
    </v-card-text>
    <v-card-actions>
      <v-row justify="space-around">
        <v-btn class="ma-4" outlined color="blue"
               @click="logoutUser">Déconnexion</v-btn>
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "UserCardVue",
  props: ["keycloak"],
  data() {
    return {
      admin: false
    }
  },
  methods: {
    logoutUser() {
      this.$store.commit("logout");
      this.keycloak.logout();
      sessionStorage.setItem("vue-token", "");
      localStorage.removeItem('keycloak-token')
    }
  },
}
</script>

