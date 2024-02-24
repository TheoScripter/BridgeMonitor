import Vue from 'vue'
import VueRouter from 'vue-router'
import DashBoard from "@/views/DashBoard.vue";
import OverViewRegion from "@/views/OverViewRegion.vue";
import OverViewPont from "@/views/OverViewPont.vue";
import OverViewAdmin from "@/views/OverViewAdmin.vue";
import OverViewError from "@/views/OverViewError.vue";

import AdminListePonts from "@/components/Admin/AdminListePonts.vue";
import AdminListeCapteurs from "@/components/Admin/AdminListeCapteurs.vue";
import store from "@/store";

Vue.use(VueRouter)

const routes = [

    //Chemins admin---------------------------------------------------
      {
        path: '/admin',
        name: 'OverViewAdmin',
        component: OverViewAdmin,
          meta: { requiresAdmin: true }, // Ajoutez la meta pour indiquer que le rôle d'admin est requis
          children : [
            {
                path: 'regions/:id_region/ponts',
                name: 'AdminListePonts',
                component: AdminListePonts,
                props: true,
            },
            {
                path: 'regions/:id_region/ponts/:id_pont/capteurs',
                name: 'AdminListeCapteurs',
                component: AdminListeCapteurs,
                props: true
            }
        ]
    },

    //Chemins visiteur---------------------------------------------
    {
        path: '/',
        redirect: '/regions',
        meta: { requiresAuth: false }
    },

    {
        path: '/regions',
        name: 'Regions',
        component: OverViewRegion,
    },

    {
        path: '/regions/:id_region/ponts',
        name: 'OverViewPont',
        component: OverViewPont,

    },

    {
        path : '/regions/:id_region/ponts/:id_pont',
        name: 'DashBoard',
        component: DashBoard,
        props: true
    },

    //Autres chemins------------------------------------------------

    {
        path: '/error',
        name: 'Error',
        component: OverViewError
    },

    {
        path: '*',
        redirect: { name: 'Error' },
    },

];

const router = new VueRouter({
    routes
});

// FIXME: Frontend n'est pas censé a gerer la gestion de données
router.beforeEach((to, from, next) => {
    // Vérifie si la route nécessite le rôle d'administrateur
    if (to.matched.some((route) => route.meta.requiresAdmin)) {
        if (store.getters.isAdmin) {
            next(); // L'utilisateur a le rôle nécessaire, autoriser l'accès
        } else {
            // Redirige vers une page d'erreur ou une autre page appropriée
            next('/error');
        }
    } else {
        next(); // Si la route n'a pas de restriction de rôle, autoriser l'accès
    }
});

export default router

