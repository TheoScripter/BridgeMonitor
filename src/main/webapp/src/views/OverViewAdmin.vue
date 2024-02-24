<!--
<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col cols="6">
          <AdminRegions></AdminRegions>
        </v-col>
        <v-col cols="6">
          <AddRegion></AddRegion>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>
-->

<template>
  <v-app>
    <v-row>
      <AdminRegions></AdminRegions>
      <v-main>
        <router-view></router-view>
      </v-main>
    </v-row>
  </v-app>
</template>


<script>
  import AdminRegions from "@/components/Admin/AdminRegions.vue";
  //import AddRegion from "@/components/Region/AddRegion.vue";

  export default {
    components: {
      AdminRegions
      //AddRegion
    }
  }

  /*
  import AdminRegions from "@/components/Admin/AdminRegions.vue";
  import AddRegion from "@/components/Region/AddRegion.vue";

  export default {
    components: {
      AdminRegions,
      AddRegion
    }
  }
  */

</script>

