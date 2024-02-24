# BridgeMonitorMain

Ce document contient les informations nécessaires pour installer et configurer Git, ainsi que les conventions de commit à suivre pour le projet. 😁


[[_TOC_]]


## Installation
Pour télecharger le projet sur votre machine:
```shell script
git clone git@gricad-gitlab.univ-grenoble-alpes.fr:cs550-applications-iot/2023-2024/cs550-groupe05/bridgemonitor.git
```

## Compilation et Lancement de l'application bridgemonitor

L'application peut être compilé avec:

```shell script
docker compose up keycloak &
mvn package
docker compose down
```

Pour lancer le système complet, après avoir fait le package via le maven, il suffit d'utiliser le script docker compose fourni dans la racine du projet.

```shell script
docker compose up
```

## Exporter l'état du keycloak

Pour enregistrer votre état de la royaume, des clients, des utilisateurs et des rôles, procéder par la suite :

Executer keycloak sur docker, puis créer votre royaume et vos utilisateurs :

```shell script
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:19.0.3 start-dev
```

Quand vous avez fini et que vous voulez enregistrer l'état de votre keycloak, cherchez l'id de votre docker 
container, pour mon cas l'id est 5b1da398f8df et mon realm bridgemonitor-realm:

```shell script
docker ps
docker exec -it 5b1da398f8df bash
```

Exportez l'état de votre realm :

```shell script
/opt/keycloak/bin/kc.sh export --file /opt/keycloak/data/export.json --realm bridgemonitor-realm
```

Quittez le bash du docker :

```shell script
exit
```

Copiez l'état de votre royaume dans votre ordinateur :

```shell script
docker cp 5b1da398f8df:/opt/keycloak/data/export.json ./export.json
```

Maintenant vous pouvez fermer votre docker :

```shell script
docker stop 5b1da398f8df
docker remove 5b1da398f8df
```

Pour ensuite re-démarrer docker en reprenant l'état de votre royaume (attention à mettre le path absolue pour votre fichier export.json): 

```shell script
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v ./src/main/docker/export.json:/opt/keycloak/data/import/export.json quay.io/keycloak/keycloak:19.0.3 start-dev --import-realm
```
