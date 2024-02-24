<template>
  <v-app-bar app color="indigo grey-lighten-3" :elevation="5" class="app-bar">
    <v-container>
      <v-row align="center">
        <v-avatar size="40">
          <img src="@/assets/logo.png" alt="Logo" class="logo" />
        </v-avatar>
        <v-col xs12 sm8 md9 lg10>
          <v-toolbar-title
              class="white--text"
              @click="$router.push('/')"
              @mouseover="setHover(true)"
              @mouseout="setHover(false)"
              :class="{ 'hovered': isHovered }"
          >
            BridgeMonitor
          </v-toolbar-title>
        </v-col>

        <v-col class="text-right">
          <v-btn v-if="isAdmin" color="transparent" elevation="0" @click="$router.push('/admin')">
            <v-icon class="white--text">mdi-cog</v-icon>
            <span class="white--text hidden-sm-and-down">Gestion</span>
          </v-btn>

          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn color="transparent" elevation="0" v-bind="attrs" v-on="on">
                <v-icon class="white--text">mdi-account</v-icon>
                <span class="white--text hidden-sm-and-down">Profil</span>
              </v-btn>
            </template>
            <v-list>
              <UserCardVue :keycloak="keycloak" @click="showProfileCardFunction"/>
            </v-list>
          </v-menu>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script>
import UserCardVue from '@/components/UserCard.vue';

export default {
  props: ["keycloak"],
  components: {
    UserCardVue
  },
  data() {
    return {
      isHovered: false,
      showProfileCard: false,
    };
  },

  methods: {
    setHover(value) {
      this.isHovered = value;
    },
    showProfileCardFunction() {
      this.showProfileCard = !this.showProfileCard;
    },
    login() {
      this.$store.commit("login");
      this.keycloak.login();
    }
  },
  computed: {
    isAdmin() {
      return this.$store.state.user.adminRole;
    },
  }
};
</script>

<style scoped>

.app-bar {
  width: 100%;
  margin-left: 0;
  margin-right: 0;
}

.logo {
  height: 40px; /* Set the desired height for your logo */
  margin-right: 10px; /* Add margin to the right if needed */
}

.hovered {
  cursor: pointer;
}
</style>