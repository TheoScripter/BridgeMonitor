<template>
    <v-card>
        <v-card-title>
            <h2>Regions France</h2>
        </v-card-title>

        <v-card-text>
            <v-data-table :headers="headers" :items="regions">

                <template v-slot:[`item.actions`]="{ item }">
                    <v-icon small @click="editRegion(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteRegion(item.id)">mdi-delete</v-icon>
                </template>
            
                <template v-slot:top>

                    <v-dialog v-model="dialogDelete" max-width="500">
                        <v-card>
                            <v-card-title>
                                <h4>Etes-vous sûr de vouloir supprimer ?</h4>
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
                                <h4>Mise à jour de la Region {{editedRegion}}</h4>
                            </v-card-title>
                            <v-card-text>
                                <v-text-field v-model="editedRegion.id" label="Id" readonly disabled/>
                                <v-text-field v-model="editedRegion.nom" label="Nom de la Region" required/>
                                <v-text-field v-model="editedRegion.codeRegion" label="Code de la Region"/>
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
        </v-card-text>

    </v-card>
</template>

<script>

    export default {
        name:"ListeRegions",
        data(){
            return {
                regions : [],
                headers : [
                    {text : "Id", sortable: true, value : "id"},
                    {text : "Nom", sortable: true, value : "nom"},
                    {text : "Code de la region", sortable: true, value: "codeRegion"},
                    {text : "Actions", sortable: false, value: "actions"}
                ],
                dialogDelete:false,
                idDelete: -1,

                dialogEdit: false,
                editedRegion:{
                    id: -1,
                    nom: "",
                    codeRegion: null
                }
            }
        },
        computed : {
            regions1(){
                return this.$store.getters['regions/getAllRegions'];
            }
        },

        methods:{
            deleteRegion(id){
                this.idDelete = id;
                this.dialogDelete = true;
            },
            closeDelete(){
                this.dialogDelete = false;
            },
            confirmDelete(){
                this.$store.dispatch("regions/deleteRegion",this.idDelete);
                this.dialogDelete = false;
            },

            editRegion(Region){
                this.editedRegion = Object.assign ({},Region);
                this.dialogEdit = true;
            },
            closeUpdate(){
                this.dialogEdit = false;
            },
            confirmUpdate(){
                this.$store.dispatch("regions/updateRegion",this.editedRegion);
                this.dialogEdit = false;
            }
        },

        watch:{
            regions1: function(regions1){
                this.regions = regions1;
            }
        },

        mounted(){
            this.$store.dispatch('regions/getRegions')
        }
    }

</script>