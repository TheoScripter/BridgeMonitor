<template>
  <v-app>
    <v-main>
      <TopNavBar :keycloak="keycloak"></TopNavBar>
      <transition name="fade" mode="out-in">
        <router-view></router-view>
      </transition>
    </v-main>
  </v-app>
</template>

<script>
import TopNavBar from "@/components/TopNavBar.vue";

export default {
  name: 'App',
  props: ["keycloak"],
  components: {
    TopNavBar,
  }
};
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
