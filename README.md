# PMTv2 - Project Management Tool

Application de gestion de projet collaboratif pour les equipes de developpement logiciel.

## Technologies

- **Frontend** : Angular 20, Angular Material
- **Backend** : Java 21, Spring Boot 3.5
- **Base de donnees** : PostgreSQL
- **Tests** : JUnit 5, Mockito, JaCoCo (backend) / Jasmine, Karma (frontend)
- **CI/CD** : GitHub Actions
- **Conteneurisation** : Docker, Docker Compose

## Pre-requis

- Java 21
- Node.js 20+
- PostgreSQL 16+
- Docker et Docker Compose (pour le deploiement conteneurise)

## Structure du projet

```
PMTv2/
  pmtv2-backend/       # API Spring Boot
  pmtv2-frontend/      # Application Angular
  docker-compose.yml   # Orchestration des conteneurs
  backup_pmt_db.sql/   # Schema SQL et donnees de test
  .github/workflows/   # Pipeline CI/CD
```

## Installation locale

### Base de donnees

Creer la base de donnees PostgreSQL et executer le script d'initialisation :

```bash
createdb pmt_db_v2
psql -d pmt_db_v2 -f backup_pmt_db.sql/init.sql
```

### Backend

```bash
cd pmtv2-backend
./mvnw spring-boot:run
```

Le backend demarre sur `http://localhost:8080`.

### Frontend

```bash
cd pmtv2-frontend
npm install
ng serve
```

Le frontend demarre sur `http://localhost:4200`.

## Tests

### Backend

```bash
cd pmtv2-backend
./mvnw verify
```

Le rapport de couverture JaCoCo est genere dans `target/site/jacoco/index.html`.

### Frontend

```bash
cd pmtv2-frontend
ng test --watch=false --browsers=ChromeHeadless --code-coverage
```

Le rapport de couverture est genere dans `coverage/`.

## Deploiement avec Docker

### Construction et lancement avec Docker Compose

```bash
docker-compose up --build
```

Cela demarre trois conteneurs :
- **db** : PostgreSQL sur le port `5432`
- **backend** : API Spring Boot sur le port `8080`
- **frontend** : Angular (nginx) sur le port `4200`

### Construction des images individuelles

```bash
# Backend
docker build -t pmtv2-backend ./pmtv2-backend

# Frontend
docker build -t pmtv2-frontend ./pmtv2-frontend
```

### Push des images sur Docker Hub

```bash
docker tag pmtv2-backend <DOCKERHUB_USERNAME>/pmtv2-backend:latest
docker push <DOCKERHUB_USERNAME>/pmtv2-backend:latest

docker tag pmtv2-frontend <DOCKERHUB_USERNAME>/pmtv2-frontend:latest
docker push <DOCKERHUB_USERNAME>/pmtv2-frontend:latest
```

## Pipeline CI/CD

Le fichier `.github/workflows/ci-cd.yml` definit une pipeline GitHub Actions avec trois jobs :

1. **backend** : Compile, execute les tests et verifie la couverture (>60%)
2. **frontend** : Installe les dependances, execute les tests avec couverture
3. **docker** : Construit et pousse les images Docker sur Docker Hub (uniquement sur la branche `master`)

### Configuration requise

Ajouter les secrets suivants dans les parametres du repository GitHub :
- `DOCKERHUB_USERNAME` : identifiant Docker Hub
- `DOCKERHUB_TOKEN` : token d'acces Docker Hub

## API Endpoints

| Methode | URL | Description |
|---------|-----|-------------|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |
| GET | `/api/users` | Liste des utilisateurs |
| GET | `/api/users/{id}` | Detail utilisateur |
| GET | `/api/projects` | Liste des projets |
| POST | `/api/projects` | Creer un projet |
| PUT | `/api/projects/{id}` | Modifier un projet |
| DELETE | `/api/projects/{id}` | Supprimer un projet |
| GET | `/api/tasks` | Liste des taches |
| POST | `/api/tasks` | Creer une tache |
| PUT | `/api/tasks/{id}` | Modifier une tache |
| POST | `/api/tasks/{id}/assign` | Assigner une tache |
| DELETE | `/api/tasks/{id}` | Supprimer une tache |
| GET | `/api/project-members` | Liste des membres |
| POST | `/api/project-members` | Ajouter un membre |
| GET | `/api/notifications` | Liste des notifications |
| GET | `/api/history` | Historique des modifications |
| GET | `/api/history/task/{id}` | Historique par tache |
