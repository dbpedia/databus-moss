# databus-moss-docker

Run

```
git clone https://github.com/dbpedia/databus-moss-docker.git
cd databus-moss-docker
docker compose pull
docker compose up
```

Go to 
```
http://localhost:5000
```

Login at the top right and click on your username. Alternatively go to
```
http://localhost:5000/user
```

Navigate to `Keys`, create an API key and save your key on your local machine. Your API key should look something like this:
```
OWM3ZTY3NTItNjQ2ZC00ZDE5LWE4ZTctYjc4NGFkNjkyZDFj_17784836-6dc2-4880-96ac-42a0f3570bb2
```

To create a layer on your MOSS instance, issue the following `curl`:
```
curl -X POST 'http://localhost:5000/api/save?layer=simple&resource=https://databus.openenergyplatform.org/hu_wu/test_group' \
  -H 'accept: application/json' \
  -H 'X-API-KEY: OWM3ZTY3NTItNjQ2ZC00ZDE5LWE4ZTctYjc4NGFkNjkyZDFj_17784836-6dc2-4880-96ac-42a0f3570bb2' \
  -H 'Content-Type: application/ld+json' \
  -d '{
  "@context" : "https://raw.githubusercontent.com/dbpedia/databus-moss/dev/devenv/context2.jsonld",
  "@id" : "https://databus.openenergyplatform.org/hu_wu/test_group",
  "isAbout": "http://openenergy-platform.org/ontology/oeo/OEO_00020054"      
}'
```

Searching for `nuclear` in the main search field at `http://localhost:5000` should now show the new layer.


## Containers

`dbpedia/databus-moss-frontend:dev`
Frontend using Svelte. Running on port `5000`

`dbpedia/databus-moss-server:dev` 
Server with MOSS API. Running on port `5001`

`dbpedia/lookup:dev`
Lookup Server for Search API. Running on port `5002`

`dbpedia/gstore:dev`
Gstore. Running on port `5003`

`openlink/virtuoso-opensource-7:latest`
Triple Store Backend. Running on port `5004`

## Proxy Setup

If both backend and frontend are on the same host, MOSS requires some routes to be proxied to the backend (listed as apache2 proxy pass and `http://localhost:5001/` as the address of the backend container):

```
ProxyPass /api http://localhost:5001/api
ProxyPass /layer http://localhost:5001/layer
ProxyPass /g http://localhost:5001/g
ProxyPass /file http://localhost:5001/file
ProxyPass /sparql http://localhost:5001/sparql
```

`sparql` can optionally be proxied to the virtuoso container directly.

## Environment Variables

### Frontend:
```
# URL for backend requests
PUBLIC_MOSS_BASE_URL: https://dev.moss.dbpedia.org
# Required for authentication: Origin and OIDC parameters
ORIGIN: https://dev.moss.dbpedia.org
AUTH_OIDC_CLIENT_ID: moss-dev
AUTH_OIDC_CLIENT_SECRET: z7feqbX7lGyAFzPzGIaC4LT7vidPqrP5
AUTH_OIDC_ISSUER: https://auth.dbpedia.org/realms/dbpedia
AUTH_OIDC_CLIENT_SCOPE: "openid profile email"
```


### Backend:
```
# Base path for MOSS identifiers
MOSS_BASE_URL: https://dev.moss.dbpedia.org
# Path of the sqlite user database
USER_DATABASE_PATH: /users/users.db
# Configuration file
CONFIG_PATH: /config/moss-config.yml
# Other services in the environment
LOOKUP_BASE_URL: http://lookup:8082
GSTORE_BASE_URL: http://gstore:8080
```