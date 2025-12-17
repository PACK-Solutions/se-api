# Page d'accueil de l'API — Connaissance Client

Cette page décrit fonctionnellement l’API « Connaissance Client » (KYC) exposée par le service. Ce n’est pas une spécification OpenAPI, mais une documentation opérationnelle avec des exemples d’appels.

### Définition — API Personne (assurance)

Une API Personne complète dans l’assurance centralise la gestion des données d’identité et de relation d’un assuré: KYC/connaissance client (PPE, proches PPE, niveaux de vigilance), état civil, adresses (postales et électroniques), moyens de contact, liens familiaux/contractuels, consentements et préférences, ainsi que l’historique/audit des mises à jour. Elle fournit des opérations cohérentes et traçables pour alimenter les processus métier (souscription, indemnisation/sinistre, conformité LCB-FT, CRM) tout en respectant la gouvernance des données et la réglementation (RGPD).

Pour l’exploration interactive de la spec OpenAPI: Swagger UI est disponible sur `/swagger` quand l’application tourne en local.

## Objectifs

- Créer/mettre à jour les informations de connaissance client d’une personne
- Consulter les informations actuelles
- Consulter l’historique des modifications
- Vérifier la santé du service

## Base URL

- Local: `http://localhost:8080`
- Exemple d’environnement: `{{baseUrl}} = http://localhost:8080`

## Authentification et en-têtes

- X-Gravitee-Api-Key: clé API fournie par la plateforme d’API Management (Gravitee). À transmettre dans l’en-tête `X-Gravitee-Api-Key` lorsqu’un gateway la requiert. Le service applicatif n’en fait pas le contrôle directement, mais l’appel en production passera via le gateway.
- login (obligatoire): identifiant de l’utilisateur appelant (ex. `dev.user`).
- tenantId (obligatoire): identifiant du tenant/pack (ex. `pack`).

Notes:
- Les en-têtes `login` et `tenantId` sont obligatoires pour toutes les routes métier. En leur absence, l’API retourne `400 Bad Request` avec un `application/problem+json`.
- Les routes `/health`, `/swagger` et `/metrics` ne requièrent pas ces en-têtes.

## Formats

- Content-Type des requêtes JSON: `application/json`
- Content-Type des erreurs: `application/problem+json`

## Endpoints

### 1) Récupérer la connaissance client

- Méthode/URL: `GET /personnes/{idPersonne}/connaissance-client`
- En-têtes requis: `login`, `tenantId` (+ éventuellement `X-Gravitee-Api-Key` via gateway)
- Réponse: 200 avec le JSON de la connaissance client

Exemple curl:

```bash
curl -X GET \
  "http://localhost:8080/personnes/1/connaissance-client" \
  -H "login: dev.user" \
  -H "tenantId: pack" \
  -H "X-Gravitee-Api-Key: ${GRAVITEE_KEY}"
```

Exemple de réponse (indicatif):

```json
{
  "idPersonne": 1,
  "statutPPE": { "mandat": { "fonction": "CHEF_ETAT", "dateFin": null } },
  "statutProchePPE": { "lienParente": "CONJOINT", "mandat": { "fonction": "MEMBRE_GOUVERNEMENT", "dateFin": "2030-12-31" } },
  "vigilance": { "vigilanceRenforcee": true, "motifs": ["MONTANT_ELEVE", "OPERATION_COMPLEXE"] }
}
```

### 2) Créer ou mettre à jour (modification) la connaissance client

- Méthode/URL: `POST /personnes/{idPersonne}/connaissance-client`
- En-têtes requis: `login`, `tenantId`
- Corps attendu (exemple):

```json
{
  "statutPPE": { "mandat": { "fonction": "CHEF_ETAT" } },
  "statutProchePPE": {
    "lienParente": "CONJOINT",
    "mandat": { "fonction": "MEMBRE_GOUVERNEMENT", "dateFin": "2030-12-31" }
  },
  "vigilance": { "vigilanceRenforcee": true, "motifs": ["MONTANT_ELEVE", "OPERATION_COMPLEXE"] }
}
```

- Réponses possibles:
  - `201 Created` avec l’objet envoyé (succès)
  - `304 Not Modified` si aucune modification n’a été détectée
  - `500 Internal Server Error` avec `application/problem+json` en cas d’erreur métier (ex. vigilance renforcée obligatoire non respectée)

Exemple curl:

```bash
curl -X POST \
  "http://localhost:8080/personnes/1/connaissance-client" \
  -H "Content-Type: application/json" \
  -H "login: dev.user" \
  -H "tenantId: pack" \
  -d '{
        "statutPPE": { "mandat": { "fonction": "CHEF_ETAT" } },
        "statutProchePPE": { "lienParente": "CONJOINT", "mandat": { "fonction": "MEMBRE_GOUVERNEMENT", "dateFin": "2030-12-31" } },
        "vigilance": { "vigilanceRenforcee": true, "motifs": ["MONTANT_ELEVE", "OPERATION_COMPLEXE"] }
      }'
```

### 3) Corriger (correction) la connaissance client

- Méthode/URL: `PUT /personnes/{idPersonne}/connaissance-client`
- En-têtes requis: `login`, `tenantId`
- Corps attendu (exemple):

```json
{
  "statutPPE": { "mandat": { "fonction": "CHEF_ETAT" } },
  "statutProchePPE": {
    "lienParente": "ENFANT",
    "mandat": { "fonction": "MEMBRE_GOUVERNEMENT", "dateFin": "2031-12-31" }
  },
  "vigilance": { "vigilanceRenforcee": true, "motifs": ["AGE_AVANCE"] }
}
```

- Réponses possibles:
  - `201 Created` avec l’objet envoyé (succès)
  - `500 Internal Server Error` avec `application/problem+json` en cas d’erreur métier

Exemple curl:

```bash
curl -X PUT \
  "http://localhost:8080/personnes/1/connaissance-client" \
  -H "Content-Type: application/json" \
  -H "login: dev.user" \
  -H "tenantId: pack" \
  -d '{
        "statutPPE": { "mandat": { "fonction": "CHEF_ETAT" } },
        "statutProchePPE": { "lienParente": "ENFANT", "mandat": { "fonction": "MEMBRE_GOUVERNEMENT", "dateFin": "2031-12-31" } },
        "vigilance": { "vigilanceRenforcee": true, "motifs": ["AGE_AVANCE"] }
      }'
```

### 4) Historique des modifications

- Méthode/URL: `GET /personnes/{idPersonne}/historique-connaissance-client`
- En-têtes requis: `login`, `tenantId`
- Réponse: `200 OK` avec un JSON contenant l’historique (liste d’événements)

Exemple curl:

```bash
curl -X GET \
  "http://localhost:8080/personnes/1/historique-connaissance-client" \
  -H "login: dev.user" \
  -H "tenantId: pack"
```

Exemple de réponse (indicatif):

```json
{
  "idPersonne": 1,
  "modifications": [
    {
      "date": "2025-01-01T12:34:56Z",
      "type": "MODIFICATION",
      "user": "dev.user"
    },
    {
      "date": "2025-02-10T08:10:00Z",
      "type": "CORRECTION",
      "user": "ops.user"
    }
  ]
}
```

### 5) Santé du service

- `GET /health` (réponse JSON) et `HEAD /health` (statut uniquement)
- En-têtes requis: aucun
- Réponses: `200 OK` si UP, `503 Service Unavailable` sinon

Exemple curl:

```bash
curl -I http://localhost:8080/health
```

## Gestion des erreurs (Problem Details)

Le service renvoie des erreurs au format RFC 9457 (« Problem Details ») avec `Content-Type: application/problem+json`.

Exemple — en-tête manquant:

```http
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json

{
  "type": "about:blank",
  "status": 400,
  "title": "Bad Request",
  "detail": "le header login est manquant",
  "code": "BAD_REQUEST"
}
```

## Swagger UI / OpenAPI

- UI: `GET /swagger`
- Fichier OpenAPI: `rest/src/main/resources/openapi/documentation.yaml`

## Exemples rapides avec variables d’environnement

```bash
export BASE_URL=http://localhost:8080
export LOGIN=dev.user
export TENANT=pack
export GRAVITEE_KEY=xxxxx # si requis par le gateway

curl -H "login: $LOGIN" -H "tenantId: $TENANT" -H "X-Gravitee-Api-Key: $GRAVITEE_KEY" "$BASE_URL/personnes/1/connaissance-client"
```

## Remarques

- Les exemples de corps de requête proviennent des fichiers `.http` du projet.
- Les valeurs de code métier (fonctions, motifs de vigilance, etc.) doivent respecter les énumérations attendues par le service.
