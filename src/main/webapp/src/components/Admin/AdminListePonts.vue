<template>
  <v-card class="custom-card elevate">

  <!-- En tete du tableau + Bouton création pont -->
    <v-card-title class="text-center indigo text--lighten-3" no-border>
      <h3>Ponts</h3>
     
      <v-spacer></v-spacer>
      <v-dialog v-model="dialogCreatePont" max-width="500px">
            <template v-slot:activator="{ on }">
                <v-btn
                  color= "#2c8fa8"
                  dark
                  class="mb-2"
                  v-on="on"
                  >+</v-btn>
            </template>

            <!-- Boite de dialogue pour la creation d'un pont -->
            <v-card> 
                  <v-card-title>
                    <h2>Ajout d'un pont</h2>
                  </v-card-title>
                  <v-card-text>
                      <v-text-field v-model="pontToAdd.nom" label="Nom du pont" required/>
                      <v-text-field v-model="pontToAdd.longueur" label="longueur" />
                      <v-text-field v-model="pontToAdd.largeur" label="largeur" />
                      <v-text-field v-model="pontToAdd.latitude" label="latitude" />
                      <v-text-field v-model="pontToAdd.longitude" label="longitude" />
                      <v-text-field v-model="pontToAdd.dateCreation" label="dateCreation" type="date"/>
                      <v-select
                        v-model="pontToAdd.etatId"
                        :items="etats"
                        item-text= "nom"
                        item-value= "id"
                        label="Etat"
                        required
                      ></v-select>
                      <v-select
                        v-model="pontToAdd.regionId"
                        :items="regions"
                        item-text= "nom"
                        item-value= "id"
                        label="Region"
                        required
                      ></v-select>
                   </v-card-text>

                   <v-card-actions>
                      <v-spacer/>
                      <v-btn @click="savePont">Ajouter</v-btn>
                  </v-card-actions>
            </v-card>
      </v-dialog>
    </v-card-title>

  <!-- Contenu du tableau : données et actions-->
    <v-card-text>
      <v-data-table v-if="ponts.length>0" :headers="headers" :items="ponts">
      
        <template v-slot:[`item.redirectCapteurs`]="{ item }">
          <v-icon @click="() => showCapteurs(item.id)"> mdi-format-list-bulleted-type</v-icon>
        </template>

        <template v-slot:[`item.actions`]="{ item }">
            <v-icon @click="editPont(item)">mdi-pencil</v-icon>
            <v-icon @click="deletePont(item.id)">mdi-delete</v-icon>
        </template>

        <!-- Boites de dialogue modification et suppression-->
        <template v-slot:top>
              <v-dialog v-model="dialogDelete" max-width="500">
                  <v-card>
                      <v-card-title>
                          <h4>Etes-vous sûr de vouloir le supprimer ?</h4>
                      </v-card-title>
                      <v-card-actions>
                          <v-spacer/>
                          <v-btn text @click="closeDelete">Annuler</v-btn>
                          <v-btn text @click="confirmDelete">OK</v-btn>
                      </v-card-actions>
                  </v-card>
              </v-dialog>
            
              <v-dialog v-model="dialogEdit" max-width="500">
                  <v-card>
                        <v-card-title>
                            <h4>Mise à jour de : {{editedPont.nom}}</h4>
                        </v-card-title>
                        <v-card-text>
                            <v-text-field v-model="editedPont.id" label="Id du pont" readonly disabled/>
                            <v-text-field v-model="editedPont.nom" label="Nom du pont" required/>
                            <v-text-field v-model="editedPont.longueur" label="longueur" />
                            <v-text-field v-model="editedPont.largeur" label="largeur" />
                            <v-text-field v-model="editedPont.latitude" label="latitude" />
                            <v-text-field v-model="editedPont.longitude" label="longitude" />
                            <v-text-field v-model="editedPont.dateCreation" label="dateCreation" type="date"/>
                            <v-select
                              v-model="editedPont.etatId"
                              :items="etats"
                              item-text= "nom"
                              item-value= "id"
                              label="Etat"
                              required
                            ></v-select>
                            
                            <v-select
                              v-model="editedPont.regionId"
                              :items="regions"
                              item-text= "nom"
                              item-value= "id"
                              label="Region"
                              required
                            ></v-select>
                        </v-card-text>
                      <v-card-actions>
                          <v-spacer/>
                          <v-btn text @click="closeUpdate">Annuler</v-btn>
                          <v-btn text @click="confirmUpdate">OK</v-btn>
                      </v-card-actions>
                  </v-card>
              </v-dialog>   
        </template>
      </v-data-table>

      <div v-else>
        <br>
        <p>Auncun pont dans cette région</p>
      </div>
    </v-card-text>

  </v-card>
</template>

<script>
export default {
  name: "AdminListePonts",
  data() {
    return {
      region_ID: 0,
      regions: [],
      ponts: [],
      etats: [],

      headers: [
        { text : "Id", sortable: true, value : "id"},
        { text: "Nom", sortable: true, value: "nom" },
        { text : "Capteurs", sortable: false, value: "redirectCapteurs"},
        { text : "Update/Delete", sortable: false, value: "actions"},
      ],

      //Modifications
      dialogEdit: false,
      editedPont:{
          id: null,
          nom: "0",
          longueur: 0,
          largeur: 0,
          latitude: "string",
          longitude: "string",
          dateCreation: "2022-03-10",
          etatId: 0,
          regionId: 0,
          capteursId: []
      },

      //Suppression
      dialogDelete: false,
      idDelete:-1,

      //Creation
      dialogCreatePont: false,
      pontToAdd : {
          id: null,
          nom: "0",
          longueur: 0,
          largeur: 0,
          latitude: "string",
          longitude: "string",
          dateCreation: "2022-03-10",
          etatId: 0,
          regionId: 0,
          capteursId: []
      },
      defaultPont:{
          id: null,
          nom: "0",
          longueur: 0,
          largeur: 0,
          latitude: "string",
          longitude: "string",
          dateCreation: "2022-03-10",
          etatId: 0,
          regionId: 0,
          capteursId: []
      }

    };
  },

  mounted() {
    this.region_ID = this.$route.params.id_region;
    this.fetchData();
    this.$store.dispatch('regions/getRegions');
    this.$store.dispatch('etats/getallEtats');

  },

  computed: {
    ponts1() {
      return this.$store.getters["regions/getPontsInRegion"];
    },

    allregions() {
      return this.$store.getters['regions/getAllRegions'];
    },
    etats1(){
      return this.$store.getters["etats/getEtats"];
    }
  },

  watch: {
    '$route.params.id_region'(newRegionId, oldRegionId) {
      if (newRegionId !== oldRegionId) {
        this.region_ID = newRegionId;
        this.fetchData();
      }
    },

    ponts1:function(ponts) {
      this.ponts = ponts;
      console.log(this.ponts);
    },

     allregions:function(regions1) {
      this.regions = regions1;
    },
     etats1: function(etats){
      this.etats = etats;
      console.log("etats existants",this.etats);
    }
  },

  methods: {
    //Recharger les données
    async fetchData() {
      await this.$store.dispatch("regions/getPonts", this.region_ID);
    },

    //Modification d'un pont avec boite de dialogue____________
    editPont(pont){
      console.log("pont à modifier :",pont.etat.id);
      this.editedPont = {
        id: pont.id || 0,
        nom: pont.nom || "",
        longueur: pont.longueur || 0,
        largeur: pont.largeur || 0,
        latitude: pont.latitude || "",
        longitude: pont.longitude || "",
        dateCreation: pont.dateCreation || "",
        etatId: pont.etat.id,
        regionId: pont.region.id,
        capteursId: pont.capteurs ? pont.capteurs.map(capteur => capteur.id || 0) : []
      }
      console.log("pont edition",this.editedPont.etatId);
      this.dialogEdit = true;
    },
    closeUpdate(){
        this.dialogEdit = false;
        console.log(this.editedPont.regionId);
    },
    async confirmUpdate(){
        await this.$store.dispatch("ponts/updatePont",this.editedPont);
        this.dialogEdit = false;
        await new Promise(resolve => setTimeout(resolve, 800)); //Attendre que l'historique soit changé dans la base
        await this.fetchData();
    },

    //Suppression d'un pont____________________________________
    deletePont(id){
      this.idDelete = id;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    async confirmDelete(){
      await this.$store.dispatch("ponts/deletePont",this.idDelete);
      this.dialogDelete = false;
      await new Promise(resolve => setTimeout(resolve, 800)); //Attendre que l'historique soit changé dans la base
      await this.fetchData();
    },

    //Capteurs d'un pont_______________________________________
    showCapteurs(pont_id){
      console.log("capteur du pont d'id ",pont_id);
      this.$router.push({ name:'AdminListeCapteurs',params:{id_pont: pont_id} });
    },

    //Creation d'un pont_____________________________________
    async savePont(){
      var data = {
          id: null,
          nom: this.pontToAdd.nom,
          longueur: this.pontToAdd.longueur,
          largeur: this.pontToAdd.largeur,
          latitude: this.pontToAdd.latitude,
          longitude: this.pontToAdd.longitude,
          dateCreation: this.pontToAdd.dateCreation,
          etatId: this.pontToAdd.etatId,
          regionId: this.pontToAdd.regionId,
          capteursId: []
      }
      console.log("pont à créer",data);
      await this.$store.dispatch("ponts/createPont",data);
      Object.assign(this.pontToAdd,this.defaultPont);
      
      this.dialogCreatePont = false;
      await new Promise(resolve => setTimeout(resolve, 800)); //Attendre que l'historique soit changé dans la base
      await this.fetchData();

    }
  },
};
</script>


<style scoped>lo
tbody tr {
  cursor: pointer;
}

.custom-card {
  margin: 20px;
}

.elevate {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}

.indigo {
  background-color: #3e50b5 !important;
  color: #ffffff !important;
}
</style>