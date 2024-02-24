

<template>
    <v-card class="custom-card elevate">
      <v-card-title class="text-center indigo text--lighten-3">
        <h3>Capteurs</h3>

      <!-- Bouton et boite de dialogue pour la creation d'un capteur -->
        <v-spacer></v-spacer>

        <!--Creation capteur Analogique-->
        <v-dialog v-model="dialogCreateNum" max-width="500px">
              <template v-slot:activator="{ on }">
                  <v-btn
                    color= "#006400"
                    dark
                    class="mb-2"
                    style="margin-right: 10px;"
                    v-on="on"
                    >+ Numerique</v-btn>
              </template>
              <v-card> 
                    <v-card-title>
                      <h2>Ajout d'un capteur numerique</h2>
                    </v-card-title>
                    <v-card-text>
                        <v-text-field v-model="this.pont_ID" label="Id du pont" readonly disabled/>
                        <v-text-field v-model="createNum.nom" label="Nom du capteur" required/>
                        <v-text-field v-model="createNum.description" label="description" />
                        <v-text-field v-model="createNum.numeroSerie" label="numeroSerie" />
                        <v-select
                          v-model="createNum.uniteNumeriqueId"
                          :items="unitesNumeriques"
                          item-text= "etatHaut"
                          item-value= "id"
                          label="unite (Etat Haut = ...)"
                          required
                        ></v-select>
                    </v-card-text>

                    <v-card-actions>
                        <v-spacer/>
                        <v-btn @click="saveNumerique">Ajouter</v-btn>
                    </v-card-actions>
              </v-card>
        </v-dialog>

        <!--Creation capteur Numerique-->
        <v-dialog v-model="dialogCreateAnalog" max-width="500px">
              <template v-slot:activator="{ on }">
                  <v-btn
                    color= "#9acd32"
                    dark
                    class="mb-2"
                    v-on="on"
                    >+ Analogique</v-btn>
              </template>
              <v-card> 
                    <v-card-title>
                      <h2>Ajout d'un capteur analogique</h2>
                    </v-card-title>
                    <v-card-text>
                        <v-text-field v-model="this.pont_ID" label="Id du pont" readonly disabled/>
                        <v-text-field v-model="createAnalog.nom" label="Nom du capteur" required/>
                        <v-text-field v-model="createAnalog.description" label="description" />
                        <v-text-field v-model="createAnalog.numeroSerie" label="numeroSerie" />
                        <v-text-field v-model="createAnalog.periodicite" label="periodicite" />
                        <v-select
                          v-model="createAnalog.uniteAnalogiqueId"
                          :items="unitesAnalogiques"
                          item-text= "nom"
                          item-value= "id"
                          label="unité"
                          required
                        ></v-select>
                    </v-card-text>

                    <v-card-actions>
                        <v-spacer/>
                        <v-btn @click="saveAnalogique">Ajouter</v-btn>
                    </v-card-actions>
              </v-card>
        </v-dialog>
      </v-card-title>

      <v-card-text>
        <v-data-table v-if="capteurs.length>0" :headers="headers" :items="capteurs">
            <template v-slot:[`item.actions`]="{ item }">
                <v-icon @click="editCapteur(item.id)">mdi-pencil</v-icon>
                <v-icon @click="deleteCapteur(item.id)">mdi-delete</v-icon>
            </template>

              <template v-slot:top> 

                  <!--Boite de dialogue modification capteur-->

                    <v-dialog v-model="dialogEdit" max-width="500">
                      <!-- Si le capteur est analogique -->
                        <v-card v-if="isAnalog == true"> 
                          <v-card-title>
                            <h4>Mise à jour du capteur analogique : </h4>
                            <h5>{{ editedAnalogique.nom }}</h5>
                          </v-card-title>
                          <v-card-text>
                                    <v-text-field v-model="editedAnalogique.pontId" label="Id du pont" readonly disabled/>
                                    <v-text-field v-model="editedAnalogique.id" label="Id du capteur" readonly disabled/>
                                    <v-text-field v-model="editedAnalogique.nom" label="Nom du capteur" required/>
                                    <v-text-field v-model="editedAnalogique.description" label="description" />
                                    <v-text-field v-model="editedAnalogique.numeroSerie" label="numeroSerie" />
                                    <v-text-field v-model="editedAnalogique.periodicite" label="periodicite" />
                                    <v-select
                                      v-model="editedAnalogique.uniteAnalogiqueId"
                                      :items="unitesAnalogiques"
                                      item-text= "nom"
                                      item-value= "id"
                                      label="unité"
                                      required
                                    ></v-select>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn text @click="closeUpdate">Annuler</v-btn>
                            <br>
                            <v-btn text @click="confirmUpdateAnalogique">OK</v-btn>
                          </v-card-actions>
                        </v-card>
                      
                        <!-- Si le capteur est numérique -->
                        
                        <v-card v-else-if="isNum == true"> 
                          <v-card-title>
                            <h4>Mise à jour du capteur numérique :</h4>
                             <h5>{{ editedNumerique.nom }}</h5>
                          </v-card-title>
                          <v-card-text>
                                    <v-text-field v-model="editedNumerique.pontId" label="Id du pont" readonly disabled/>
                                    <v-text-field v-model="editedNumerique.id" label="Id du capteur" readonly disabled/>
                                    <v-text-field v-model="editedNumerique.nom" label="Nom du capteur" required/>
                                    <v-text-field v-model="editedNumerique.description" label="description" />
                                    <v-text-field v-model="editedNumerique.numeroSerie" label="numeroSerie" />
                                    <v-select
                                      v-model="editedNumerique.uniteNumeriqueId"
                                      :items="unitesNumeriques"
                                      item-text= "etatHaut"
                                      item-value= "id"
                                      label="unite (Etat Haut = ...)"
                                      required
                                    ></v-select>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn text @click="closeUpdate">Annuler</v-btn>
                            <v-btn text @click="confirmUpdateNumerique">OK</v-btn>
                          </v-card-actions>
                        </v-card>

                        <v-card v-else> 
                            <v-card-text>
                              <h4>Type inconnu</h4>
                            </v-card-text>
                        </v-card>

                    </v-dialog>

                  <!--Boite de dialogue Suppression-->
                    
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
                </template>

        </v-data-table>
        
        <div v-else>
          <br>
          <p>Aucun capteur sur ce pont</p>
        </div> 

      </v-card-text>

    </v-card>
    
</template>




<script>
import CapteurService from "@/services/CapteurService";

export default {
  name: "AdminListeCapteurs",

  data() {
    return {
      pont_ID: 0,
      capteurs:[],

      capteurToEdit : null,

      isAnalog:false,
      isNum:false,

      unitesAnalogiques:[],
      unitesNumeriques: [],

      headers:[
        { text: "Id", sortable:true, value: "id"},
        { text: "Nom", sortable: true, value: "nom" },
        { text: "Description", sortable: false, value: "description" },
        { text: "Numero de serie", sortable: true, value: "numeroSerie" },
        { text : "Update/Delete", sortable: false, value: "actions"},
      ],
    
    
    //Suppression
      dialogDelete: false,
      idDelete:-1,

    //Modification
      dialogEdit: false,
      editedAnalogique: {
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          periodicite: 0,
          uniteAnalogiqueId: 1
      },
      editedNumerique:{
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          uniteNumeriqueId: 1
      },

    //Creation 
      //Analogique
      dialogCreateAnalog: false,
      createAnalog : {
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          periodicite: 0,
          uniteAnalogiqueId: 1
      },
      defaultAnalog : {
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          periodicite: 0,
          uniteAnalogiqueId: 1
      },
      //Numerique
      dialogCreateNum: false,
      createNum : {
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          uniteNumeriqueId: 1
      },
      defaultNum : {
          id: null,
          nom: "string",
          description: "string",
          numeroSerie: "string",
          pontId: this.pont_ID,
          uniteNumeriqueId: 1
      }
    };
  },
 
  methods: {


  //Modification d'un capteur__________________________
    editCapteur(id_capteur) {
      console.log("id du capteur à chercher",id_capteur);
      CapteurService.getCapteur(id_capteur)
      .then(response => {
        const capteurRecupere = response.data; //extrait la donnée de la réponse HTTP
        console.log("full capteur",capteurRecupere);
      
        if (capteurRecupere.uniteAnalogique !== undefined) {
          console.log("Le capteur est analogique");
          this.editedAnalogique = {
            id: capteurRecupere.id,
            nom: capteurRecupere.nom,
            description: capteurRecupere.description,
            numeroSerie : capteurRecupere.numeroSerie,
            pontId: this.pont_ID,//ponId est toujours celui du pont actuel
            periodicite : capteurRecupere.periodicite,
            uniteAnalogiqueId : capteurRecupere.uniteAnalogique.id
          }
          this.isAnalog = true;
          this.isNum = false;
        } else if (capteurRecupere.uniteNumerique !== undefined) {
            console.log("Le capteur est numérique");
            this.editedNumerique = {
              id: capteurRecupere.id,
              nom: capteurRecupere.nom,
              description: capteurRecupere.description,
              numeroSerie: capteurRecupere.numeroSerie,
              pontId: this.pont_ID,//ponId est toujours celui du pont actuel
              uniteNumeriqueId: capteurRecupere.uniteNumerique.id
            }; 
          this.isAnalog = false;
          this.isNum = true;
        }        
        this.dialogEdit = true;
      });
    },
    closeUpdate(){
        this.dialogEdit = false;
        this.isAnalog = false;
        this.isNum = false;
    },
    confirmUpdateAnalogique(){
      console.log("edit capteur analogique",this.editedAnalogique);
        this.$store.dispatch("capteurs/updateCapteurAnalog",this.editedAnalogique);
        this.dialogEdit = false;
        this.isAnalog = false;
        this.isNum = false;
    },
    confirmUpdateNumerique(){
        console.log("edit capteur numerique",this.editedNumerique);
        this.$store.dispatch("capteurs/updateCapteurNum",this.editedNumerique);
        this.dialogEdit = false;
        this.isAnalog = false;
        this.isNum = false;
    },


  //Suppression d'un capteur____________________________
    deleteCapteur(id){
      this.idDelete = id;
      this.dialogDelete = true;
    },
    closeDelete(){
      this.dialogDelete = false;
    },
    confirmDelete(){
      this.$store.dispatch("capteurs/deleteCapteur",this.idDelete);
      this.dialogDelete = false;
    },


  //Creation d'un capteur________________________________
    saveAnalogique(){
      var data = {
        id: null,
        nom: this.createAnalog.nom,
        description: this.createAnalog.description,
        numeroSerie: this.createAnalog.numeroSerie,
        pontId: this.pont_ID,
        periodicite: this.createAnalog.periodicite,
        uniteAnalogiqueId: this.createAnalog.uniteAnalogiqueId
      }
      console.log("capteur à créer dans le pont d'id",this.pont_ID,data);
      this.$store.dispatch("capteurs/createCapteurAnalog",data);
      Object.assign(this.createAnalog,this.defaultAnalog);
      this.dialogCreateAnalog = false;
    },

    saveNumerique(){
      var data = {
        id: null,
        nom: this.createNum.nom,
        description: this.createNum.description,
        numeroSerie: this.createNum.numeroSerie,
        pontId: this.pont_ID,
        uniteNumeriqueId: this.createNum.uniteNumeriqueId
      }
      console.log("capteur à créer dans le pont d'id",this.pont_ID,data);
      this.$store.dispatch("capteurs/createCapteurNum",data);
      Object.assign(this.createNum,this.defaultNum);
      this.dialogCreateNum = false;
    }
  },

  mounted() { 
    this.pont_ID = this.$route.params.id_pont;
    //console.log(this.pont_ID);
    this.$store.dispatch('capteurs/getCapteurs',this.pont_ID);

    this.$store.dispatch('unites/getUnitesAnalogiques');
    this.$store.dispatch('unites/getUnitesNumeriques');
  },

  computed: {
   
    capteurs1(){
        return this.$store.getters["capteurs/getCapteursInPont"];
    },
    unitesA(){
      return this.$store.getters["unites/getUnitesA"];
    },
    unitesN(){
      return this.$store.getters["unites/getUnitesN"];
    },
     
  },

  watch: {
    capteurs1:function(capteurs){
      this.capteurs = capteurs;
      //console.log("capteurs recupérés :",this.capteurs);
    },

    unitesA:function(unites){
      this.unitesAnalogiques = unites;
      //console.log("unites analogiques",this.unitesAnalogiques);
    },
    unitesN:function(unites){
      this.unitesNumeriques = unites;
      //console.log("unites numeriques",this.unitesNumeriques);
    }
  },

};
</script>







<style scoped>

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
  background-color: rgb(50, 150, 163) !important;
  color: #ffffff !important;
}
</style>