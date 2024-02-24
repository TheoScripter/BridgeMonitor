import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import vuetify from './plugins/vuetify';
import Keycloak from 'keycloak-js';

Vue.config.productionTip = false;

let initOptions = {
  url: process.env.KEYCLOAK_URL || "http://localhost:8080",
  realm: 'bridgemonitor-realm',
  clientId: 'bridgemonitor-frontend',
  onLoad: 'login-required',
  enableLogging: true,
};

let keycloak = new Keycloak(initOptions);

keycloak.init({ onLoad: initOptions.onLoad, 'checkLoginIframe': false }).then(
    (auth) => {
      if (!auth) {
        window.location.reload();
      } else {
        sessionStorage.setItem('vue-token', keycloak.token);
        console.log('--> keycloak-token : ', keycloak.token);
        console.log(
            "--> sessionStorage('vue-token') : ",
            sessionStorage.getItem('vue-token')
        );

        // Continue with the app initialization
        new Vue({
          router,
          store,
          vuetify,
          render: (h) => h(App, { props: { keycloak: keycloak } }),
        }).$mount('#app');

        let payload = {
          idToken: keycloak.idToken,
          accessToken: keycloak.idToken,
        };

        if (keycloak.token && keycloak.idToken && keycloak.token !== '' && keycloak.idToken !== '') {
          store.commit('login', payload);
          payload = {
            username: keycloak.tokenParsed.preferred_username,
            givenName: keycloak.tokenParsed.given_name,
            familyName: keycloak.tokenParsed.family_name,
            email: keycloak.tokenParsed.email,
            adminRole: keycloak.hasResourceRole('admin'),
          };
          store.commit('setName', payload);

          
        } else {
          store.commit('logout');
          sessionStorage.setItem('vue-token', "");
        }
      }
    }
).catch(() => {
  alert('Login failure');
});

Vue.prototype.$keycloak = keycloak;
//
// import Vue from 'vue';
// import App from './App.vue';
// import router from './router';
// import store from './store';
// import vuetify from './plugins/vuetify';
// import Keycloak from 'keycloak-js';
//
// Vue.config.productionTip = false;
//
// let initOptions = {
//   url: 'http://localhost:8081',
//   realm: 'bridgemonitor-realm',
//   clientId: 'bridgemonitor-frontend',
//   onLoad: 'check-sso',
//   enableLogging: true,
// };
//
// let keycloak = new Keycloak(initOptions);
//
// keycloak.init({ onLoad: initOptions.onLoad, 'checkLoginIframe': false }).then((auth) => {
//   if (auth) {
//     sessionStorage.setItem('vue-token', keycloak.token);
//     console.log('--> keycloak-token : ', keycloak.token);
//     console.log("--> sessionStorage('vue-token') : ", sessionStorage.getItem('vue-token'));
//
//     let payload = {
//       idToken: keycloak.idToken,
//       accessToken: keycloak.idToken,
//     };
//
//     if (keycloak.token && keycloak.idToken && keycloak.token !== '' && keycloak.idToken !== '') {
//       store.commit('login', payload);
//       payload = {
//         username: keycloak.tokenParsed.preferred_username,
//         givenName: keycloak.tokenParsed.given_name,
//         familyName: keycloak.tokenParsed.family_name,
//         email: keycloak.tokenParsed.email,
//         adminRole: keycloak.hasResourceRole('admin'),
//       };
//       store.commit('setName', payload);
//
//       // Continue with the app initialization
//       new Vue({
//         router,
//         store,
//         vuetify,
//         render: (h) => h(App, { props: { keycloak: keycloak } }),
//       }).$mount('#app');
//     } else {
//       store.commit('logout');
//       sessionStorage.setItem('vue-token', null);
//     }
//   }
// }).catch(() => {
//   // Traitement en cas d'échec de l'initialisation
//   alert('Login failure');
// });
//
// Vue.prototype.$keycloak = keycloak;